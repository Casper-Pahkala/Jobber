package com.badfish.jobber.Models;

public class EmployerModel {
    Double lat,lng;
    String jobDescription, employerName,EmployerUID,jobToken,id,jobAddress,date,time, employerProfilePicture;

    public EmployerModel(Double lat, Double lng,String employerName,String jobDescription,String EmployerUID,String jobToken,String id,String jobAddress
            ,String date, String time, String employerProfilePicture) {
        this.lat = lat;
        this.lng = lng;
        this.employerName=employerName;
        this.jobDescription=jobDescription;
        this.EmployerUID=EmployerUID;
        this.jobToken=jobToken;
        this.id=id;
        this.jobAddress=jobAddress;
        this.date=date;
        this.time=time;
        this.employerProfilePicture=employerProfilePicture;
    }

    public String getEmployerProfilePicture() {
        return employerProfilePicture;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getEmployerUID() {
        return EmployerUID;
    }

    public String getJobToken() {
        return jobToken;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }



    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public EmployerModel() {
    }
}
