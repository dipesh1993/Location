package com.example.imagenew1;

public class Reporter {

    private String date;
    private Integer roaster;
    private String place_name;
    private String place_village;

    public Reporter( Integer roaster,String date, String place_name, String place_village) {

        this.date = date;
        this.roaster = roaster;
        this.place_name = place_name;
        this.place_village = place_village;
    }


    public Integer getRoasterId() {
        return roaster;
    }

    public String getDate() {
        return date;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getPlace_village() {
        return place_village;
    }

}