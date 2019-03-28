package pl.zaradny.springApp.domain;

import org.springframework.stereotype.Component;
import pl.zaradny.springApp.infrastructure.ProductRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ProductFacadeImpl implements ProductFacade {

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
    public ProductResponseDto deleteById(String id) {
        Product product = productRepository.findById(id);
        productRepository.deleteById(id);
        return new ProductResponseDto(product.getId(), product.getName());
    }

    @Override
    public ProductResponseDto updateById(String id, ProductRequestDto productRequestDto) {

        if(!productRequestDto.isValid()){
            throw new RuntimeException("Product can not be empty!");
        }

        Product product = productRepository.findById(id);
        Product updatedProduct = productRepository.updateById(product, productRequestDto.getName());

        return new ProductResponseDto(updatedProduct.getId(), updatedProduct.getName());
    }

}
