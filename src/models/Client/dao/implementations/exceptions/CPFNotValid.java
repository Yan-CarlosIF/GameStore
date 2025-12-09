package models.Client.dao.implementations.exceptions;

public class CPFNotValid extends RuntimeException {
    public CPFNotValid() {
        super("Erro: CPF inválido! Deve conter exatamente 11 dígitos numéricos.");
    }
    
    public CPFNotValid(String message) {
        super(message);
    }
}
