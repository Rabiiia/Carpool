package dtos;

import entities.User;

public class UserDTO {
    int id;
    String username;
    String address;
    int phone;
    int zipcode;String name;
    String role;
    String schoolName;
    String location;


    public UserDTO(User user) {
        if(user.getId() != null) this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.zipcode = user.getZipcode();
        this.role = user.getRole();
        this.schoolName = user.getSchool().getName();
        this.location = user.getSchool().getLocation();
    }

    public String getRole() {
        return role;
    }
}
