package edu.neu.course.asst7.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;

@IgnoreExtraProperties
public class Sticker {
    public String name;
    public String location;
    public String token;
    public String localFile;

    public Sticker() {
    }

    public Sticker(String name, String location, String token) {
        this.name = name;
        this.location = location;
        this.token = token;
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

    public String getLocalFile() {
        return localFile;
    }

    public void setLocalFile(String localFile) {
        this.localFile = localFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sticker sticker = (Sticker) o;
        return Objects.equals(name, sticker.name) && Objects.equals(location, sticker.location) && Objects.equals(token, sticker.token) && Objects.equals(localFile, sticker.localFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, token, localFile);
    }

    @Override
    public String toString() {
        return "Sticker{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", token='" + token + '\'' +
                ", localFile='" + localFile + '\'' +
                '}';
    }
}
