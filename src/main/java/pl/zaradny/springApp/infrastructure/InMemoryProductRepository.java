package pl.zaradny.springApp.infrastructure;

import org.springframework.stereotype.Repository;
import pl.zaradny.springApp.domain.*;
import pl.zaradny.springApp.exceptions.ProductNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

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
    public Product update(Product oldProduct, Product newProduct){
        if(products.containsKey(oldProduct.getId())){
            products.replace(oldProduct.getId(), newProduct);
            return newProduct;
        }else{
            throw new ProductNotFoundException();
        }
    }

    @Override
    public List<Product> getAll() {
        return List.copyOf(products.values());
    }

    @Override
    public List<Product> findByTag(Tag tag){
        return products.values().stream()
                .filter(product ->  product.getTags().isPresent() && product.getTags().get().contains(tag))
                .collect(Collectors.toList());
    }
}
