package dtos;

import entities.School;

import java.util.Objects;

public class SchoolDTO {
    int id;
    String name;
    AddressDTO location;

    public SchoolDTO(String name, String address, int zipcode) {
        this.name = name;
        this.location = new AddressDTO(address, zipcode);
    }

    public SchoolDTO(School school) {
        if (school.getId() != null) this.id = school.getId();
        this.name = school.getName();
        this.location = new AddressDTO(school.getAddress(), school.getZipcode());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressDTO getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolDTO schoolDTO = (SchoolDTO) o;
        return id == schoolDTO.id && Objects.equals(name, schoolDTO.name) && Objects.equals(location, schoolDTO.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }
}
