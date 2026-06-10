package org.example.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    public UserService() {
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

    public User createUser(User user) {
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

        return Optional.of(existingUser);
    }

    public boolean deleteUser(Long id) {
        return users.removeIf(user -> user.getId().equals(id));
    }
}
