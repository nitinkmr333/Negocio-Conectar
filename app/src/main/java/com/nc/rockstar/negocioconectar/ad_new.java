package com.nc.rockstar.negocioconectar;

public class ad_new {


    public String userid;
    public String username;
    public String location;
    public String contact;

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public String getAdAdditionalDescription() {
        return adAdditionalDescription;
    }

    public String getAdtitle() {
        return adtitle;
    }

    public String getCompany() {
        return company;
    }

    public String adDescription;
    public String adAdditionalDescription;
    public String adtitle;
    public String company;

    public ad_new(){

    }

    public ad_new(String uid, String username, String location, String contact, String description, String addDescription, String title, String company) {
        this.userid = uid;
        this.username = username;
        this.location = location;
        this.contact = contact;
        this.adDescription = description;
        this.adAdditionalDescription = addDescription;
        this.adtitle = title;
        this.company = company;
    }
}
