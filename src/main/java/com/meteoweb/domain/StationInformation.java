package com.meteoweb.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "StationInformation")
public class StationInformation {

    @Override
    public String toString() {
        return "StationInformation{" +
                "id='" + id + '\'' +
                ", longitude='" + longitude + '\'' +
                ", date='" + date + '\'' +
                ", city='" + city + '\'' +
                ", latitude='" + latitude + '\'' +
                ", elevation='" + elevation + '\'' +
                ", tmax='" + tmax + '\'' +
                ", tmin='" + tmin + '\'' +
                ", tavg='" + tavg + '\'' +
                ", perception='" + perception + '\'' +
                ", tobs='" + tobs + '\'' +
                ", snwd='" + snwd + '\'' +
                ", snow='" + snow + '\'' +
                '}';
    }

    @Id
    String id;

    String longitude ;

    String date;

    String city;

    String latitude ;

    String elevation ;

    String tmax ;

    String tmin ;

    String tavg ;

    String perception ;

    String tobs ;

    String snwd ;

    String snow ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getTmax() {
        return tmax;
    }

    public void setTmax(String tmax) {
        this.tmax = tmax;
    }

    public String getTmin() {
        return tmin;
    }

    public void setTmin(String tmin) {
        this.tmin = tmin;
    }

    public String getTavg() {
        return tavg;
    }

    public void setTavg(String tavg) {
        this.tavg = tavg;
    }

    public String getPerception() {
        return perception;
    }

    public void setPerception(String perception) {
        this.perception = perception;
    }

    public String getTobs() {
        return tobs;
    }

    public void setTobs(String tobs) {
        this.tobs = tobs;
    }

    public String getSnwd() {
        return snwd;
    }

    public void setSnwd(String snwd) {
        this.snwd = snwd;
    }

    public String getSnow() {
        return snow;
    }

    public void setSnow(String snow) {
        this.snow = snow;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}