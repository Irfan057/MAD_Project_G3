package com.example.madprojectg3;

public class Project {
    private String name;
    private String description;
    private String websiteURL;

    private String imageURL;

    public Project(String name, String description, String websiteUrl, String imageURL) {
        this.name = name;
        this.description = description;
        this.websiteURL = websiteUrl;
        this.imageURL = imageURL;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsiteUrl() {
        return websiteURL;
    }

    public String getimageURL() {
        return imageURL;
    }

}
