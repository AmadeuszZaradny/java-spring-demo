package pl.zaradny.springApp.infrastructure;

import pl.zaradny.springApp.domain.Description;
import pl.zaradny.springApp.domain.Product;

import java.net.URL;
import java.util.List;

public interface ProductRepository {

    void save(Product product);

    Product findById(String id);

    void deleteById(String id);

    Product updateById(Product product, String name);
    Product updateById(Product product, String amount, String currency);
    Product updateById(Product product, URL imageURL);
    Product updateById(Product product, Description description);

    List<Product> getAll();
}
