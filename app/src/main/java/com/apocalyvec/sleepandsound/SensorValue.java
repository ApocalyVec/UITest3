package com.apocalyvec.sleepandsound;

public class SensorValue {

    int time, value;

    public SensorValue() {
    }

    public SensorValue(int time, int value) {
        this.time = time;
        this.value = value;
    }

    public int getTime() {
        return time;
    }

    public int getValue() {
        return value;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
