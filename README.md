Вроде работает.

Сначала запустить DatabaseInitializer из пакета util/jdbc (!! осторожно DROP DATABASE IF EXISTS products;!!)

Для запуска приложения юзать CheckRunner.

Отдельно хочу обратить внимание на ReusableConnection из util/jdbc. Вот так красиво kotlin умеет делегировать. 

Совсем забыл, в util/jdbc/Props.kt прописать свои данные для sql (user и password)
