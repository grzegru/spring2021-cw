package pl.javastart.todo;

import java.util.Optional;

interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(Long id);

}
