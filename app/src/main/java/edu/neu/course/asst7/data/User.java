package edu.neu.course.asst7.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;

@IgnoreExtraProperties
public class User {

    public String username;
    public String token;
//    public SentStickersCount sticker1;
//    public SentStickersCount sticker2;
//    public SentStickersCount sticker3;
//    public SentStickersCount sticker4;

    public User() {
    }

//    , SentStickersCount sticker1, SentStickersCount sticker2, SentStickersCount sticker3,SentStickersCount sticker4
    public User(String username, String token) {
        this.username = username;
        this.token = token;
//        this.sticker1 = sticker1;
//        this.sticker2 = sticker2;
//        this.sticker3 = sticker3;
//        this.sticker4 = sticker4;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(token, user.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, token);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
