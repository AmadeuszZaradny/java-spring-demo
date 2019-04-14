package pl.zaradny.springApp.infrastructure;

import pl.zaradny.springApp.domain.Product;
import pl.zaradny.springApp.domain.ProductResponseDto;
import pl.zaradny.springApp.domain.ProductsResponseDto;

import java.util.List;

public interface ProductRepository {

    void save(Product product);

    Product findById(String id);

    void deleteById(String id);

    Product updateById(Product product, String name);

    List<Product> getAll();
}
