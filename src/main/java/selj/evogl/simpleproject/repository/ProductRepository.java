package selj.evogl.simpleproject.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import selj.evogl.simpleproject.model.Product;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByName(String name);

    boolean existsByName(String newName);
}