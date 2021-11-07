package edu.neu.course.asst7.data;

import java.util.Objects;

public class SentStickersCount {
    public String username;
    public int stickerId;
    public int count;

    public SentStickersCount() {
    }

    public SentStickersCount(String username, int stickerId, int count) {
        this.username = username;
        this.stickerId = stickerId;
        this.count = count;
    }

    public SentStickersCount(String username, int stickerId) {
        this(username, stickerId, 0);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStickerId() {
        return stickerId;
    }

    public void setStickerId(int stickerId) {
        this.stickerId = stickerId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentStickersCount that = (SentStickersCount) o;
        return stickerId == that.stickerId && count == that.count && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, stickerId, count);
    }

    @Override
    public String toString() {
        return "SentStickers{" +
                "username='" + username + '\'' +
                ", stickerId=" + stickerId +
                ", count=" + count +
                '}';
    }
}
