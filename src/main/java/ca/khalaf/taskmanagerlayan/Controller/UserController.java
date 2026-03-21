package ca.khalaf.taskmanagerlayan.Controller;

import ca.khalaf.taskmanagerlayan.model.Task;
import ca.khalaf.taskmanagerlayan.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final TaskService taskService;

    public UserController(TaskService taskService) {
        this.taskService = taskService;
    }

    // User dashboard: shows only this user's tasks
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Task> tasks = taskService.getTasksByOwner(username);
        model.addAttribute("tasks", tasks);
        model.addAttribute("username", username);
        return "user/dashboard";
    }

    // Show create task form
    @GetMapping("/create-task")
    public String showCreateForm() {
        return "user/create-task";
    }

    // Handle create task form submission
    @PostMapping("/create-task")
    public String createTask(@RequestParam String title,
                             @RequestParam String description,
                             Authentication authentication) {
        String username = authentication.getName();
        taskService.createTask(title, description, username);
        return "redirect:/user/dashboard";
    }

    // Mark a task complete
    @PostMapping("/complete/{id}")
    public String markComplete(@PathVariable Integer id,
                               Authentication authentication) {
        String username = authentication.getName();
        taskService.markCompleted(id, username);
        return "redirect:/user/dashboard";
    }
}
