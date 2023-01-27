package com.badfish.jobber.Models;

public class User {
    public static User currentUser = new User();

    public String firstName, lastName, email, locked, userID,token,bio, profilePicture;

    public User(){


    }
    public User(String firstName, String lastName, String email, String locked, String userID, String token,String bio, String profilePicture){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.locked = locked;
        this.userID = userID;
        this.token=token;
        this.bio=bio;
        this.profilePicture=profilePicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public String getToken() {
        return token;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getLocked() {
        return locked;
    }

    public String getUserID() {
        return userID;
    }
}
