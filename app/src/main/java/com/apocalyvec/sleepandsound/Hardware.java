package com.apocalyvec.sleepandsound;

public class Hardware {

    public String Timestamp;
    public String pid;

    public Hardware() {}

    public Hardware(String Timestamp, String pid) {
        this.Timestamp = Timestamp;
        this.pid = pid;

    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String Timestamp) {
        this.Timestamp = Timestamp;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
