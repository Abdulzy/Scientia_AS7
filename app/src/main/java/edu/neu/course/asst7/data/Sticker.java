package edu.neu.course.asst7.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;

@IgnoreExtraProperties
public class Sticker {
    public String name;
    public String location;
    public String token;
    public String localFilePath;

    public Sticker() {
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

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sticker sticker = (Sticker) o;
        return Objects.equals(name, sticker.name) && Objects.equals(location, sticker.location) && Objects.equals(token, sticker.token) && Objects.equals(localFilePath, sticker.localFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, token, localFilePath);
    }

    @Override
    public String toString() {
        return "Sticker{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", token='" + token + '\'' +
                ", localFile='" + localFilePath + '\'' +
                '}';
    }
}
