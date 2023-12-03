# сервис "Адаптер телеграм"
mvn clean package -Dfile.encoding=UTF-8

# Контейнер для сборки
На тестовой машине докер раннер для сборки и деплоя, на нем собран контейнер
maven:3.8.6-liberica-jdk-11 из Dockerfile в корне проекта.
- docker build -t maven:3.8.6-liberica-jdk-11 .

# Тестовое покрытие
mvn clean test создает файл telegram/telegram-microservice/target/jacoco.csv
командой можно смотреть процент покрытия:
cd telegram/telegram-microservice/target/
awk -F"," '{ instructions += $6 + $7; covered += $7 } END { print covered, "/", instructions, " instructions covered"; print 100*covered/instructions, "% covered" }' `find . -name "jacoco.csv"`
Последний замер 3 декабря -> 87.7358 % covered
