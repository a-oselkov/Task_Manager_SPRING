package hexlet.code.exception;

public class TaskStatusException extends NotFoundException {

    public TaskStatusException(String message) {
        super(message);
    }
}
