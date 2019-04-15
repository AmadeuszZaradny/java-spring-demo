package pl.zaradny.springApp.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.zaradny.springApp.exceptions.BadRequestException;
import pl.zaradny.springApp.exceptions.BadURLFormException;
import pl.zaradny.springApp.infrastructure.ProductRepository;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
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
        ImageDto image = new ImageDto(product.getImage());
        DescriptionDto description = new DescriptionDto(product.getDescription());
        return new ProductResponseDto(product.getId(), product.getName(), priceDto, image, description);
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
        Image image = null;
        if(productRequest.isImageValid()) {
            image = new Image(createURL(productRequest.getImage().getUrl()));
        }
        Description description = null;
        if(productRequest.isDescriptionValid()){
            description = new Description(productRequest.getDescription().getText());
        }
        Product product = new Product(id, productRequest.getName(), price, image, description, createdAt);
        productRepository.save(product);
        return new ProductResponseDto(product.getId(), product.getName(), new PriceDto(price),
                new ImageDto(product.getImage()), new DescriptionDto(product.getDescription()));
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
                 product.getName(), new PriceDto(product.getPrice()), new ImageDto(product.getImage()),
                 new DescriptionDto(product.getDescription())))
                .sorted(Comparator.comparing(ProductResponseDto::getId)).collect(Collectors.toList());

        return new ProductsResponseDto(response);
    }

    @Override
    public ProductResponseDto update(String id, ProductRequestDto productRequestDto) {
        if(!productRequestDto.isValidToUpdate()){
            throw new BadRequestException();
        }
        Product product = productRepository.findById(id);
        if(productRequestDto.isNameValid()){
            product = productRepository.updateById(product, productRequestDto.getName());
        }
        if(productRequestDto.isPriceValid()){
            product = productRepository.updateById(product, productRequestDto.getPrice().getAmount(),
                    productRequestDto.getPrice().getCurrency());
        }
        if(productRequestDto.isImageValid()){
            product = productRepository.updateById(product, createURL(productRequestDto.getImage().getUrl()));
        }
        if(productRequestDto.isDescriptionValid()){
            product = productRepository.updateById(product, new Description(productRequestDto.getDescription()));
        }
        return new ProductResponseDto(product.getId(), product.getName(), new PriceDto(product.getPrice()),
                new ImageDto(product.getImage()), new DescriptionDto(product.getDescription()));
    }

    private URL createURL(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new BadURLFormException();
        }
    }
}
