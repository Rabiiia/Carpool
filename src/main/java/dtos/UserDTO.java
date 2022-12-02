package dtos;

import entities.User;

public class UserDTO {
    int id;
    String username;
    String address;
    int phone;
    int zipcode;
    String name;
    String role;

    public UserDTO(User user) {
        if(user.getId() != null)
            this.id = user.getId();
        this.username = user.getName();
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
