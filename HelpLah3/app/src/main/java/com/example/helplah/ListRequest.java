package com.example.helplah;


public class ListRequest {
    private String title;
    private String description;
    private String location;
    private String bestby;
    private Boolean urgent;
    private Integer requesterID;

    public ListRequest(String title, String description, String location, String bestby, Integer requesterID) {
        this.title = title;
        this.description = description;
        this.requesterID = requesterID;
        this.bestby = bestby;
        this.location = location;
    }

    public ListRequest(){
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBestby(String bestby) {
        this.bestby = bestby;
    }

    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getBestby() {
        return bestby;
    }

    public Integer getRequesterID() {
        return requesterID;
    }
}

