Класс ProductGenerator имеет статический метод, который возвраащет список сгенеренных продуктов.

Класс DataFiller пишет сгенеренные данные в файл в виде json в файлы. Имена файлов заданы в Constants.

Метод main класса CheckRunner принимает данные в аргументах, создаёт мапу с товаром и его кол-ом.
!!! Сейчас осенило, что тестеры могут запихнуть в аргументы одинаковый id товара (1-2 2-4 1-5), тогда результат
будет неверным.
Нужно либо обновлять кол-во в мапе, либо юзать LinkedList<Pair<Product, Integer>>, если одинаковые товары не нужно
объединять в одну позицию!!!

Статический метод в CheckReceiptCalculator получает на вход мапу и скидочную карту, на их основании отдаёт лист строк 
с окончательным чеком. Можно было возвращать String с переносами, но мне так больше нравится.

Полученный лист отправляется на печать в CheckReceiptPrinter, который в конструкторе получает PrintWriter (для печати в
консоль или файл) 
