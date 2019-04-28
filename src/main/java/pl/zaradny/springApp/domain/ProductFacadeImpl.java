package pl.zaradny.springApp.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.zaradny.springApp.exceptions.*;
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
        ImageDto image = new ImageDto(product.getImage().orElse(null));
        DescriptionDto description = new DescriptionDto(product.getDescription().orElse(null));
        return new ProductResponseDto(product.getId(), product.getName(), priceDto, image, description);
    }

    @Override
    public ProductResponseDto create(ProductRequestDto productRequest) {
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        Price price = getPriceFromRequest(productRequest);
        Image image = getImageFromRequest(productRequest);
        Description description = getDescriptionFromRequest(productRequest);
        Product product = Product.build().withId(id).withName(productRequest.getName()).withPrice(price)
                .withCreatedAt(createdAt).withDescription(description).withImage(image).build();
        productRepository.save(product);
        return new ProductResponseDto(product.getId(), product.getName(), new PriceDto(price),
                new ImageDto(product.getImage().orElse(null)), new DescriptionDto(product.getDescription().orElse(null)));
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
                 product.getName(), new PriceDto(product.getPrice()), new ImageDto(product.getImage().orElse(null)),
                 new DescriptionDto(product.getDescription().orElse(null))))
                .sorted(Comparator.comparing(ProductResponseDto::getId)).collect(Collectors.toList());

        return new ProductsResponseDto(response);
    }

    @Override
    public ProductResponseDto update(String id, ProductRequestDto productRequestDto) {
        Product oldProduct = productRepository.findById(id);
        String name = productRequestDto.getName();
        Price price = getPriceFromRequest(productRequestDto);
        Image image = getImageFromRequest(productRequestDto);
        Description description = getDescriptionFromRequest(productRequestDto);
        Product newProduct = Product.build().withName(name).withPrice(price).withId(id).withCreatedAt(oldProduct.getCreatedAt())
                .withImage(image).withDescription(description).build();
        Product updatedProduct = productRepository.update(oldProduct, newProduct);
        return new ProductResponseDto(updatedProduct.getId(), updatedProduct.getName(), new PriceDto(updatedProduct.getPrice()),
                new ImageDto(updatedProduct.getImage().orElse(null)), new DescriptionDto(updatedProduct.getDescription().orElse(null)));
    }

    private Description getDescriptionFromRequest(ProductRequestDto productRequest){
        try {
            if (productRequest.getDescription() != null)
                return Description.build(productRequest.getDescription().getText());
            else return null;
        }catch (NullPointerException e){
            return null;
        }

    }

    private Image getImageFromRequest(ProductRequestDto productRequest){
        try{
            if(productRequest.getImage() != null)
                if(productRequest.getImage().getUrl() != null )return Image.build(new URL(productRequest.getImage().getUrl()));
                else return null;
            else return null;
        }catch (NullPointerException e){
            return null;
        }catch (MalformedURLException e){
            throw new BadImageURLException();
        }
    }

    private Price getPriceFromRequest(ProductRequestDto productRequest) {
        if(productRequest.getPrice() == null)  throw new ProductPriceIsNullException();
        PriceDto priceDto = productRequest.getPrice();
        try {
            return Price.build(new BigDecimal(priceDto.getAmount()),
                    Currency.getInstance(priceDto.getCurrency()));
        }catch (IllegalArgumentException | NullPointerException e){
            throw new BadPriceException();
        }
    }

}