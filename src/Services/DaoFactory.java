package Services;

import models.Client.dao.ClientDAO;
import models.Client.dao.implementations.ClientDAOJDBC;
import models.Product.dao.ProductDAO;
import models.Product.dao.implementations.ProductDAOJDBC;
import models.Order.dao.OrderDAO;
import models.Order.dao.implementations.OrderDAOJDBC;
import models.Game.dao.GameDAO;
import models.Game.dao.implementations.GameDAOJDBC;
import models.EletronicItem.dao.EletronicItemDAO;
import models.EletronicItem.dao.implementations.EletronicItemDAOJDBC;
import models.Genre.dao.GenreDAO;
import models.Genre.dao.implementations.GenreDAOJDBC;
import models.GameGenre.dao.GameGenreDAO;
import models.GameGenre.dao.implementations.GameGenreDAOJDBC;
import models.OrderProduct.dao.OrderProductDAO;
import models.OrderProduct.dao.implementations.OrderProductDAOJDBC;

public class DaoFactory {
    public static ClientDAO getClientDAO() {
        return new ClientDAOJDBC();
    }

    public static ProductDAO getProductDAO() {
        return new ProductDAOJDBC();
    }

    public static OrderDAO getOrderDAO() {
        return new OrderDAOJDBC();
    }

    public static GameDAO getGameDAO() {
        return new GameDAOJDBC();
    }

    public static EletronicItemDAO getEletronicItemDAO() {
        return new EletronicItemDAOJDBC();
    }

    public static GenreDAO getGenreDAO() {
        return new GenreDAOJDBC();
    }

    public static GameGenreDAO getGameGenreDAO() {
        return new GameGenreDAOJDBC();
    }

    public static OrderProductDAO getOrderProductDAO() {
        return new OrderProductDAOJDBC();
    }
}
