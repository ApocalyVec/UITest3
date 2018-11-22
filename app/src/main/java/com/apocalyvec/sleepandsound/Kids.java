package com.apocalyvec.sleepandsound;

public class Kids {
    private String name;
    private String age;

    public Kids() {

    }

    public Kids(String firstName, String lastName, String age, String image) {
        this.name = firstName;
        this.age = age;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    private String image;
}
