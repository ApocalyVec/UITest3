package com.apocalyvec.sleepandsound;

public class Kids {
    private String kidName;
    private String age;
    private String image;

    public Kids() {}

    public Kids(String kidName, String age, String image) {
        this.kidName = kidName;
        this.age = age;
        this.image = image;
    }

    public String getkidName() {
        return kidName;
    }

    public void setName(String kidName) {
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
}
