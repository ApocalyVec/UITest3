package com.apocalyvec.sleepandsound;

public class Hardware {

    public String timestamp;
    public String pid;
    public String status;

    public Hardware() {}

    public Hardware(String timestamp, String pid, String status) {
        this.timestamp = timestamp;
        this.pid = pid;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
