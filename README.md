# java-filmorate
Template repository for Filmorate project.        
![Database schema](https://github.com/OlegSharomov/java-filmorate/blob/add-friends-likes/images/QuickDBD-Free%20Diagram.png)        
Вы можете воспроизвести у себя эту базу данных, вставив текст из пояснений
на странице: https://app.quickdatabasediagrams.com/

Пояснения по базе данных:

### users  
*--*  
user_id PK bigint  
user_name varchar(255)  
user_login varchar(255)  
user_email varchar(255)  
user_birthday date    
  
#friendship_list - отношение многие ко многим  
#оба поля ссылаются как FK на поле user_id таблицы users  
### friendship_list
*--*  
user_id PK bigint FK >- users.user_id  
friend_id PK bigint FK >- users.user_id  
status varchar(63)  

### likes
*--*  
film_id PK bigint  FK >- films.film_id  
user_like PK bigint FK >- users.user_id  

### films
*--*  
film_id PK bigint  
film_name varchar(255)  
film_description varchar(200)  
film_release_date date  
film_duration int  
mpa_id int FK >- film_mpa.mpa_id  
rating real

#film_mpa - отношение один ко многим  
#вынесена в отдельную таблицу, что бы не было  
#возможности потери информации и для легкости дальнейшего исправления  
### film_mpa
*--*  
mpa_id PK int  
mpa_name varchar(5)  

#genre - отношение многие ко многим через таблиуц film_genre  
### genre
*--*  
genre_id PK int  
genre_name varchar(63)  

### film_genre
*--*  
film_id PK bigint FK >- films.film_id  
genre_id PK int FK >- genre.genre_id  
