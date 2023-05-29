package pl.javastart.todo;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class DbTaskRepository implements TaskRepository{
    private final EntityManager entityManager;
    private long nextId = 1;

    public DbTaskRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Task save(Task task) {
        task.setId(nextId);
        entityManager.persist(task);
        nextId++;
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Task.class, id));
    }
}
