package com.example.helplah;


public class ListRequest {
    private String title;
    private String description;
    private String location;
    private String bestby;
    private Boolean urgent;
    private String requesterID;

    public ListRequest(String title, String description, String requesterID) {
        this.title = title;
        this.description = description;
        this.requesterID = requesterID;
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

    public String getHead() {
        return title;
    }

    public String getRequesterID() {
        return requesterID;
    }
}

