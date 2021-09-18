package com.example.bloodonrequest.model;

public class RequestBlood {
    private String _id;
    private String fullName;
    private String bloodGroup;
    private String bloodAmount;
    private String contact;
    private String location;
    private String dateRequest;
    private String reason;


    public RequestBlood(String fullName, String bloodGroup, String bloodAmount, String contact, String location, String dateRequest, String reason) {
        this.fullName = fullName;
        this.bloodGroup = bloodGroup;
        this.bloodAmount = bloodAmount;
        this.contact = contact;
        this.location = location;
        this.dateRequest = dateRequest;
        this.reason = reason;
    }

    public RequestBlood(String _id, String fullName, String bloodGroup, String bloodAmount, String contact, String location, String dateRequest, String reason) {
        this._id = _id;
        this.fullName = fullName;
        this.bloodGroup = bloodGroup;
        this.bloodAmount = bloodAmount;
        this.contact = contact;
        this.location = location;
        this.dateRequest = dateRequest;
        this.reason = reason;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBloodAmount() {
        return bloodAmount;
    }

    public void setBloodAmount(String bloodAmount) {
        this.bloodAmount = bloodAmount;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(String dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}
