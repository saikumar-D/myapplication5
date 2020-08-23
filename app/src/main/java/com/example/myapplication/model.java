package com.example.myapplication;

public class model {
String image,name,status;

    public model() {
    }

    public model(String image, String name, String status) {
        this.image = image;
        this.name = name;
        this.status = status;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
