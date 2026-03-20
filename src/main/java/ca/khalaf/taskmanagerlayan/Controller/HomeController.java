package ca.khalaf.taskmanagerlayan.Controller;

import ca.khalaf.taskmanagerlayan.model.Task;
import ca.khalaf.taskmanagerlayan.service.TaskService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class HomeController {
    import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

    @Controller
    public class HomeController {

        // Public home page — no login required
        // maps to "/"
        @GetMapping("/")
        public String home() {
            return "home";  // loads templates/home.html
        }

        // After login, Spring sends the user to /dashboard
        // This method checks their role and redirects them to the right place
        @GetMapping("/dashboard")
        public String dashboard(Authentication authentication) {

            // Check if the logged in user has ROLE_ADMIN
            if (authentication.getAuthorities()
                    .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return "redirect:/admin/dashboard";  // send admin to admin page
            }

            return "redirect:/user/dashboard";  // send user to user page
        }

        // This page shows when someone tries to access a URL they are not allowed to
        // For example: alice tries to go to /admin/dashboard
        @GetMapping("/access-denied")
        public String accessDenied() {
            return "access-denied";  // loads templates/access-denied.html
        }
    }

    FILE 6 — UserController.java
    Path: src/main/java/com/example/taskmanager/controller/UserController.java
    Right-click controller package → New → Java Class → name it UserController
    javapackage com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
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
    @RequestMapping("/user")  // all routes in this controller start with /user
    public class UserController {

        // Spring automatically injects TaskService here
        private final TaskService taskService;

        public UserController(TaskService taskService) {
            this.taskService = taskService;
        }

        // GET /user/dashboard
        // Shows only the tasks that belong to the logged in user
        @GetMapping("/dashboard")
        public String dashboard(Model model, Authentication authentication) {

            // get the username of whoever is logged in right now
            String username = authentication.getName();

            // fetch only their tasks from the service
            List<Task> myTasks = taskService.getTasksByOwner(username);

            // pass the tasks and username to the HTML template
            model.addAttribute("tasks", myTasks);
            model.addAttribute("username", username);

            return "user/dashboard";  // loads templates/user/dashboard.html
        }

        // GET /user/create-task
        // Just shows the empty form
        @GetMapping("/create-task")
        public String showCreateForm() {
            return "user/create-task";  // loads templates/user/create-task.html
        }

        // POST /user/create-task
        // Handles the form submission when user clicks Create Task
        @PostMapping("/create-task")
        public String createTask(@RequestParam String title,
                                 @RequestParam String description,
                                 Authentication authentication) {

            // get who is logged in
            String username = authentication.getName();


            // send them back to their dashboard after creating
            return "redirect:/user/dashboard";
        }

        // POST /user/complete/{id}
        // Called when user clicks Mark Complete button on a task
        @PostMapping("/complete/{id}")
        public String markComplete(@PathVariable Integer id,
                                   Authentication authentication) {

            String username = authentication.getName();
            return "redirect:/user/dashboard";
        }
}
