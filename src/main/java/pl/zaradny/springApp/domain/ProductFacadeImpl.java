package pl.zaradny.springApp.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.zaradny.springApp.infrastructure.ProductRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
class ProductFacadeImpl implements ProductFacade {

    private final ProductRepository productRepository;

    ProductFacadeImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseDto findById(String id){
        Product product = productRepository.findById(id);
        return new ProductResponseDto(product.getId(), product.getName());
    }

    @Override
    public ProductResponseDto create(ProductRequestDto productRequest) {
        if(!productRequest.isValid()){
            throw new RuntimeException("Product can not be empty!");
        }
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        Product product = new Product(id, productRequest.getName(), createdAt);
        productRepository.save(product);
        return new ProductResponseDto(product.getId(), product.getName());
    }

    @Override
    public ResponseEntity<Void> deleteById(String id) {
        productRepository.deleteById(id);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ProductsResponseDto getAll() {
        List<Product> allProducts = productRepository.getAll();
        List<ProductResponseDto> response = allProducts.stream().map(product -> new ProductResponseDto(product.getId(), product.getName()))
                .sorted(Comparator.comparing(ProductResponseDto::getId)).collect(Collectors.toList());

        return new ProductsResponseDto(response);
    }

    @Override
    public ProductResponseDto update(String id, ProductRequestDto productRequestDto) {
        if(!productRequestDto.isValid()){
            throw new RuntimeException("Product can not be empty!");
        }
        Product product = productRepository.findById(id);
        Product updatedProduct = productRepository.updateById(product, productRequestDto.getName());
        return new ProductResponseDto(updatedProduct.getId(), updatedProduct.getName());
    }

}
