package dtos;

import entities.User;

public class UserDTO {
    int id;
    String username;
    String address;
    int phone;
    int zipcode;String name;
    String role;

    String password;


    int schoolId;

    public UserDTO(User user) {
        if(user.getId() != null) this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.zipcode = user.getZipcode();
        this.role = user.getRole();
    }

    public UserDTO(String username, String password, String address, int phone, int zipcode, String name, String role, int schoolId) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.zipcode = zipcode;
        this.name = name;
        this.role = role;
        this.schoolId = schoolId;
    }

    public String getRole() {
        return role;
    }
}
