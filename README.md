
swagger - http://localhost:8080/swagger-ui/index.html

При старте сущности пересоздаются в БД автоматически, чтобы сохранять бд - нужно поменять в application.properties spring.jpa.hibernate.ddl-auto=create на spring.jpa.hibernate.ddl-auto=update