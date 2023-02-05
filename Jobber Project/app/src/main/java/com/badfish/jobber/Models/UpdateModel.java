package com.badfish.jobber.Models;

public class UpdateModel {
    String versionName, versionCode, url;

    public UpdateModel(String versionName, String versionCode, String url) {
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.url = url;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public String getUrl() {
        return url;
    }

    public UpdateModel() {
    }
}
