# elasticsearch-poc
PoC Spring Data Elasticsearch, включая:
- MongoRepository,
- Embedded и Normalized data models,
- M:N relationships,
- Aggregation pipelines,
- Обновление (merge) графа сущностей, включая обновление (в т.ч. перепривязку) вложенных сущностей

## Инфраструктура
Исходные compose- и .env-файлы взяты по ссылкам со страницы https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html#docker-compose-file <br/>
Дополнительно в них отключена вся security, чтобы не возиться с SSL-сертификатами.

Конфигурация инфраструктуры: _/docker <br>_
Запуск инфраструктуры: `docker-compose up -d` <br>
Остановка инфраструктуры: `docker-compose down -v` <br>

**ВАЖНО**: для Elasticsearch используемой версии (8.11) нужно установить параметр ОС `vm.max_map_count=262144` (детали - по ссылке выше).
Если запуск производится в Windows в Docker Desktop на базе WSL, то перед каждым (!) запуском
инфраструктуры надо выполнить в CMD 2 команды: <br>
`wsl -d docker-desktop -u root` <br>
`sysctl -w vm.max_map_count=262144` <br>

## Kibana
http://localhost:5601

## Swagger
http://localhost:8090/swagger-ui/index.html

1. Пример создания ИП вместе с его ФЛ:
```json
{
    "@class": "poc.elasticsearch.domain.IndividualEntrepreneur",
    "name": "ИП Бахарев",
    "individual": {
        "@class": "poc.elasticsearch.domain.Individual",
        "fio": "Бахарев Петр Петрович",
        "address": {
            "fullAddress": "Москва, 113327"
        }
    },
    "selfEmployed": true
}
```

2. Пример создания ИП со ссылкой на существующего ФЛ, при этом также обновляется этот ФЛ (поле "fio").
Перед выполнением подставь в _individual.id_ id существующего ФЛ:
```json
{
    "@class": "poc.elasticsearch.domain.IndividualEntrepreneur",
    "name": "ИП Бахарев 2",
    "individual": {
        "@class": "poc.elasticsearch.domain.Individual",
        "id": "64ccee1a6a7dce4e3a8a3f4f",
        "fio": "Бахарев - 2"
    },
    "selfEmployed": false
}
```
3. Пример создания сделки вместе с участниками
```json
{
    "number": "Сделка-1",
    "amount": 11.22,
    "participants": [
        {
            "@class": "poc.elasticsearch.domain.LegalEntity",
            "name": "ООО Ромашка",
            "inn": "111222",
            "address": {
              "fullAddress": "Москва, 113327"
            }
        },
        {
          "@class": "poc.elasticsearch.domain.Individual",
          "fio": "Остап Бендер",
          "inn": "999000",
          "address": {
            "fullAddress": "Одесса, 123"
          }
        }
    ]
}
```
4. Пример создания сделки со ссылкой на существующего участника-ЮЛ,
  при этом также обновляется этот ЮЛ (поле "name").
   Перед выполнением подставь в _participants.id_ id существующего ЮЛ
```json
{
    "number": "Сделка-2",
    "amount": 22.33,
    "participants": [
        {
            "@class": "poc.elasticsearch.domain.LegalEntity",
            "id": "64ccee1a6a7dce4e3a8a3f4f",
            "name": "ООО Ромашка - 2"
        }
    ]
}
```
## Замечания
### 1
Для Работы с MongoDB используются Spring Data Repositories. MongoTemplate не используется.

### 2
Реализованы 2 варианта обновления (merge) сущностей:
- кастомный (с помощью BeanMerger) - см. `IndividualEntrepreneurEventProcessor.updateIndividual()`, проверка сценария - пример № 2 выше
- средствами MongoDB aggregation pipeline - см. `DealEventProcessor.updateParticipants()`, проверка сценария - пример № 4 выше

Реализовано частичное обновление полей (not null поля из запроса перезатирают поля в БД,
а остальные поля в БД остаются как есть).
Но очистка (за'null'ение) полей в БД не реализована для простоты примера.

### 3
Отношение Deal : Party = M:N (Party.deals - Deal.participants),
отсюда циркулярная зависимость этих java-бинов и stack overflow в Jackson
при их сериализации в JSON (напр. при вызове GET /deal/list и GET /party/list).

Для устранения этой проблемы логично не считывать соответствующие данные при выборке, но например через projections это не получится:<br>
в документации сказано "_Projections must not be applied to DBRefs_",
и если всё-таки попробовать сделать такую логику в DealRepository (для примера на поле Party.inn)

`@Query(value = "select d from Deal d", fields = "{'participants.inn': 0 }")` <br>
`List<Deal> findAll();` <br>
то будет exception "_Invalid path reference participants.inn; Associations can only be pointed to directly or via their id property_".
Очевидно, это потому, что у нас Deal.participants считывается через `@DocumentReference`

Возможные решения:

1. Если мы отдаем Entity как есть, то можно применить костыль - очистка данных после считывания, см. `MongoDbAdapter.getAllDeals()`,
а на java beans навешена аннотация `@JsonInclude(JsonInclude.Include.NON_NULL)`, чтобы пустые поля не светились.
Возможно, использование lazy load на выкидываемом поле поможет производительности (не проверено).

2. Если отдаем DTO, то исключаем из него ненужные поля (в PartyDto отсутствует поле deals).
Для оптимизации производительности исключаем считывание данных из БД путем задания lazy load = true (см. _Party.deals_),
что вместе с отсутствим обращения к полю при мапинге обеспечивает отсутствие load'а.
Применено в `MongoDbAdapter.getAllParties()`.

3. Непроверенное решение: возможно, удаление полей на этапе вычитывания данных получится сделать, если
реализовать aggregetion pipeline, в котором и попробовать выполнять фильтрацию полей ($unset ?).