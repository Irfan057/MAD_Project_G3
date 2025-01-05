package com.example.madprojectg3;

public class DonationProject {
    private String name;
    private String shortdesc;
    private String link;

    public DonationProject() {}

    public DonationProject(String name, String shortdesc, String link) {
        this.name = name;
        this.shortdesc = shortdesc;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public String getLink() {
        return link;
    }
}
