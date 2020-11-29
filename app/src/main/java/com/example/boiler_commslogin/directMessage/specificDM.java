package com.example.boiler_commslogin.directMessage;

public class specificDM {
    public static final int THEIR_DM = 0;
    public static final int YOUR_DM = 1;

    private String mName;
    private String mBody;
    private String mTime;
    private int mType;

    public specificDM(String name, String body, String time, int type) {
        this.mName = name;
        this.mBody = body;
        this.mTime = time;
        this.mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }
    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }
}
