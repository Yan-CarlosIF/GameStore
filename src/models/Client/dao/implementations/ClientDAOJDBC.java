package models.Client.dao.implementations;

import Services.DatabaseService;
import models.Client.dao.ClientDAO;
import models.Client.dao.implementations.exceptions.ClientNotFound;
import models.Client.entities.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOJDBC implements ClientDAO {
    private final DatabaseService db = DatabaseService.getInstance();
    private final Connection connection = db.getConnection();

    @Override
    public List<Client> find() {
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Client> clients = new ArrayList<>();

        try {
            st = connection.prepareStatement("SELECT * FROM Client");
            rs = st.executeQuery();

            while (rs.next()) {
                Client client = new Client(
                    rs.getString("cpf"),
                    rs.getString("phone"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("street"),
                    rs.getString("city"),
                    rs.getInt("house_number")
                );

                clients.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return clients;
    }

    @Override
    public Client findByCPF(String cpf) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement("SELECT * FROM Client WHERE cpf = ?");
            st.setString(1, cpf);

            rs = st.executeQuery();

            if (rs.next()) {
                return new Client(
                    rs.getString("cpf"),
                    rs.getString("phone"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("street"),
                    rs.getString("city"),
                    rs.getInt("house_number")
                );
            }

            throw new ClientNotFound();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por CPF", e);
        } catch (ClientNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return null;
    }

    @Override
    public void update(Client client) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                "UPDATE Client SET phone = ?, name = ?, email = ?, street = ?, city = ?, house_number = ? WHERE cpf = ?"
            );
            st.setString(1, client.getPhone());
            st.setString(2, client.getName());
            st.setString(3, client.getEmail());
            st.setString(4, client.getStreet());
            st.setString(5, client.getCity());
            st.setInt(6, client.getHouseNumber());
            st.setString(7, client.getCpf());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new ClientNotFound();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        } catch (ClientNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void insert(Client client) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                "INSERT INTO Client (cpf, phone, name, email, street, city, house_number) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            st.setString(1, client.getCpf());
            st.setString(2, client.getPhone());
            st.setString(3, client.getName());
            st.setString(4, client.getEmail());
            st.setString(5, client.getStreet());
            st.setString(6, client.getCity());
            st.setInt(7, client.getHouseNumber());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente", e);
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void delete(String cpf) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("DELETE FROM Client WHERE cpf = ?");
            st.setString(1, cpf);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new ClientNotFound();
            }
            System.out.println("Cliente deletado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente", e);
        } catch (ClientNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
        }
    }
}
