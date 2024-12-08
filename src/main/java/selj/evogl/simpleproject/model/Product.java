package selj.evogl.simpleproject.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "products") // Indique la collection MongoDB
public class Product {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private double price;
    private LocalDate expirationDate;
}
