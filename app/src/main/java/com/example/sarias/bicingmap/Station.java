package com.example.sarias.bicingmap;

/**
 * Created by Sarias on 04/02/2018.
 */

public class Station {
    private int idNum;
    private String type;
    private double latitude;
    private double longitude;
    private String streetName;
    private int streetNumber;
    private int altitude;
    private int slots;
    private int bikes;
    private boolean status;

    public int getIdNum() {
        return idNum;
    }

    public void setIdNum(int id) {
        this.idNum = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getBikes() {
        return bikes;
    }

    public void setBikes(int bikes) {
        this.bikes = bikes;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
