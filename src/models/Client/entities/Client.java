package models.Client.entities;

public class Client {
    private String cpf;
    private String phone;
    private String name;
    private String email;
    private String street;
    private String city;
    private int houseNumber;

    public Client(
            String cpf,
            String phone,
            String name,
            String email,
            String street,
            String city,
            int houseNumber
    ) {
        this.cpf = cpf;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.street = street;
        this.city = city;
        this.houseNumber = houseNumber;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return "Client{" + "cpf='" + cpf + '\'' + ", phone='" + phone + '\'' + ", name='" + name + '\'' + ", email='" + email + '\'' + ", street='" + street + '\'' + ", city='" + city + '\'' + ", houseNumber=" + houseNumber + '}';
    }
}
