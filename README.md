При развертывании приложения ContextListener инициализирует БД, инициализирует AppContextHolder, в котором лежит спринговый контехт.

Т.к. сервлеты создаются контейнером, зависимости сервлетов приходится получать костылями через context.getBean(..)