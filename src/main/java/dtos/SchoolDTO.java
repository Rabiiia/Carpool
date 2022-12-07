package dtos;

import entities.School;
import entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolDTO schoolDTO = (SchoolDTO) o;
        return id == schoolDTO.id && schoolName.equals(schoolDTO.schoolName) && location.equals(schoolDTO.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, schoolName, location);
    }
}
