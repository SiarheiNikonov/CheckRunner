Входные данные типа "id,title,price,count" не очень лепились с моим кодом, пришлось костылить.

Файл с входными данными лежит в resources (order.txt), 19 валидных позиций, 21 некорректных. Запускать надо RegexCheckRunner.

Валидные строки парсятся в объекты, но их основе выводится чек в cash receipt.txt. Некорректные строки выводятся в 
resources/invalidData.txt. 

Регулярка проверяет сразу всю строку. Весь экшн происходит в RegexProductRepositoryImpl.