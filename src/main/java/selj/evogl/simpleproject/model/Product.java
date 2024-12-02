package selj.evogl.simpleproject.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "products") // Indique la collection MongoDB
public class Product {
    @Id
    private String id;
    private String name;
    private double price;
    private LocalDate expirationDate;
}
