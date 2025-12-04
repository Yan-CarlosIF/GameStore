package models.EletronicItem.dao.implementations.exceptions;

public class EletronicItemNotFound extends Exception {
    public EletronicItemNotFound() {
        super("Item eletrônico não foi encontrado");
    }
}
