package com.badfish.jobber.Models;

public class WorkPlaceModel {
    Double lat,lng;
    String distance, jobDescription,jobToken,employerName,id,workerAddress, employerUID,jobAddress, date, time,workerToken,workerUserID,workerName, employerProfilePicture
            ,workerProfilePicture,status;



    public WorkPlaceModel(String distance, String employerName, String jobDescription, String id, String workerAddress,String workerUserID,String workerToken
            , String employerUID, String jobAddress
    , String jobToken, String date, String time,String workerName, String employerProfilePicture, String workerProfilePicture,String status) {
        this.employerName = employerName;
        this.distance = distance;
        this.jobDescription =jobDescription;
        this.id=id;
        this.workerAddress=workerAddress;
        this.workerToken=workerToken;
        this.workerUserID=workerUserID;
        this.employerUID=employerUID;
        this.jobAddress=jobAddress;
        this.jobToken=jobToken;
        this.date=date;
        this.time=time;
        this.workerName=workerName;
        this.employerProfilePicture=employerProfilePicture;
        this.workerProfilePicture=workerProfilePicture;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public String getEmployerProfilePicture() {
        return employerProfilePicture;
    }

    public String getWorkerProfilePicture() {
        return workerProfilePicture;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getDistance() {
        return distance;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getWorkerToken() {
        return workerToken;
    }

    public String getWorkerUserID() {
        return workerUserID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public String getJobToken() {
        return jobToken;
    }

    public String getWorkerAddress() {
        return workerAddress;
    }

    public String getEmployerUID() {
        return employerUID;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public String getEmployerName() {
        return employerName;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public WorkPlaceModel() {
    }

}
