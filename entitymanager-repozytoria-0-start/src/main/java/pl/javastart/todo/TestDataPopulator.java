package pl.javastart.todo;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.javastart.todo.dto.NewTaskDto;

@Service
@Profile("dev")
public class TestDataPopulator {
    private TaskService taskService;

    public TestDataPopulator(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostConstruct
    void populateTestData(){
        taskService.saveTask(new NewTaskDto("Nauka Springa", "Nauczyć się obsługiwać bazy danych w Springu", 90));
        taskService.saveTask(new NewTaskDto("Poprawić budżet domowy", "Sprawdzić arkusz, który błędnie liczy budżet domowy", 50));
        taskService.saveTask(new NewTaskDto("Auto do mechanika", "Umówić i zawieźć auto do mechanika na przegląd", 80));
        taskService.saveTask(new NewTaskDto("Zaplanować wakacje", "Wyszukać i zaklepać wakacje w biurze podróży", 70));
        taskService.saveTask(new NewTaskDto("Kupić farbę", "Kupić białą farbę do odmalowania mieszkania", 30));
        taskService.startTask(1L);
        taskService.completeTask(1L);
        taskService.startTask(3L);
        taskService.completeTask(3L);
    }
}
