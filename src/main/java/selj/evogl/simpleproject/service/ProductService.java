package selj.evogl.simpleproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selj.evogl.simpleproject.exception.ProductAlreadyExistsException;
import selj.evogl.simpleproject.exception.ProductNotFoundException;
import selj.evogl.simpleproject.model.Product;
import selj.evogl.simpleproject.model.User;
import selj.evogl.simpleproject.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    UserService userService;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();

    }


    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }


    public Product getProductByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with name: " + name));
    }

    public Product createProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new ProductAlreadyExistsException("Product already exists with name: " + product.getName());
        }

        return productRepository.save(product); // MongoDB générera automatiquement l'ID
    }


    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with name: " + name));
    }

    public Product updateProductByName(String name, Product updatedProduct) {
        Product existingProduct = findByName(name);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setExpirationDate(updatedProduct.getExpirationDate());

        return productRepository.save(existingProduct);
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    private double getProductPrice(String productName) {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with name: " + productName));
        return product.getPrice();
    }

}
