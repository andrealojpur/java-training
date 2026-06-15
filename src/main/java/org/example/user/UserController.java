package org.example.user;

import org.example.pipeline.PipelineException;
import org.example.pipeline.PipelineRunner;
import org.example.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PipelineRunner pipelineRunner;

    public UserController(UserService userService, PipelineRunner pipelineRunner) {
        this.userService = userService;
        this.pipelineRunner = pipelineRunner;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {

        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(201).body(createdUser);
        } catch (ValidationException exception) {
            return ResponseEntity.badRequest().body(exception.getErrors());
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitUser(@RequestBody User user) {
        try {
            User processedUser = pipelineRunner.runInitialPipeline(user);
            return ResponseEntity.status(201).body(processedUser);
        } catch (ValidationException exception) {
            return ResponseEntity.badRequest().body(exception.getErrors());
        } catch (PipelineException exception) {
            return ResponseEntity.badRequest().body(List.of(exception.getMessage()));
        }
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    try {
                        User completedUser = pipelineRunner.runCompletionPipeline(user);
                        return ResponseEntity.ok(completedUser);
                    } catch (ValidationException exception) {
                        return ResponseEntity.badRequest().body(exception.getErrors());
                    } catch (PipelineException exception) {
                        return ResponseEntity.badRequest().body(List.of(exception.getMessage()));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
