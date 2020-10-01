package com.example.imagenew1;

public class Visit {

    private String date;
    private Integer roaster_id;
    private String place_name;
    private String place_village;

    public Visit(Integer roaster_id, String date, String place_name, String place_village) {
        this.roaster_id = roaster_id;
        this.date = date;
        this.place_name = place_name;
        this.place_village = place_village;
    }



    public Integer getRoster() {
        return roaster_id;
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