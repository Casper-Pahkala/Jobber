package com.badfish.jobber.Models;

public class WorkerInfoModel {
    String name,id,jobDescription,workerUID,employerUID;



    public WorkerInfoModel(String name, String id, String työnkuva, String workerUID, String employerUID) {
        this.name = name;
        this.id = id;
        this.jobDescription = jobDescription;
        this.workerUID=workerUID;
        this.employerUID=employerUID;

    }

    public String getWorkerUID() {
        return workerUID;
    }

    public String getEmployerUID() {
        return employerUID;
    }

    public WorkerInfoModel() {
    }

    public String getName() {
        return name;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getId() {
        return id;
    }

}
