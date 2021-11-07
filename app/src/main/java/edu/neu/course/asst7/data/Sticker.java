package edu.neu.course.asst7.data;

import java.util.Objects;

public class Sticker {
    public int id;
    public String name;
    public String location;
    public String token;

    public Sticker() {
    }

    public Sticker(int id, String name, String location, String token) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        Sticker sticker = (Sticker) o;
        return id == sticker.id && Objects.equals(name, sticker.name) && Objects.equals(location, sticker.location) && Objects.equals(token, sticker.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, token);
    }

    @Override
    public String toString() {
        return "Sticker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
