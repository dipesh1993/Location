package com.example.imagenew1;

public class SchoolData {

    private Integer s_id;
    private String school_name;
    private String place;

    public SchoolData(Integer s_id,String school_name, String place) {
        this.s_id = s_id;
        this.school_name = school_name;
        this.place = place;
    }



    public Integer getS_id() {
        return s_id;
    }


    public String getSchool_name() {
        return school_name;
    }

    public String getPlaceName() {
        return place;
    }

}