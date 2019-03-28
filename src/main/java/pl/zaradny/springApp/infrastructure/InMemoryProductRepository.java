package pl.zaradny.springApp.infrastructure;

import org.springframework.stereotype.Repository;
import pl.zaradny.springApp.domain.Product;
import pl.zaradny.springApp.exceptions.ProductNotFoundException;

import java.util.HashMap;
import java.util.Map;

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
            Product newProduct = new Product(product.getId(), name, product.getCreatedAt());
            products.replace(product.getId(), newProduct);
            return newProduct;

        }else{
            throw new ProductNotFoundException();
        }
    }

}
