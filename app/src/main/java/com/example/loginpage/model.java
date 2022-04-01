package com.example.loginpage;

public  class model{
    private String imageUrl;
    public model(){

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public model(String imageUrl){
        this.imageUrl=imageUrl;
    }

}