[![Actions Status](https://github.com/a-oselkov/java-project-73/workflows/hexlet-check/badge.svg)](https://github.com/a-oselkov/java-project-73/actions)
![Java CI](https://github.com/a-oselkov/java-project-73/actions/workflows/Java-CI.yml/badge.svg)
[![Maintainability](https://api.codeclimate.com/v1/badges/ac87fdb9caec56dfac5b/maintainability)](https://codeclimate.com/github/a-oselkov/java-project-73/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/ac87fdb9caec56dfac5b/test_coverage)](https://codeclimate.com/github/a-oselkov/java-project-73/test_coverage)

**Менеджер Задач** | Веб - приложение, система управления задачами. Для работы с приложением предусмотрена регистрация и аутоинтефикация пользователя. Далее пользователь имеет возможность создавать/редактировать/удалять задачи, создавать собственные метки и статусы. При просмотре списка задач предусмотрена фильтрация.


Чтобы ознакомиться с приложением, вы можете зарегистрировать пользователя или воспользоваться тестовым:

Логин: **test@user.com**

Пароль: **111**


https://task-manager-8yjq.onrender.com/users - приложение в сети.

https://task-manager-8yjq.onrender.com/api-docs/swagger-ui/index.html - документация.
___
Для локального запуска:
```
make run
```

http://localhost:5001/ - адрес локально запущенного приложения.

http://localhost:5001/api-docs/ - документация при локальном запуске.
___

На проекте используется: 
- Spring Framework(Spring Boot, Web MVC, Security, Data)
- Hibernate ORM
- PostgeSQL
- JUnit, Mockito, Testcontainers
- Liquibase
- Mapstruct
- OpenApi
- Lombok
