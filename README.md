### Репозиторий с картами работает на SessionFactory, Session и тд 

### Репозиторий продуктов построен на JpaRepository

### И в Product, и в DiscountCard есть ассоциация @ManyToOne

### Дабы не ломать архитектуру на интерфейсах, JpaRepository<Product, Long> был прикручен через адаптер, реализующий ProductRepository

### Пришлось перелопатить проект и поменять Integer на Long, т.к. в HibernateCardRepository.findAll вылетал ClassCastException

### С методами с пагинацией задолбался :)
 