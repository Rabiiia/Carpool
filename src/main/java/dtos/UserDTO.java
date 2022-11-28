package dtos;

import entities.User;

import java.util.List;

public class UserDTO {

    String name;
    String role;

    public UserDTO(User user) {
        this.name = user.getUsername();
        this.role = user.getRole();
    }

    public String getRole() {
        return role;
    }
}
