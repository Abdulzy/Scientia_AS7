package edu.neu.course.asst7.data;

import java.util.Objects;

public class SentStickersCount {
    //    public String username;
    public int stickerId;
    public int count;

    public SentStickersCount() {
    }

    public SentStickersCount(int stickerId) {

        this.stickerId = stickerId;
        this.count = 0;
    }

    public SentStickersCount(String username, int stickerId) {
        this(stickerId);
    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

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

    public int incrementcount() {
        this.count = this.count + 1;
        this.setCount(this.count);
        return this.count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentStickersCount that = (SentStickersCount) o;
//        return stickerId == that.stickerId && count == that.count && Objects.equals(username, that.username);
        return stickerId == that.stickerId && count == that.count;

    }

    @Override
    public int hashCode() {
//        return Objects.hash(username, stickerId, count);
        return Objects.hash(stickerId, count);
    }

    @Override
    public String toString() {
        return "SentStickers{" +
//                "username='" + username + '\'' +
                ", stickerId=" + stickerId +
                ", count=" + count +
                '}';
    }
}
