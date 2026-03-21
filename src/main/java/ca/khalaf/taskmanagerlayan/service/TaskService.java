package ca.khalaf.taskmanagerlayan.service;

import ca.khalaf.taskmanagerlayan.model.Task;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    // In-memory task storage; in a real app, this would be a database
    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    // Returns only tasks that belong to the requesting user
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public List<Task> getTasksByOwner(String username) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getOwnerUsername().equals(username)) {
                result.add(task);
            }
        }
        return result;
    }

    // Creates a task with the owner stamped at creation time
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Task createTask(String title, String description, String ownerUsername) {
        Task task = new Task(nextId++, title, description, false, ownerUsername);
        tasks.add(task);
        return task;
    }

    // Marks a task complete; caller must be the owner or admin
    @PreAuthorize("hasRole('ADMIN') or @taskService.isOwner(#taskId, authentication.name)")
    public boolean markCompleted(Integer taskId, String username) {
        for (Task task : tasks) {
            if (task.getId() == taskId && task.getOwnerUsername().equals(username)) {
                task.setCompleted(true);
                return true;
            }
        }
        return false;
    }

    // Admin only: returns every task
    @PreAuthorize("hasRole('ADMIN')")
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    // Admin only: deletes any task by id
    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteTask(Integer taskId) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == taskId) {
                tasks.remove(i);
                return true;
            }
        }
        return false;
    }

    // Used by @PreAuthorize to check ownership
    public boolean isOwner(Integer taskId, String username) {
        for (Task task : tasks) {
            if (task.getId() == taskId && task.getOwnerUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
