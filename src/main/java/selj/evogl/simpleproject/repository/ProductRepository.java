package selj.evogl.simpleproject.repository;

import selj.evogl.simpleproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}