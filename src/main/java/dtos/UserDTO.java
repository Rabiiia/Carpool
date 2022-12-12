package dtos;

import entities.School;
import entities.User;

public class UserDTO {
    int id;
    String name;
    String username;
    int phone;
    AddressDTO address;
    String role;
    String password;
    int schoolId;
    InnerSchoolDTO school;

    public UserDTO(User user) {
        if(user.getId() != null) this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.address = new AddressDTO(user.getAddress(), user.getZipcode());
        this.role = user.getRole();
        this.school = new InnerSchoolDTO(user.getSchool());
    }

    public UserDTO(String username, String password, String street, int phone, int zipcode, String name, String role, int schoolId) {
        this.username = username;
        this.password = password;
        this.address = new AddressDTO(street, zipcode);
        this.phone = phone;
        this.name = name;
        this.role = role;
        this.schoolId = schoolId;
    }

    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public Object getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public InnerSchoolDTO getSchool() {
        return school;
    }

    public class InnerSchoolDTO {
        int id;
        String name;
        AddressDTO address;

        public InnerSchoolDTO(String name, String street, int zipcode) {
            this.name = name;
            this.address = new AddressDTO(street, zipcode);
        }

        public InnerSchoolDTO(School school) {
            if (school.getId() != null) this.id = school.getId();
            this.name = school.getName();
            this.address = new AddressDTO(school.getStreet(), school.getZipcode());
        }
    }
}
