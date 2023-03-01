DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS films_likes;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS mpa;
DROP TABLE IF EXISTS mpa_films;
DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS films;

CREATE TABLE IF NOT EXISTS users  (
    id       int generated by default as identity primary key,
    email    varchar(50) not null,
    login    varchar(20) not null,
    name     varchar(50),
    birthday date        not null
    );

CREATE TABLE IF NOT EXISTS friends (
    user_id   integer references users (id),
    friend_id integer references users (id),
    status    boolean
    );

CREATE TABLE IF NOT EXISTS films (
    id           int generated by default as identity primary key,
    name         varchar(100) not null,
    description  varchar(200) not null,
    release_date date         not null,
    duration     int          not null
    );

CREATE TABLE IF NOT EXISTS genre (
        genre_id          int generated by default as identity primary key,
        genre_name        varchar(255) not null
    );

CREATE TABLE IF NOT EXISTS film_genre (
    film_id     integer references films(id) ON DELETE CASCADE,
    genre_id    integer references genre(genre_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS mpa (
    mpa_id       int generated by default as identity primary key,
    mpa_name     varchar(6) not null
    );
CREATE TABLE IF NOT EXISTS mpa_films (
    film_id     integer references films(id) ON DELETE CASCADE,
    mpa_id      integer references mpa(mpa_id)
    );

CREATE TABLE IF NOT EXISTS films_likes (
    film_id     integer references films(id) ON DELETE CASCADE,
    user_id     integer references users(id) ON DELETE CASCADE
    );


