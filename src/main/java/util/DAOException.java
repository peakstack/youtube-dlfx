package util;

public class DAOException extends Exception {

    private static final long serialVersionUID = -341696119747763184L;

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

}
