package selj.evogl.simpleproject.simulation;

import org.springframework.context.annotation.Profile;
import selj.evogl.simpleproject.model.Product;
import selj.evogl.simpleproject.model.User;
import selj.evogl.simpleproject.service.ProductService;
import selj.evogl.simpleproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Profile("simulation")
@Component
public class SimulationRunner implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        // Ajouter des utilisateurs
        List<User> users = createDummyUsers();
        users.forEach(user -> {
            try {
                userService.createUser(user);
            } catch (Exception e) {
                System.out.println("Error while creating user: " + e.getMessage());
            }
        });

        // Ajouter des produits de test
        List<Product> products = createDummyProducts();
        products.forEach(product -> {
            try {
                User currentUser = users.get(random.nextInt(users.size()));
                userService.setLoggedInUser(currentUser);
                productService.createProduct(product);
            } catch (Exception e) {
                System.out.println("Error while creating product: " + e.getMessage());
            }
        });

        // Exécuter les scénarios de simulation
        runScenarios(users, products);
    }

    private List<User> createDummyUsers() {
        return Arrays.asList(
                createDummyUser("Alice", "alice@example.com", 30, "password1"),
                createDummyUser("Bob", "bob@example.com", 25, "password2"),
                createDummyUser("Charlie", "charlie@example.com", 28, "password3"),
                createDummyUser("Diana", "diana@example.com", 35, "password4"),
                createDummyUser("Eve", "eve@example.com", 22, "password5"),
                createDummyUser("Frank", "frank@example.com", 40, "password6"),
                createDummyUser("Grace", "grace@example.com", 33, "password7"),
                createDummyUser("Hank", "hank@example.com", 29, "password8"),
                createDummyUser("Ivy", "ivy@example.com", 26, "password9"),
                createDummyUser("Jack", "jack@example.com", 31, "password10")
        );
    }


    private List<Product> createDummyProducts() {
        return Arrays.asList(
                createDummyProduct("Laptop", 1200.50, LocalDate.now().plusMonths(24)),
                createDummyProduct("Phone", 800.00, LocalDate.now().plusMonths(12)),
                createDummyProduct("Tablet", 500.00, LocalDate.now().plusMonths(18)),
                createDummyProduct("Headphones", 150.00, LocalDate.now().plusYears(2)),
                createDummyProduct("Monitor", 300.00, LocalDate.now().plusMonths(15)),
                createDummyProduct("Keyboard", 50.00, LocalDate.now().plusYears(1)),
                createDummyProduct("Mouse", 25.00, LocalDate.now().plusMonths(10)),
                createDummyProduct("Charger", 20.00, LocalDate.now().plusMonths(6)),
                createDummyProduct("Desk", 200.00, LocalDate.now().plusYears(5)),
                createDummyProduct("Chair", 100.00, LocalDate.now().plusYears(3))
        );
    }


    public User createDummyUser(String name, String email, int age,  String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setPassword(password);
        return user;
    }

    public Product createDummyProduct(String name, double price, LocalDate expdate) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setExpirationDate(expdate);
        return product;
    }

    private void runScenarios(List<User> users, List<Product> products) {
        // Exemple de scénarios
        for (int i = 0; i < 500; i++) {
            User currentUser = users.get(random.nextInt(users.size()));
            userService.setLoggedInUser(currentUser);
            int scenario = random.nextInt(5);
            if (!(userService.getLoggedInUser() == null) ) {
                try {
                    switch (scenario) {
                        case 0:
                            displayAllProducts();
                            break;
                        case 1:
                            fetchProductByName(products);
                            break;
                        case 2:
                            createNewProduct();
                            break;
                        case 3:
                            updateRandomProduct(products);
                            break;
                        case 4:
                            deleteRandomProduct(products);
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error during scenario execution: " + e.getMessage());
                }
            }
        }
        System.out.println("Simulation done, log available in logs/user-logs.log" );

    }

    private void displayAllProducts() {
        System.out.println("\nScenario: Display All Products");
        productService.getAllProducts().forEach(System.out::println);
    }

    private void fetchProductByName(List<Product> products) {
        System.out.println("\nScenario: Fetch Product By Name");
        String randomName = products.get(random.nextInt(products.size())).getName();
        Product product = productService.getProductByName(randomName);
        System.out.println("Fetched product: " + product);
    }

    private void createNewProduct() {
        System.out.println("\nScenario: Create New Product");
        Product product = createDummyProduct(
                "Product" + random.nextInt(100),
                50 + random.nextDouble() * 500,
                LocalDate.now().plusMonths(random.nextInt(36))
        );
        productService.createProduct(product);
        System.out.println("Created product: " + product);
    }

    private void updateRandomProduct(List<Product> products) {
        System.out.println("\nScenario: Update Random Product");

        String nameToUpdate = products.get(random.nextInt(products.size())).getName();
        Product updatedProduct = createDummyProduct(
                nameToUpdate + "_Updated",
                50 + random.nextDouble() * 500,
                LocalDate.now().plusMonths(random.nextInt(36))
        );
        products.add(updatedProduct);
        productService.updateProductByName(nameToUpdate, updatedProduct);
        System.out.println("Updated product: " + updatedProduct);
    }

    private void deleteRandomProduct(List<Product> products) {
        System.out.println("\nScenario: Delete Random Product");
        Product toDelete = products.get(random.nextInt(products.size()));
        productService.deleteProduct(toDelete.getId());
        System.out.println("Deleted product with name: " + toDelete.getName());
    }
}
