package pl.zaradny.springApp.infrastructure;

import pl.zaradny.springApp.domain.Product;
import pl.zaradny.springApp.domain.Tag;

import java.util.List;

public interface ProductRepository {

    void save(Product product);

    Product findById(String id);

    void deleteById(String id);

    Product update(Product oldProduct, Product newProduct);

    List<Product> getAll();

    List<Product> findByTag(Tag tag);
}
