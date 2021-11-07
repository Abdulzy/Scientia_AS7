package edu.neu.course.asst7.data;

import java.util.Objects;

public class ReceivedHistory {
    public String userName;
    public int stickerId;
    public String sender;
    public String timeSent;

    public ReceivedHistory() {
    }

    public ReceivedHistory(String userName, int stickerId, String sender, String timeSent) {
        this.userName = userName;
        this.stickerId = stickerId;
        this.sender = sender;
        this.timeSent = timeSent;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceivedHistory that = (ReceivedHistory) o;
        return stickerId == that.stickerId && Objects.equals(userName, that.userName) && Objects.equals(sender, that.sender) && Objects.equals(timeSent, that.timeSent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, stickerId, sender, timeSent);
    }

    @Override
    public String toString() {
        return "ReceivedHistory{" +
                "userName='" + userName + '\'' +
                ", stickerId=" + stickerId +
                ", sender='" + sender + '\'' +
                ", timeSent='" + timeSent + '\'' +
                '}';
    }
}
