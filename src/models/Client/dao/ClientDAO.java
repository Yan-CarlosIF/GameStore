package models.Client.dao;

import models.Client.entities.Client;

import java.util.List;

public interface ClientDAO {
    List<Client> find();
    Client findByCPF(String cpf);
    void update(Client client);
    void insert(Client client);
    void delete(double cpf);
}
