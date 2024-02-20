package hexlet.code.exception;

public class TaskNotFoundException extends NotFoundException{

    TaskNotFoundException(String message) {
        super(message);
    }
}
