# Filmorate
Template repository for Filmorate project.        

Фильмов много — и с каждым годом становится всё больше. Чем их больше, тем больше разных оценок. Чем больше оценок, тем сложнее сделать выбор. Однако не время сдаваться! 

У вас есть возможность воспользоваться мини социальной сетью, которая поможет выбрать кино на основе того, какие фильмы вы и ваши друзья смотрите, какие оценки им ставите, а также возвращать лучшие фильмы, рекомендованные к просмотру. Теперь ни вам, ни вашим друзьям не придётся долго размышлять, что посмотреть вечером.

### *Функциональность:*
Здесь вы можете заводить друзей, просматривать популярные фильмы, выбирать фильмы по жанрам и ассоциациям кинокомпаний, по возрастным ограничениям, ставить фильмам лайки и др.

### *Структура:*
REST приложение "Кинопоиск для своих" со встроенной базой данных H2. Взаимодействие с БД происходит благодаря JdbcTempalte.

### *Запуск:*
Для запуска запустите файл run.bat. Для остановки приложения в открывшейся консоли нажмите ctrl=c / cmd=c

### *Стек:*
Java 11, Spring Boot, JDBC, H2, Maven, JUnit, Lombok, Slf4j.

![Database schema](https://github.com/OlegSharomov/filmorate/blob/main/images/QuickDBD-Free%20Diagram.png)        
