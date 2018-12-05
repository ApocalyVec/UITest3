package com.apocalyvec.sleepandsound;

public class Hardware {

    public String timestamp;
    public String pid;

    public Hardware() {}

    public Hardware(String timestamp, String pid) {
        this.timestamp = timestamp;
        this.pid = pid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
