package selj.evogl.simpleproject.cli;

import selj.evogl.simpleproject.model.Product;
import selj.evogl.simpleproject.model.User;
import selj.evogl.simpleproject.repository.ProductRepository;
import selj.evogl.simpleproject.service.ProductService;
import selj.evogl.simpleproject.service.UserService;
import selj.evogl.simpleproject.exception.ProductNotFoundException;
import selj.evogl.simpleproject.exception.ProductAlreadyExistsException;
import selj.evogl.simpleproject.exception.UserNotFoundException;
import selj.evogl.simpleproject.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;

@Component
public class ConsoleUI implements CommandLineRunner {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    private Scanner scanner = new Scanner(System.in);
    private User loggedInUser = null;

    @Override
    public void run(String... args) {
        boolean running = true;
        while (running) {
            if (loggedInUser == null) {
                displayAuthMenu();
                int authChoice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                try {
                    switch (authChoice) {
                        case 1:
                            registerUser();
                            break;
                        case 2:
                            loginUser();
                            break;
                        case 3:
                            System.out.println("Exiting...");
                            System.exit(0);
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (UserNotFoundException | UserAlreadyExistsException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                try {
                    switch (choice) {
                        case 1:
                            displayAllProducts();
                            break;
                        case 2:
                            fetchProductByName();
                            break;
                        case 3:
                            addNewProduct();
                            break;
                        case 4:
                            deleteProduct();
                            break;
                        case 5:
                            updateProduct();
                            break;
                        case 6:
                            loggedInUser = null; // logout
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (ProductNotFoundException | ProductAlreadyExistsException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
            }
        }
    }

    private void displayAuthMenu() {
        System.out.println("\n=== User Authentication ===");
        System.out.println("1. Register new user");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private void registerUser() {
        User user = new User();
        System.out.print("Enter username: ");
        user.setName(scanner.nextLine());

        System.out.print("Enter email: ");
        user.setEmail(scanner.nextLine());

        System.out.print("Enter password: ");
        user.setPassword(scanner.nextLine());

        userService.createUser(user);
        System.out.println("User registered successfully!");
    }

    private void loginUser() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            loggedInUser = userService.authenticateUser(email, password);
            System.out.println("Login successful! Welcome " + loggedInUser.getName());
        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayMenu() {
        System.out.println("\n=== Product Management System ===");
        System.out.println("1. Display all products");
        System.out.println("2. Fetch product by name");
        System.out.println("3. Add new product");
        System.out.println("4. Delete product");
        System.out.println("5. Update product");
        System.out.println("6. Logout");
        System.out.print("Enter your choice: ");
    }

    private void displayAllProducts() {
        System.out.println("\n=== All Products ===");
        productService.getAllProducts().forEach(System.out::println);
    }

    private void fetchProductByName() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        Product product = productService.getProductByName(name);
        System.out.println(product);
    }


    private void addNewProduct() {
        Product product = new Product();

        System.out.print("Enter product ID: ");
        product.setId(scanner.nextLine());

        System.out.print("Enter product name: ");
        product.setName(scanner.nextLine());

        System.out.print("Enter product price: ");
        product.setPrice(scanner.nextDouble());

        System.out.print("Enter expiration date (YYYY-MM-DD): ");
        scanner.nextLine();
        product.setExpirationDate(LocalDate.parse(scanner.nextLine()));

        productService.createProduct(product);
        System.out.println("Product added successfully!");
    }


    private void deleteProduct() {
        System.out.print("Enter product ID to delete: ");
        String id = scanner.nextLine();
        productService.deleteProduct(id);
        System.out.println("Product deleted successfully!");
    }


    private void updateProduct() {
        System.out.print("Enter product ID to update: ");
        String id = scanner.nextLine();

        Product product = new Product();
        product.setId(id);

        System.out.print("Enter new product name: ");
        product.setName(scanner.nextLine());

        System.out.print("Enter new product price: ");
        product.setPrice(scanner.nextDouble());

        System.out.print("Enter new expiration date (YYYY-MM-DD): ");
        scanner.nextLine(); // consume newline
        product.setExpirationDate(LocalDate.parse(scanner.nextLine()));

        productService.updateProduct(id, product);
        System.out.println("Product updated successfully!");
    }

}
