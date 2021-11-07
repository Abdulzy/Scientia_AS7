package edu.neu.course.asst7.data;

import java.util.Objects;

public class ReceivedHistory {
    public String userName;
    public int stickerId;

    public ReceivedHistory() {
    }

    public ReceivedHistory(String userName, int stickerId) {
        this.userName = userName;
        this.stickerId = stickerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStickerId() {
        return stickerId;
    }

    public void setStickerId(int stickerId) {
        this.stickerId = stickerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceivedHistory that = (ReceivedHistory) o;
        return stickerId == that.stickerId && Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, stickerId);
    }

    @Override
    public String toString() {
        return "ReceivedHistory{" +
                "userName='" + userName + '\'' +
                ", stickerId=" + stickerId +
                '}';
    }
}
