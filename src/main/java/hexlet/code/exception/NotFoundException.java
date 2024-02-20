package hexlet.code.exception;

public class NotFoundException extends  RuntimeException{

    NotFoundException(String message) {
        super(message);
    }
}
