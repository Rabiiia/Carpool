package dtos;

import entities.School;
import entities.User;

public class SchoolDTO {

    int id;
    String schoolName;
    String location;


    public SchoolDTO(School school) {
        if(school.getId() != null)
            this.id = school.getId();
        this.schoolName = school.getName();
        this.location = school.getLocation();
    }
}
