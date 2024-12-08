
package selj.evogl.simpleproject.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users") // Indique la collection MongoDB
public class User {
    @Id
    private String id;
    private String name;
    private int age;
    private String email;
    private String password;

}
