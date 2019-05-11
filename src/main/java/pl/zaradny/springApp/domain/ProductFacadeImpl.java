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
        PriceDto priceDto = createPriceDtoToResponse(product.getPrice());
        ImageDto imageDto = createImageDtoToResponse(product.getImage().orElse(null));
        DescriptionDto descriptionDto = createDescriptionDtoToResponse(product.getDescription().orElse(null));
        List<TagDto> tagsDto = createTagsDtoToResponse(product.getTags().orElse(null));
        return new ProductResponseDto(product.getId(), product.getName(), priceDto, imageDto, descriptionDto, tagsDto);
    }

    @Override
    public ProductResponseDto create(ProductRequestDto productRequest) {
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        Price price = getPriceFromRequest(productRequest);
        Image image = getImageFromRequest(productRequest);
        Description description = getDescriptionFromRequest(productRequest);
        List<Tag> tags = getTagsFromRequest(productRequest);
        Product product = Product.build().withId(id).withName(productRequest.getName()).withPrice(price)
                .withCreatedAt(createdAt).withDescription(description).withImage(image).withTags(tags).build();
        productRepository.save(product);
        return new ProductResponseDto(product.getId(), product.getName(), createPriceDtoToResponse(price),
                createImageDtoToResponse(image), createDescriptionDtoToResponse(description),
                createTagsDtoToResponse(tags));
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
                 product.getName(), createPriceDtoToResponse(product.getPrice()),
                 createImageDtoToResponse(product.getImage().orElse(null)),
                 createDescriptionDtoToResponse(product.getDescription().orElse(null)),
                 createTagsDtoToResponse(product.getTags().orElse(null))))
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
        List<Tag> tags = getTagsFromRequest(productRequestDto);
        Product newProduct = Product.build().withName(name).withPrice(price).withId(id).withCreatedAt(oldProduct.getCreatedAt())
                .withImage(image).withDescription(description).withTags(tags).build();
        Product updatedProduct = productRepository.update(oldProduct, newProduct);
        return new ProductResponseDto(updatedProduct.getId(), updatedProduct.getName(),
                createPriceDtoToResponse(updatedProduct.getPrice()),
                createImageDtoToResponse(updatedProduct.getImage().orElse(null)),
                createDescriptionDtoToResponse(updatedProduct.getDescription().orElse(null)),
                createTagsDtoToResponse(updatedProduct.getTags().orElse(null)));
    }

    private PriceDto createPriceDtoToResponse(Price price){
        return new PriceDto(price.getAmount().toString(), price.getCurrency().getCurrencyCode());
    }

    private DescriptionDto createDescriptionDtoToResponse(Description description){
        if(description != null){
            return new DescriptionDto(description.getText());
        }else return null;
    }

    private ImageDto createImageDtoToResponse(Image image){
        if(image != null){
            return new ImageDto(image.getUrl().toString());
        }else return null;
    }

    private List<TagDto> createTagsDtoToResponse(List<Tag> tags){
        if(tags != null){
            return tags.stream().map(tag -> new TagDto(tag.getName())).collect(Collectors.toList());
        }else return null;
    }

    private List<Tag> getTagsFromRequest(ProductRequestDto productRequest){
        try {
            return productRequest.getTags().stream().map(tag -> Tag.build(tag.getName()))
                    .collect(Collectors.toList());
        }catch (NullPointerException e){
            return null;
        }
    }

    private Description getDescriptionFromRequest(ProductRequestDto productRequest){
        try {
            return Description.build(productRequest.getDescription().getText());
        }catch (NullPointerException e){
            return null;
        }
    }

    private Image getImageFromRequest(ProductRequestDto productRequest){
        try{
            return Image.build(productRequest.getImage().getUrl());
        }catch (NullPointerException e){
            return null;
        }
    }

    private Price getPriceFromRequest(ProductRequestDto productRequest) {
        if(productRequest.getPrice() == null)  throw new ProductPriceIsNullException();
        return Price.build(productRequest.getPrice().getAmount(), productRequest.getPrice().getCurrency());
    }

}