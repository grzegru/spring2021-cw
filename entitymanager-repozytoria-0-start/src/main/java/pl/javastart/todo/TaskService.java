package pl.javastart.todo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.javastart.todo.dto.NewTaskDto;
import pl.javastart.todo.dto.TaskDurationDto;
import pl.javastart.todo.exception.TaskALreadyCompletedException;
import pl.javastart.todo.exception.TaskAlreadyStartedException;
import pl.javastart.todo.exception.TaskNotFoundException;
import pl.javastart.todo.exception.TaskNotStartedException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Long saveTask(NewTaskDto task){
        Task taskToSave = new Task(task.getTitle(), task.getDescription(), task.getPriority());
        Task savedTask = taskRepository.save(taskToSave);
        return savedTask.getId();
    }

    public Optional<String> getTaskInfo(Long taskId){
        Optional<Task> byId = taskRepository.findById(taskId);
        return byId.map(Task::toString);
    }

    @Transactional
    public LocalDateTime startTask(Long taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
        if(task.getStartTime() != null){
            throw new TaskAlreadyStartedException();
        }
        task.setStartTime(LocalDateTime.now());
        return task.getStartTime();
    }

    @Transactional
    public TaskDurationDto completeTask(Long taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
        if(task.getStartTime() == null){
            throw new TaskNotStartedException();
        }else if(task.getCompletionTime() != null){
            throw new TaskALreadyCompletedException();
        }
        task.setCompletionTime(LocalDateTime.now());
        return new TaskDurationDto(task.getStartTime(), task.getCompletionTime());
    }
}
