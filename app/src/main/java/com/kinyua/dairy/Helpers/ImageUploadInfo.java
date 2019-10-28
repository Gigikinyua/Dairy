package com.kinyua.dairy.Helpers;

public class ImageUploadInfo {
    public String cowName;
    public String cowTagId;
    public String cowOwner;
    public String cowDOB;

    public String CowimageURL;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String id, String owner, String dob, String url) {

        this.cowName = name;
        this.cowTagId = id;
        this.cowOwner = owner;
        this.cowDOB = dob;
        this.CowimageURL= url;
    }


    public String getCowName() {
        return cowName;
    }
    public String getCowId() {
        return cowTagId;
    }
    public String getCowOwner() {
        return cowOwner;
    }
    public String getCowDOB() {
        return cowDOB;
    }

    public String getImageURL() {
        return CowimageURL;
    }
}
