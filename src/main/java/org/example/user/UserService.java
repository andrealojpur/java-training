package org.example.user;

import org.example.validation.ValidationEngine;
import org.example.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private final ValidationEngine validationEngine;

    public UserService(ValidationEngine validationEngine) {
        this.validationEngine = validationEngine;

        users.add(new User(1L, "Milos", "milos@example.com", "password123"));
        users.add(new User(2L, "Nikola", "nikola@example.com", "password456"));
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public Long getNextId() {
        return users.stream()
                .map(User::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    public void save(User user) {
        Optional<User> existingUserOptional = getUserById(user.getId());

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setStatus(user.getStatus());
            return;
        }

        users.add(user);
    }

    public User createUser(User user) {

        List<String> validationErrors = validationEngine.validate(user);

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        users.add(user);
        return user;
    }

    public Optional<User> updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOptional = getUserById(id);

        if(existingUserOptional.isEmpty()) {
            return Optional.empty();
        }

        User existingUser = existingUserOptional.get();

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setStatus(updatedUser.getStatus());

        return Optional.of(existingUser);
    }

    public boolean deleteUser(Long id) {
        return users.removeIf(user -> user.getId().equals(id));
    }
}
