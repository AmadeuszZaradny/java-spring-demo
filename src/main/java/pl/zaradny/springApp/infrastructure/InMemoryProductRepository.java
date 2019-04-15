package pl.zaradny.springApp.infrastructure;

import org.springframework.stereotype.Repository;
import pl.zaradny.springApp.domain.Description;
import pl.zaradny.springApp.domain.Image;
import pl.zaradny.springApp.domain.Price;
import pl.zaradny.springApp.domain.Product;
import pl.zaradny.springApp.exceptions.ProductNotFoundException;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> products = new HashMap<>();

    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public Product findById(String id) {
        if(products.containsKey(id)){
            return products.get(id);
        }
        throw new ProductNotFoundException();
    }

    @Override
    public void deleteById(String id) {
        if(products.containsKey(id)){
            products.remove(id);
        }else{
            throw new ProductNotFoundException();
        }
    }

    @Override
    public Product updateById(Product product, String name) {
        if(products.containsKey(product.getId())){
            Product newProduct = new Product(product.getId(), name, product.getPrice(), product.getImage(),
                    product.getDescription(), product.getCreatedAt());
            products.replace(product.getId(), newProduct);
            return newProduct;
        }else{
            throw new ProductNotFoundException();
        }
    }

    @Override
    public Product updateById(Product product, String amount, String currency) {
        if(products.containsKey(product.getId())){
            Price newPrice = new Price(new BigDecimal(amount), Currency.getInstance(currency));
            Product newProduct = new Product(product.getId(), product.getName(), newPrice, product.getImage(),
                    product.getDescription(), product.getCreatedAt());
            products.replace(product.getId(), newProduct);
            return newProduct;
        }else{
            throw new ProductNotFoundException();
        }
    }

    @Override
    public Product updateById(Product product, URL url){
        if(products.containsKey(product.getId())){
            Product newProduct = new Product(product.getId(), product.getName(), product.getPrice(), new Image(url),
                    product.getDescription(), product.getCreatedAt());
            products.replace(product.getId(), newProduct);
            return newProduct;
        }else{
            throw new ProductNotFoundException();
        }
    }

    @Override
    public Product updateById(Product product, Description description){
        if(products.containsKey(product.getId())){
            Product newProduct = new Product(product.getId(), product.getName(), product.getPrice(), product.getImage(),
                    description, product.getCreatedAt());
            products.replace(product.getId(), newProduct);
            return newProduct;
        }else{
            throw new ProductNotFoundException();
        }
    }

    @Override
    public List<Product> getAll() {
        return List.copyOf(products.values());
    }
}
