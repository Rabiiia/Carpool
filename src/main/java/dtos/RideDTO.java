package dtos;

import entities.Ride;

import java.util.List;
import java.util.stream.Collectors;

public class RideDTO {
    private int id;
    private String origin;
    private String destination;
    private long arrival;
    private int seats;
    private UserDTO driver;
    private List<UserDTO> passengers;

    public RideDTO(Ride ride) {
        id = ride.getId();
        origin = ride.getOrigin();
        destination = ride.getDestination();
        arrival = ride.getArrivalTime();
        seats = ride.getSeats();
        driver = new UserDTO(ride.getDriver());
        passengers = ride.getPassengers().stream().map(UserDTO::new).collect(Collectors.toList());
    }
}
