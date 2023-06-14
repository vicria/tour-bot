# сервис "Адаптер телеграм"
mvn clean package -Dfile.encoding=UTF-8

# Контейнер для сборки
На тестовой машине докер раннер для сборки и деплоя, на нем собран контейнер
maven:3.8.6-liberica-jdk-11 из Dockerfile в корне проекта.
- docker build -t maven:3.8.6-liberica-jdk-11 .

# Тестовое покрытие
mvn clean test создает файл subte/subte-microservice/target/jacoco.csv
командой можно смотреть процент покрытия:
awk -F"," '{ instructions += $6 + $7; covered += $7 } END { print covered, "/", instructions, " instructions covered"; print 100*covered/instructions, "% covered" }' `find . -name "jacoco.csv"`
Последний замер 13 апреля -> 71.0526 % covered