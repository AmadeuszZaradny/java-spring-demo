package pl.zaradny.springApp.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.zaradny.springApp.exceptions.BadRequestException;
import pl.zaradny.springApp.infrastructure.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Currency;
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
        PriceDto priceDto = new PriceDto(product.getPrice().getAmount().toString(),
                                         product.getPrice().getCurrency().getCurrencyCode());
        return new ProductResponseDto(product.getId(), product.getName(), priceDto);
    }

    @Override
    public ProductResponseDto create(ProductRequestDto productRequest) {
        if(!productRequest.isValidToCreate()){
            throw new BadRequestException();
        }
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        Price price = new Price(new BigDecimal(productRequest.getPrice().getAmount()),
                                Currency.getInstance(productRequest.getPrice().getCurrency()));
        Product product = new Product(id, productRequest.getName(), price, createdAt);
        productRepository.save(product);
        return new ProductResponseDto(product.getId(), product.getName(), new PriceDto(price));
    }

    @Override
    public ResponseEntity<Void> deleteById(String id) {
        productRepository.deleteById(id);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ProductsResponseDto getAll() {
        List<Product> allProducts = productRepository.getAll();
        List<ProductResponseDto> response = allProducts.stream().map(product -> new ProductResponseDto(product.getId(),
                 product.getName(), new PriceDto(product.getPrice())))
                .sorted(Comparator.comparing(ProductResponseDto::getId)).collect(Collectors.toList());

        return new ProductsResponseDto(response);
    }

    @Override
    public ProductResponseDto update(String id, ProductRequestDto productRequestDto) {
        if(!productRequestDto.isValidToUpdate()){
            throw new BadRequestException();
        }
        Product product = productRepository.findById(id);
        Product updatedProduct;
        if(productRequestDto.getName() != null && productRequestDto.getPrice() != null){
            updatedProduct = productRepository.updateById(product, productRequestDto.getName(),
                    productRequestDto.getPrice().getAmount(), productRequestDto.getPrice().getCurrency());
        }else if(productRequestDto.getName() != null){
            updatedProduct = productRepository.updateById(product, productRequestDto.getName());
        }else {
            updatedProduct = productRepository.updateById(product, productRequestDto.getPrice().getAmount(),
                    productRequestDto.getPrice().getCurrency());
        }
        return new ProductResponseDto(updatedProduct.getId(), updatedProduct.getName(), new PriceDto(updatedProduct.getPrice()));
    }

}
