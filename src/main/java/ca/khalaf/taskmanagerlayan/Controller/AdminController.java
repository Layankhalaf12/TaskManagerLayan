package ca.khalaf.taskmanagerlayan.Controller;

import ca.khalaf.taskmanagerlayan.model.Task;
import ca.khalaf.taskmanagerlayan.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TaskService taskService;

    public AdminController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Admin dashboard: shows all tasks
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "admin/dashboard";
    }

    // Admin deletes any task by id
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return "redirect:/admin/dashboard";
    }
}
