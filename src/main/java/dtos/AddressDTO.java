package dtos;

public class AddressDTO {
    private String address;
    private int zipcode;

    public AddressDTO(String address, int zipcode) {
        this.address = address;
        this.zipcode = zipcode;
    }

    public String getStreetAndNumber() {
        return address;
    }

    public int getZipcode() {
        return zipcode;
    }
}
