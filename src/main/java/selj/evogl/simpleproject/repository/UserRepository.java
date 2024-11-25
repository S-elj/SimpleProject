package selj.evogl.simpleproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selj.evogl.simpleproject.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
