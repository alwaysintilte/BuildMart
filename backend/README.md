# BuildMart Backend

REST API интернет-магазина строительных товаров.

Проект реализован на Spring Boot с использованием PostgreSQL для хранения данных, Redis для кэширования каталога и JWT для аутентификации и авторизации пользователей.

## Технологии

- Java 21
- Spring Boot 4.0.3
- Spring Web MVC
- Spring Security
- Spring Data JPA (Hibernate)
- PostgreSQL
- Redis
- Liquibase
- JWT
- Swagger / OpenAPI
- Docker / Docker Compose

### Облачная инфраструктура

- Render — хостинг REST API.
- Supabase PostgreSQL — облачная база данных.
- Upstash Redis — облачное кэширование.
- Supabase Storage — хранение изображений товаров.

## Функциональность API

- Регистрация и авторизация пользователей через JWT.
- Ролевой доступ CUSTOMER / ADMIN.
- Получение каталога товаров.
- Поиск, фильтрация, сортировка и пагинация товаров.
- Получение товара по ID.
- Получение товаров со скидкой.
- Удаление товаров администратором.
- Redis-кэширование каталога.
- Управление корзиной пользователя.
- Применение промокодов.
- Централизованная обработка HTTP-ошибок.

## Требования для локального запуска

Перед запуском необходимо установить:

- Docker Desktop
- Docker Compose
- Git

Java и Maven устанавливать необязательно, поскольку приложение собирается внутри Docker.

## Локальный запуск

### 1. Клонирование репозитория

```bash
git clone https://github.com/alwaysintilte/BuildMart.git
cd BuildMart/backend
```

### 2. Настройка переменных окружения

Создайте файл `.env` на основе `.env.example`.

Для Linux/macOS:

```bash
cp .env.example .env
```

Для Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

Заполните переменные окружения:

```env
DB_HOST=localhost
DB_PORT=5433
DB_NAME=buildmart
DB_USER=postgres
DB_PASSWORD=YOUR_LOCAL_DB_PASSWORD
DB_SSLMODE=disable

REDIS_HOST=localhost
REDIS_PORT=6380
REDIS_PASSWORD=
REDIS_SSL=false

JWT_SIGNING_KEY=YOUR_JWT_SECRET
JWT_EXPIRATION=3600000

SERVER_PORT=8080
```

При запуске через Docker Compose приложение автоматически использует внутренние адреса PostgreSQL (`postgres:5432`) и Redis (`redis:6379`).

### 3. Запуск контейнеров

Из каталога с `docker-compose.yml` выполните:

```bash
docker compose up --build -d
```

Docker Compose запускает три сервиса:

- `app` — Spring Boot.
- `postgres` — PostgreSQL 17.
- `redis` — Redis.

### 4. Проверка контейнеров

```bash
docker compose ps
```

Для просмотра логов приложения:

```bash
docker compose logs -f
```

### 5. Доступ к приложению

После запуска REST API доступен по адресу:

http://localhost:8080

Swagger UI:

http://localhost:8080/swagger-ui/index.html

OpenAPI JSON:

http://localhost:8080/v3/api-docs

### 6. Остановка приложения

```bash
docker compose down
```

Именованные Docker volumes сохраняются, поэтому данные PostgreSQL не удаляются при обычной остановке.

## Переменные окружения

| Переменная | Назначение |
|---|---|
| `DB_HOST` | Адрес PostgreSQL |
| `DB_PORT` | Порт PostgreSQL |
| `DB_NAME` | Название базы данных |
| `DB_USER` | Пользователь PostgreSQL |
| `DB_PASSWORD` | Пароль PostgreSQL |
| `DB_SSLMODE` | Режим SSL для PostgreSQL |
| `REDIS_HOST` | Адрес Redis |
| `REDIS_PORT` | Порт Redis |
| `REDIS_PASSWORD` | Пароль Redis |
| `REDIS_SSL` | Использование TLS для Redis |
| `JWT_SIGNING_KEY` | Секретный ключ подписи JWT |
| `JWT_EXPIRATION` | Время действия JWT в миллисекундах |
| `SERVER_PORT` | Порт Spring Boot |

Настоящие пароли и JWT-ключ должны храниться в `.env` или переменных окружения хостинга и не должны попадать в Git.

## REST API

### Аутентификация

| Метод | Эндпоинт | Описание | Доступ |
|---|---|---|---|
| POST | `/api/auth/register` | Регистрация пользователя | Публичный |
| POST | `/api/auth/login` | Авторизация и получение JWT | Публичный |

Для защищённых эндпоинтов используется заголовок:

```http
Authorization: Bearer <JWT_TOKEN>
```

### Каталог товаров

| Метод | Эндпоинт | Описание | Доступ |
|---|---|---|---|
| GET | `/api/products/` | Каталог с фильтрацией и пагинацией | Публичный |
| GET | `/api/products/id/{id}` | Получение товара по ID | Публичный |
| GET | `/api/products/discount` | Товары со скидкой | Публичный |
| DELETE | `/api/products/{id}` | Удаление товара | ADMIN |

Параметры каталога:

- `page` — номер страницы.
- `limit` — количество товаров.
- `search` — поиск по названию или описанию.
- `min_price`, `max_price` — диапазон цен.
- `min_rating` — минимальный рейтинг.
- `sort_by` — поле сортировки.
- `order` — направление сортировки.

### Корзина

| Метод | Эндпоинт | Описание | Доступ |
|---|---|---|---|
| GET | `/api/cart/` | Получение корзины | Авторизованный пользователь |
| POST | `/api/cart/items` | Добавление товара | Авторизованный пользователь |
| PATCH | `/api/cart/items/{id}` | Изменение количества | Авторизованный пользователь |
| DELETE | `/api/cart/items/{id}` | Удаление товара | Авторизованный пользователь |
| POST | `/api/cart/apply-promo` | Применение промокода | Авторизованный пользователь |

## Кэширование

Для кэширования используется Redis.

Результаты запросов каталога сохраняются с учётом параметров фильтрации, сортировки и пагинации.

При изменении или удалении товаров соответствующие записи кэша инвалидируются.

Локально Redis запускается через Docker Compose.

## Миграции базы данных

Для управления схемой PostgreSQL используется Liquibase.

Файлы миграций расположены в:

```text
src/main/resources/db/changelog/
```

При запуске Spring Boot Liquibase автоматически применяет миграции, которые ещё не были выполнены.

## Развёртывание

Бэкенд развёртывается на Koyeb из Dockerfile.

Для облачного окружения используются:

- PostgreSQL в Supabase.
- Redis в Upstash.
- Supabase Storage для изображений товаров.

Параметры подключения к облачным сервисам задаются через Environment Variables в Koyeb.
