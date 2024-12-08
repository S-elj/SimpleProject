package selj.evogl.simpleproject.log_parser;

import java.util.HashMap;
import java.util.Map;

public class UserProfile {
    private String userId;
    private String userName;
    private String email;
    private int age;
    private int readOperations;
    private int writeOperations;
    private Map<String, Double> productsSearched; // nom du produit -> prix

    public UserProfile(String userId, String userName, String email, int age) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.age = age;
        this.readOperations = 0;
        this.writeOperations = 0;
        this.productsSearched = new HashMap<>();
    }

    // Incrémenter les opérations READ/WRITE
    public void incrementOperation(String operation) {
        if ("READ".equalsIgnoreCase(operation)) {
            this.readOperations++;
        } else if ("WRITE".equalsIgnoreCase(operation)) {
            this.writeOperations++;
        }
    }

    // Ajouter un produit recherché
    public void addProductSearched(String productName, double productPrice) {
        this.productsSearched.put(productName, productPrice);
    }

    public String getUserId() { return userId; }
    public String getUserName() { return userName;}
    public String getUserMail() { return email; }
    public int getUserAge() { return age; }
    public int getReadOperations() { return readOperations; }
    public int getWriteOperations() { return writeOperations; }
    public Map<String, Double> getProductsSearched() { return productsSearched; }

    public double getMostExpensiveProductPrice() {
        return productsSearched.values().stream().max(Double::compare).orElse(0.0);
    }
}
