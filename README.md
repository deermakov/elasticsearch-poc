# elasticsearch-poc
PoC Spring Data Elasticsearch, включая:
- ElasticsearchRepository,
- Entity callbacks (on IndividualEntrepreneur)
- Nested fields (Deal.participants)
- Nested query, custom query, source filter (DealRepository.findAllParticipants())

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

1. Пример создания ИП с его ФЛ (ФЛ выписывается в отдельный документ вручную - с помощью entity callback):
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

2. Пример создания сделки с участниками
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

## Замечания
### 1
Elasticsearch - это продукт, заточенный на поиск, а не на оптимальное хранение данных.
Поэтому Elasticsearch не может быть полноценной заменой RDBMS и не подходит на
роль мастер-системы хранения с нормализованной моделью данных, поиском и устранением дубликатов и т.д.
### 2
Elasticsearch имеет слабую поддержку нормализованной модели данных
(Join и Nested fields, Terms query) и сам не рекомендует ее использовать по соображениям производительности. <br/>
Доп. информация:
- [Join field type](https://www.elastic.co/guide/en/elasticsearch/reference/current/parent-join.html)
- [Managing Relations Inside Elasticsearch](https://www.elastic.co/blog/managing-relations-inside-elasticsearch)
- [Joining Two Indexes in Elasticsearch](https://opster.com/guides/elasticsearch/search-apis/elasticsearch-join-two-indexes/)

### 3
В коде есть ручное объединение результатов нескольких запросов к разным индексам - см.
`poc.elasticsearch.adapter.elasticsearch.ElasticsearchAdapter.getAllParties()`.
Можно попробовать заменить это на один запрос с помощью [Multi search API](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-multi-search.html)