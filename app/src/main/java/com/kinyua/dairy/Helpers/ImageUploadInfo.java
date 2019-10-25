package com.kinyua.dairy.Helpers;

public class ImageUploadInfo {
    public String cowName;
    public String cowiden;
    public String cowOwner;
    public String cowDOB;

    public String imageURL;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String id, String owner, String dob, String url) {

        this.cowName = name;
        this.cowiden = id;
        this.cowOwner = owner;
        this.cowDOB = dob;
        this.imageURL= url;
    }


    public String getCowName() {
        return cowName;
    }
    public String getCowId() {
        return cowiden;
    }
    public String getCowOwner() {
        return cowOwner;
    }
    public String getCowDOB() {
        return cowDOB;
    }

    public String getImageURL() {
        return imageURL;
    }
}
