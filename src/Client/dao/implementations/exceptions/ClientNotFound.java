package Client.dao.implementations.exceptions;

public class ClientNotFound extends Exception {
    public ClientNotFound() {
        super("Cliente não foi encontrado");
    }
}
