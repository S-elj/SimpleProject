package selj.evogl.simpleproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selj.evogl.simpleproject.exception.UserAlreadyExistsException;
import selj.evogl.simpleproject.exception.UserNotFoundException;
import selj.evogl.simpleproject.model.User;
import selj.evogl.simpleproject.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    ObjectMapper mapper= new ObjectMapper();
    @Setter
    @Getter
    private User loggedInUser;

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User createUser(User user) {
        if (userRepository.existsById(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    public User authenticateUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public User updateUser(String id, User user) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        user.setId(id);
        return userRepository.save(user);
    }

    public String getLoggedInUserJson() {
        try {
            return mapper.writeValueAsString(loggedInUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

}