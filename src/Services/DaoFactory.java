package Services;

import Client.dao.ClientDAO;
import Client.dao.implementations.ClientDAOJDBC;

public class DaoFactory {
    public static ClientDAO getClientDAO() {
        return new ClientDAOJDBC();
    }
}
