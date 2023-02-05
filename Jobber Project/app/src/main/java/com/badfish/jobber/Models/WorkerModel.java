package com.badfish.jobber.Models;

public class WorkerModel {
    Double lat,lng;
    String address, jobDescriptions,token, userid,distance,name, workerProfilePicture;



    public WorkerModel(Double lat, Double lng, String distance, String token, String userid,String jobDescriptions,String address,String name, String workerProfilePicture) {
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.token=token;
        this.userid=userid;
        this.jobDescriptions=jobDescriptions;
        this.address=address;
        this.name=name;
        this.workerProfilePicture=workerProfilePicture;
    }

    public String getWorkerProfilePicture() {
        return workerProfilePicture;
    }

    public WorkerModel() {
    }

    public String getName() {
        return name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getAddress() {
        return address;
    }

    public String getJobDescriptions() {
        return jobDescriptions;
    }

    public String getToken() {
        return token;
    }

    public String getUserid() {
        return userid;
    }

    public String getDistance() {
        return distance;
    }
}
