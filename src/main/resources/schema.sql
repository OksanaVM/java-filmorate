
CREATE TABLE IF NOT EXISTS PUBLIC.genre (
    genre_id          int generated by default as identity primary key,
    genre_name        varchar(255) not null
    );

CREATE TABLE IF NOT EXISTS PUBLIC.mpa (
    mpa_id       int generated by default as identity primary key,
    mpa_name     varchar(6) not null
    );

CREATE TABLE IF NOT EXISTS PUBLIC.films (
    id           int generated by default as identity primary key,
    name         varchar(100) not null,
    description  varchar(255) not null,
    release_date date         not null,
    duration     int          not null,
    mpa_id      integer references mpa(mpa_id) on update cascade on delete cascade
    );

CREATE TABLE IF NOT EXISTS PUBLIC.users  (
    id       int generated by default as identity primary key,
    email    varchar(50) not null,
    login    varchar(20) not null,
    name     varchar(50),
    birthday date        not null
    );


CREATE TABLE IF NOT EXISTS PUBLIC.film_genre (
    film_id     integer references films(id) on update cascade on delete cascade,
    genre_id    integer references genre(genre_id) on update cascade on delete cascade,
        PRIMARY KEY (FILM_ID, GENRE_ID)
    );

CREATE TABLE IF NOT EXISTS PUBLIC.films_likes (
    film_id     integer references films(id) on update cascade on delete cascade,
    user_id     integer references users(id) on update cascade on delete cascade
    );

CREATE TABLE IF NOT EXISTS PUBLIC.friends (
    user_id   integer references users (id),
    friend_id integer references users (id),
    status    boolean
    );




