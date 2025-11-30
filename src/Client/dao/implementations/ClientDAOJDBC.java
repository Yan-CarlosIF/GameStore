package Client.dao.implementations;

import Services.DatabaseService;
import Client.dao.ClientDAO;
import Client.dao.implementations.exceptions.ClientNotFound;
import Client.entities.Client;

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

    }

    @Override
    public void insert(Client client) {

    }

    @Override
    public void delete(String cpf) {

    }
}
