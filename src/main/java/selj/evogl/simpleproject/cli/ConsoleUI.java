package selj.evogl.simpleproject.cli;

import selj.evogl.simpleproject.model.Product;
import selj.evogl.simpleproject.service.ProductService;
import selj.evogl.simpleproject.exception.ProductNotFoundException;
import selj.evogl.simpleproject.exception.ProductAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;

@Component
public class ConsoleUI implements CommandLineRunner {

    @Autowired
    private ProductService productService;
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            try {
                switch (choice) {
                    case 1:
                        displayAllProducts();
                        break;
                    case 2:
                        fetchProductById();
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
                        running = false;
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

    private void displayMenu() {
        System.out.println("\n=== Product Management System ===");
        System.out.println("1. Display all products");
        System.out.println("2. Fetch product by ID");
        System.out.println("3. Add new product");
        System.out.println("4. Delete product");
        System.out.println("5. Update product");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private void displayAllProducts() {
        System.out.println("\n=== All Products ===");
        productService.getAllProducts().forEach(System.out::println);
    }

    private void fetchProductById() {
        System.out.print("Enter product ID: ");
        Long id = scanner.nextLong();
        Product product = productService.getProductById(id);
        System.out.println(product);
    }

    private void addNewProduct() {
        Product product = new Product();

        System.out.print("Enter product ID: ");
        product.setId(scanner.nextLong());
        scanner.nextLine(); // consume newline

        System.out.print("Enter product name: ");
        product.setName(scanner.nextLine());

        System.out.print("Enter product price: ");
        product.setPrice(scanner.nextDouble());

        System.out.print("Enter expiration date (YYYY-MM-DD): ");
        scanner.nextLine(); // consume newline
        product.setExpirationDate(LocalDate.parse(scanner.nextLine()));

        productService.createProduct(product);
        System.out.println("Product added successfully!");
    }

    private void deleteProduct() {
        System.out.print("Enter product ID to delete: ");
        Long id = scanner.nextLong();
        productService.deleteProduct(id);
        System.out.println("Product deleted successfully!");
    }

    private void updateProduct() {
        System.out.print("Enter product ID to update: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // consume newline

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