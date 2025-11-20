package Services;

import models.Client.dao.ClientDAO;
import models.Client.dao.implementations.ClientDAOJDBC;

public class DaoFactory {
    public static ClientDAO getClientDAO() {
        return new ClientDAOJDBC();
    }
}
