package com.apocalyvec.sleepandsound.behavior;

public class Hardware {

    public String Timestamp;

    public Hardware() {}

    public Hardware(String Timestamp) {
       this.Timestamp = Timestamp;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String Timestamp) {
        this.Timestamp = Timestamp;
    }
}
