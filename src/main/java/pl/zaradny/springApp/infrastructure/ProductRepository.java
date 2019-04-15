package pl.zaradny.springApp.infrastructure;

import pl.zaradny.springApp.domain.Product;

import java.util.List;

public interface ProductRepository {

    void save(Product product);

    Product findById(String id);

    void deleteById(String id);

    Product updateById(Product product, String name);
    Product updateById(Product product, String amount, String currency);
    Product updateById(Product product, String name, String amount, String currency);

    List<Product> getAll();
}
