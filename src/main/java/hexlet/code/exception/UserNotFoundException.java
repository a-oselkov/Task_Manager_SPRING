package hexlet.code.exception;

public class UserNotFoundException extends NotFoundException{

    UserNotFoundException(String message) {
        super(message);
    }
}
