package selj.evogl.simpleproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    private Long id;
    private String name;
    private double price;
    private LocalDate expirationDate;
}
