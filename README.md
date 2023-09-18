# REST API для аутентификации и регистрации пользователей

Это REST API предоставляет конечные точки для аутентификации и регистрации пользователей. Он позволяет пользователям зарегистрироваться, войти в систему и получить информацию о себе.

## Технологии

Для разработки данного API были использованы следующие технологии:

- Java - основной язык программирования
- Spring Boot - фреймворк для создания приложений на Java
- Spring WebFlux - модуль для разработки реактивных веб-приложений
- Spring Security - модуль для обеспечения безопасности приложения
- Maven - инструмент для управления зависимостями и сборки проекта

## Запуск приложения

1. Установите Java Development Kit (JDK) версии 8 или выше, если еще не установлено.
2. Скачайте исходный код проекта из репозитория GitHub.
3. Откройте командную строку или терминал и перейдите в каталог проекта.
4. Выполните команду mvn spring-boot:run, чтобы запустить приложение.
5. Приложение будет доступно по адресу http://localhost:8083.

## Запуск тестов

1. Убедитесь, что приложение не запущено.
2. Откройте командную строку или терминал и перейдите в каталог проекта.
3. Выполните команду mvn test, чтобы запустить тесты.
4. Результаты тестов будут отображены в командной строке или терминале.

## Конечные точки API

API предоставляет следующие конечные точки:

- POST /api/v1/auth/register - регистрация нового пользователя.
- POST /api/v1/auth/login - аутентификация пользователя и получение информации для входа.
- GET /api/v1/auth/info - получение информации о вошедшем в систему пользователе.‍


## Примеры использования REST-контроллера для аутентификации и авторизации

### Аутентификация пользователя
Для аутентификации пользователя отправьте POST-запрос на следующий адрес:
URL: http://localhost:8083/api/v1/auth/login
Тело запроса должно содержать имя пользователя (username) и пароль (password):
json { 
"username": "test1",
"password": "testtest"
} 

Ответ будет содержать идентификатор пользователя (user_id), JWT-токен (token), время выдачи токена (issued_at) и время его истечения (expires_at):
json { 
"user_id": 2,
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicm9sZSI6IlVTRVIiLCJpc3MiOiJwcm9zZWx5dGUiLCJleHAiOjE2OTUwMzg3MzcsImlhdCI6MTY5NTAzNTEzNywianRpIjoiNTVmODlhYWMtMjU5YS00OWEzLThmNTEtNzM4M2UwODc4YjNjIiwidXNlcm5hbWUiOiJ0ZXN0MSJ9.0D3P3RbRwDe4nob62fxXRpUqEdS8W_bbX06PT9_35a4",
"issued_at": "2023-09-18T11:05:37.851+00:00",
"expires_at": "2023-09-18T12:05:37.851+00:00"
} 

### Регистрация нового пользователя
Чтобы зарегистрировать нового пользователя, отправьте POST-запрос на следующий адрес:
URL: http://localhost:8083/api/v1/auth/register
Тело запроса должно содержать имя пользователя (username), пароль (password), имя (first_name) и фамилию (last_name):
json { 
"username": "test1",
"password": "testtest",
"first_name": "Vasya",
"last_name": "Pypkin"
} 

Ответ будет содержать информацию о созданном пользователе:
json { 
"id": 2,
"username": "test1",
"role": "USER",
"first_name": "Vasya",
"last_name": "Pypkin",
"enabled": true,
"created_at": "2023-09-18T11:49:52.756184",
"updated_at": "2023-09-18T11:49:52.756199" } 

### Получение информации о текущем пользователе
Для получения информации о текущем пользователе отправьте GET-запрос на следующий адрес с указанным JWT-токеном:
URL: http://localhost:8083/api/v1/auth/info
В заголовке запроса укажите авторизацию с типом "Bearer" и токеном:
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicm9sZSI6IlVTRVIiLCJpc3MiOiJwcm9zZWx5dGUiLCJleHAiOjE2OTUwMzg3MzcsImlhdCI6MTY5NTAzNTEzNywianRpIjoiNTVmODlhYWMtMjU5YS00OWEzLThmNTEtNzM4M2UwODc4YjNjIiwidXNlcm5hbWUiOiJ0ZXN0MSJ9.0D3P3RbRwDe4nob62fxXRpUqEdS8W_bbX06PT9_35a4
Ответ будет содержать информацию о текущем пользователе:
json {
"id": 2,
"username": "test1",
"role": "USER",
"first_name": "Vasya",
"last_name": "Pypkin",
"enabled": true
"created_at": "2023-09-18T11:49:52.756184",
"updated_at": "2023-09-18T11:49:52.756199"
}

