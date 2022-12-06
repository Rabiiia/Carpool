package dtos;

import entities.School;
import entities.User;

import java.util.ArrayList;
import java.util.List;

public class SchoolDTO {

    int id;
    String schoolName;
    String location;


    public SchoolDTO(School school) {
        if(school.getId() != null) this.id = school.getId();
        this.schoolName = school.getName();
        this.location = school.getLocation();
    }

    public SchoolDTO(String schoolName, String location) {
        this.schoolName = schoolName;
        this.location = location;
    }

}
