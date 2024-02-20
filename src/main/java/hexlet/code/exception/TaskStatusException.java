package hexlet.code.exception;

public class TaskStatusException extends NotFoundException{

    TaskStatusException(String message) {
        super(message);
    }
}
