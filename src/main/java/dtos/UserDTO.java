package dtos;

import entities.School;
import entities.User;

public class UserDTO {
    int id;
    String name;
    String username;
    int phone;
    AddressDTO location;
    String role;
    InnerSchoolDTO school;

    public UserDTO(User user) {
        if (user.getId() != null) this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.location = new AddressDTO(user.getAddress(), user.getZipcode());
        this.role = user.getRole();
        this.school = new InnerSchoolDTO(user.getSchool());
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

    public AddressDTO getLocation() {
        return location;
    }

    public InnerSchoolDTO getSchool() {
        return school;
    }

    public class InnerSchoolDTO {
        int id;
        String name;
        AddressDTO location;

        public InnerSchoolDTO(School school) {
            if (school.getId() != null) this.id = school.getId();
            this.name = school.getName();
            this.location = new AddressDTO(school.getAddress(), school.getZipcode());
        }
    }
}
