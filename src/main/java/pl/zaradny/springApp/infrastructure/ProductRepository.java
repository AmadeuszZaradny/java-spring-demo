package pl.zaradny.springApp.infrastructure;

import pl.zaradny.springApp.domain.Product;

public interface ProductRepository {

    void save(Product product);

    Product findById(String id);

    void deleteById(String id);

    Product updateById(Product product, String name);
}
