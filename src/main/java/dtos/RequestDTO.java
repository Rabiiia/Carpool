package dtos;

import entities.Request;

public class RequestDTO {
    private int id;
    private UserDTO user;
    private RideDTO ride;
    private String status;

    public RequestDTO(Request request) {
        this.id = request.getId();
        this.user = new UserDTO(request.getUser());
        this.ride = new RideDTO(request.getRide());
        this.status = request.getStatus();
    }
}
