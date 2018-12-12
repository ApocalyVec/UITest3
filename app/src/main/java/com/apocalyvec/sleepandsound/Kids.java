package com.apocalyvec.sleepandsound;

public class Kids {
    private String kidName;
    private String age;
    private String image;
    private String associatedPID;

    public Kids() {}

    public Kids(String kidName, String age, String image, String associatedPID) {
        this.kidName = kidName;
        this.age = age;
        this.image = image;
        this.associatedPID = associatedPID;
    }

    public String getKidName() {
        return kidName;
    }

    public void setKidName(String kidName) {
        this.kidName = kidName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAssociatedPID() {
        return associatedPID;
    }

    public void setAssociatedPID(String associatedPID) {
        this.associatedPID = associatedPID;
    }
}
