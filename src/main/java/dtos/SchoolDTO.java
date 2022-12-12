package dtos;

import entities.School;

public class SchoolDTO {
    int id;
    String name;
    AddressDTO address;

    public SchoolDTO(String name, String address, int zipcode) {
        this.name = name;
        this.address = new AddressDTO(address, zipcode);
    }

    public SchoolDTO(School school) {
        if (school.getId() != null) this.id = school.getId();
        this.name = school.getName();
        this.address = new AddressDTO(school.getStreet(), school.getZipcode());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressDTO getAddress() {
        return address;
    }
}
