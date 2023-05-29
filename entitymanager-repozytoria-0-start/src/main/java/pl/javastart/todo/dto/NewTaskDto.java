package pl.javastart.todo.dto;

public class NewTaskDto {
    private final String title;
    private final String description;
    private final int priority;

    public NewTaskDto(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
