package selj.evogl.simpleproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selj.evogl.simpleproject.exception.ProductAlreadyExistsException;
import selj.evogl.simpleproject.exception.ProductNotFoundException;
import selj.evogl.simpleproject.model.Product;
import selj.evogl.simpleproject.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

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
        if (product.getId() != null && productRepository.existsById(product.getId())) {
            throw new ProductAlreadyExistsException("Product already exists with id: " + product.getId());
        }
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product updateProduct(String id, Product product) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        product.setId(id); // Assurez-vous que l'ID reste cohérent
        return productRepository.save(product);
    }
}
