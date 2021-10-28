package com.example.loginpage;

public class model {
    String purl,location;
    model(){

    }

    public model(String purl, String location) {
        this.purl = purl;
        this.location = location;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
