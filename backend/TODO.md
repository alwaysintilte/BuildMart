# 📋 BuildMart Project TODO List

## 1. База данных и Сущности (PostgreSQL & JPA)
- [x] User Entity: id, email, password_hash, role (CUSTOMER / ADMIN), created_at
- [x] Product Entity: id, title, description, price, rating (float), images (список URL мин. 3 шт), stock_quantity, created_at
- [x] Cart & CartItem Entities: привязка к пользователю, перечень товаров, количество
- [x] PromoCode Entity: id, code (например SAVE10), discount_percentage (10%), is_active
- [x] Foreign Keys: Настройка связей и каскадного удаления / soft-delete для товаров

## 2. Аутентификация и Авторизация (JWT & Security)
- [x] Хеширование паролей (BCrypt)
- [x] POST /api/auth/register (Регистрация)
- [x] POST /api/auth/login (Авторизация с выдачей JWT)
- [x] JWT Filter: передача и валидация токена в заголовке Authorization: Bearer <token>
- [x] Настройка ролевого доступа (RBAC):
    - CUSTOMER: просмотр каталога, поиск, фильтры, личная корзина, промокод
    - ADMIN: создание, редактирование и удаление товаров
- [ ] Настройка CORS-политики для работы с фронтендом

## 3. Каталог: Поиск, Фильтрация, Сортировка и Пагинация (на уровне БД)
- [x] GET /api/products с обработкой query-параметров:
    - page (default: 1) и limit
    - search (поиск по подстроке в title/description без учета регистра через ILIKE)
    - min_price и max_price
    - min_rating
    - sort_by (price или title)
    - order (asc или desc)
- [x] Форматирование ответа GET /api/products в точный JSON (структура: items, meta -> total_items, total_pages, current_page, limit)

## 4. Удаление товаров и Инвалидация Кэша
- [x] DELETE /api/products/:id (доступ строго для ADMIN)
- [ ] Обработка внешних ключей при удалении
- [x] Автоматический сброс кэша каталога при удалении товара

## 5. Кэширование (Redis)
- [x] Подключение Spring Data Redis и настройка @EnableCaching
- [x] Кэширование запросов GET /api/products (ключ на основе query-строки)
- [x] Настройка TTL кэша (60–120 секунд)
- [x] Инвалидация (очистка) кэша при создании, изменении или удалении товара

## 6. Корзина и Промокоды
- [x] GET /api/cart — получение корзины пользователя
- [x] POST /api/cart/items — добавление товара в корзину
- [x] PATCH /api/cart/items/:id — изменение количества
- [x] DELETE /api/cart/items/:id — удаление товара из корзины
- [x] POST /api/cart/apply-promo — валидация промокода SAVE10 и перерасчет стоимости со скидкой 10%

## 7. Контейнеризация (Docker)
- [ ] Dockerfile: Multi-stage build для сборки и запуска
- [ ] Dockerfile: Запуск процесса от непривилегированного пользователя (USER node / USER spring)
- [ ] docker-compose.yml: Сервисы app, postgres, redis
- [ ] Именованные volumes для сохранения данных PostgreSQL
- [ ] Изоляция переменных окружения в .env (и создание .env.example)

## 8. Облачная Инфраструктура и Деплой
- [ ] Развертывание PostgreSQL в облаке (Neon / Supabase / TiDB)
- [ ] Загрузка медиафайлов/картинок товаров в Cloudinary / Supabase Storage
- [ ] Деплой бэкенда из Dockerfile на Render / Koyeb
- [ ] Проверка CORS-запросов с клиентского приложения

## 9. Git Flow & Документация
- [x] Настройка веток Git Flow (main, develop, feature/*)
- [x] Соблюдение формата Conventional Commits (feat:, fix:, chore:, refactor:)
- [ ] Подключение Swagger / OpenAPI (springdoc-openapi-ui)
- [ ] Написание README.md (инструкция docker compose, переменные окружения, ссылки на деплой и Swagger)