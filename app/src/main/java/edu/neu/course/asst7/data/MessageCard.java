package edu.neu.course.asst7.data;

public class MessageCard {
    private final String sender;
    private final String time;
    private final int StickerSent;

    public MessageCard(int StickerSent, String sender,String time) {
        this.StickerSent = StickerSent;
        this.sender = sender;
        this.time = time;
    }

    public int getStickerSent() {
        return StickerSent;
    }

    public String getSender() {
        return sender;
    }

    public String getTime() {
        return time;
    }

}
