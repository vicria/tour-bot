# subte-bot

Бот для обучения "Разработка на Java"
Все задачи по проекту https://trello.com/b/03pO8EkE/subway
Видео, как брать задачи https://youtu.be/V84Ky7sGDjg

# Релиз 1.0.0
Модуль Subte - работа с базой данных (postgres), рассчет времени пути
Модуль адаптер telegram - коннект к телеграм боту, работа с кнопками, обращение к модулю Subte

ОБЯЗАТЕЛЬНО прописываем используемые хосты в /etc/hosts
https://www.manageengine.com/network-monitoring/how-to/how-to-add-static-entry.html
Пример:
127.0.0.1       db
127.0.0.1       subte
127.0.0.1       telegram

Видео 1 - Запуск без докера https://youtu.be/gyLzejdkGYU
Видео 2 - Запуск в докере или частично https://youtu.be/ZRgRq18GpeU

Создание бота - получение username и token:
1) Заходим в телеграме в бот @BotFather
2) вызываем команду /newbot
3) выбираем имя, username
4) получаем Use this token to access the HTTP API - это токен. пример 5988992902:AAHZIr0aKSzdBSxiF9I13vRjq_bVVeiu7s8

# Релиз 1.1.0

ОБЯЗАТЕЛЬНОЕ использование докера. Тесты перенесены на запуск в контейнерах. Микросервисы subte и telegram
общаются через Apache Kafka

запуск:
mvn clean install
docker compose up

# Релиз 1.2.0

В разработке
