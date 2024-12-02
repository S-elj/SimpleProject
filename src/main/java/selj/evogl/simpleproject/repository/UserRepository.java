package selj.evogl.simpleproject.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import selj.evogl.simpleproject.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailAndPassword(String email, String password);
}
