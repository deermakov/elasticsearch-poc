# elasticsearch-poc
PoC Spring Data Elasticsearch, включая:
- ElasticsearchRepository,
- Entity callbacks (on IndividualEntrepreneur)
- Nested fields (Deal.participants)
- Embedded data model
  - т.к. Elasticsearch - это продукт, заточенный на поиск, а не на оптимальное хранение данных. А поиск быстрее всего работает с денормализованными данными. В целом, Elasticsearch имеет слабую поддержку нормализованной модели данных и не рекомендует ее использовать, см. [Join field type](https://www.elastic.co/guide/en/elasticsearch/reference/current/parent-join.html)
- M:N relationships,
- ?? Обновление (merge) графа сущностей, включая обновление (в т.ч. перепривязку) вложенных сущностей

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

1. Пример создания ИП вместе с его ФЛ (ФЛ выписывается в отдельный документ вручную - с помощью entity callback):
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

2. ?? Пример создания ИП со ссылкой на существующего ФЛ, при этом также обновляется этот ФЛ (поле "fio").
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
3. Пример создания сделки вместе с участниками (участники выписываются в отдельные документы автоматом - с помощью @Field(type = Nested))
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
4. ?? Пример создания сделки со ссылкой на существующего участника-ЮЛ,
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
