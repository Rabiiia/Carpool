package dtos;

import entities.User;

import java.util.List;

public class UserDTO {

     int id;

     String username;

     String address;

     String password;

     int phone;
     int zipcode;
     String name;
     String role;

    public UserDTO(User user) {
        if(user.getId() != null)
            this.id = user.getId();
        this.username = user.getName();
        this.password = user.getPassword();
        this.name = user.getUsername();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.zipcode = user.getZipcode();
        this.role = user.getRole();
    }

    public String getRole() {
        return role;
    }
}
