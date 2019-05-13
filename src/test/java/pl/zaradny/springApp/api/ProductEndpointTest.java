package pl.zaradny.springApp.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import pl.zaradny.springApp.SpringAppApplicationTests;
import pl.zaradny.springApp.domain.*;
import pl.zaradny.springApp.exceptions.ProductNotFoundException;
import pl.zaradny.springApp.testUtils.ProductRequestDtoBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductEndpointTest extends SpringAppApplicationTests {


    @Autowired
    private ProductFacade productFacade;

    private String productsUrl;

    @Before
    public void init() {
        productsUrl = "http://localhost:" + port + "/products/";
    }

    @Test
    public void shouldGetExistingProduct(){
        //given
        ProductRequestDto requestDto = new ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .build();
        ProductResponseDto existingProduct = productFacade.create(requestDto);
        final String url = productsUrl + existingProduct.getId();
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.getForEntity(url, ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualToComparingFieldByField(existingProduct);
    }

    @Test
    public void shouldGetListOfAllProducts(){
        //given
        ProductResponseDto prd1 = productFacade.create(new ProductRequestDtoBuilder("product",
                new PriceDto("100", "PLN"))
                .build());
        ProductResponseDto prd2 = productFacade.create(new ProductRequestDtoBuilder("product",
                new PriceDto("150", "EUR"))
                .build());

        //when
        ResponseEntity<ProductsResponseDto> result = httpClient.getForEntity(productsUrl, ProductsResponseDto.class);

        //then
        assertTrue(result.getBody().getProducts().contains(prd1));
        assertTrue(result.getBody().getProducts().contains(prd2));
    }

    @Test
    public void shouldGetProductsWithTagFromRequestParam(){
        //given
        ProductResponseDto prd1 = productFacade.create(new ProductRequestDtoBuilder("product",
                new PriceDto("100", "PLN"))
                .withTags(Lists.asList(new TagDto("tag"),new TagDto[0]))
                .build());
        ProductResponseDto prd2 = productFacade.create(new ProductRequestDtoBuilder("product",
                new PriceDto("150", "EUR")).build());
        final String url = productsUrl + "?tag=tag";

        //when
        ResponseEntity<ProductsResponseDto> result = httpClient.getForEntity(url, ProductsResponseDto.class);

        //then
        assertThat(result.getBody().getProducts().size()).isEqualTo(1);
        assertThat(result.getBody().getProducts().get(0).getId()).isEqualTo(prd1.getId());
    }

    @Test
    public void shouldResponse404HttpCodeWhenProductDoesNotExist(){
        //given
        final String url = productsUrl + "emptyDB";
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.getForEntity(url, ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void shouldCreateProductWithRequiredFields(){
        //given
        final ProductRequestDto product = new ProductRequestDtoBuilder("product",
                new PriceDto("100", "PLN"))
                .build();
        String productJson = mapToJson(product);
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.postForEntity(productsUrl, getHttpRequest(productJson), ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody().getName()).isEqualTo("product");
        assertThat(result.getBody().getPrice().getAmount()).isEqualTo("100");
        assertThat(result.getBody().getPrice().getCurrency()).isEqualTo("PLN");
    }

    @Test
    public void shouldCreateProductWithImage(){
        //given
        final ProductRequestDto product = new ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .withImage(new ImageDto("https://via.placeholder.com/150"))
                .build();
        String productJson = mapToJson(product);
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.postForEntity(productsUrl, getHttpRequest(productJson), ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody().getName()).isEqualTo("product");
        assertThat(result.getBody().getPrice().getAmount()).isEqualTo("100");
        assertThat(result.getBody().getPrice().getCurrency()).isEqualTo("PLN");
        assertThat(result.getBody().getImage().getUrl()).isEqualTo("https://via.placeholder.com/150");
    }

    @Test
    public void shouldCreateProductWithDescription(){
        //given
        final ProductRequestDto product = new ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .withDescription(new DescriptionDto("DescriptionDupa"))
                .build();
        String productJson = mapToJson(product);
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.postForEntity(productsUrl, getHttpRequest(productJson), ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody().getName()).isEqualTo("product");
        assertThat(result.getBody().getPrice().getAmount()).isEqualTo("100");
        assertThat(result.getBody().getPrice().getCurrency()).isEqualTo("PLN");
        assertThat(result.getBody().getDescription().getText()).isEqualTo("DescriptionDupa");
    }

    @Test
    public void shouldCreateProductWithTag(){
        //given
        final ProductRequestDto product = new ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .withTags(Lists.asList(new TagDto("tag1"), new TagDto("tag2"), new TagDto[0]))
                .build();
        String productJson = mapToJson(product);
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.postForEntity(productsUrl, getHttpRequest(productJson), ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody().getName()).isEqualTo("product");
        assertThat(result.getBody().getPrice().getAmount()).isEqualTo("100");
        assertThat(result.getBody().getPrice().getCurrency()).isEqualTo("PLN");
        assertTrue(result.getBody().getTags().contains(new TagDto("tag1")));
        assertTrue(result.getBody().getTags().contains(new TagDto("tag2")));
    }

    @Test
    public void shouldCreateProductWithoutRepeatingTags(){
        //given
        final ProductRequestDto product = new ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .withTags(Lists.asList(new TagDto("tag1"), new TagDto("tag1"), new TagDto[0]))
                .build();
        String productJson = mapToJson(product);
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.postForEntity(productsUrl, getHttpRequest(productJson), ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody().getName()).isEqualTo("product");
        assertThat(result.getBody().getPrice().getAmount()).isEqualTo("100");
        assertThat(result.getBody().getPrice().getCurrency()).isEqualTo("PLN");
        assertTrue(result.getBody().getTags().contains(new TagDto("tag1")));
        assertThat(result.getBody().getTags().size()).isEqualTo(1);
    }

    @Test
    public void shouldResponse400HttpCodeWhenCreatesWithEmptyTagName(){
        //given
        final ProductRequestDto product = new ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .withTags(Lists.asList(new TagDto(""), new TagDto[0]))
                .build();
        String productJson = mapToJson(product);
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.postForEntity(productsUrl, getHttpRequest(productJson), ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void shouldResponse400HttpCodeWhenCreatesWithEmptyPriceFields(){
        //given
        final String json = jsonWithoutPriceField("product");
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.postForEntity(productsUrl, getHttpRequest(json), ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void shouldDeleteExistingProduct(){
        //given
        ProductRequestDto requestDto = new ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .build();
        ProductResponseDto existingProduct = productFacade.create(requestDto);
        final String url = productsUrl + existingProduct.getId();
        //when
        ResponseEntity<Void> result = httpClient.exchange(url,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class);
        httpClient.delete(url);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(204);
        assertThrows( ProductNotFoundException.class, () -> productFacade.findById(existingProduct.getId()));
    }

    @Test
    public void shouldResponse404HttpCodeWhenDeletesNonExistingProduct(){
        //given
        final String url = productsUrl + "emptyDB";
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.exchange(url,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                ProductResponseDto.class );
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void shouldUpdateNameOfExistingProduct(){
        //given
        ProductResponseDto existingProduct = productFacade.create(new ProductRequestDtoBuilder("product",
                new PriceDto("100", "PLN"))
                .build());
        ProductRequestDto requestDto = new ProductRequestDtoBuilder("product2",
                new PriceDto("100", "PLN"))
                .build();
        final String productJson = mapToJson(requestDto);
        final String url = productsUrl + existingProduct.getId();
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.exchange(url,
                HttpMethod.PUT,
                getHttpRequest(productJson),
                ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody().getId()).isEqualTo(existingProduct.getId());
        assertThat(result.getBody().getName()).isEqualTo(requestDto.getName());
    }

    @Test
    public void shouldUpdateDescriptionOfExistingProduct(){
        //given
        ProductResponseDto existingProduct = productFacade.create(new ProductRequestDtoBuilder("product",
                new PriceDto("100", "PLN"))
                .build());
        ProductRequestDto requestDto = new ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .withDescription(new DescriptionDto("DescriptionDupa"))
                .build();
        final String productJson = mapToJson(requestDto);
        final String url = productsUrl + existingProduct.getId();
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.exchange(url,
                HttpMethod.PUT,
                getHttpRequest(productJson),
                ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody().getId()).isEqualTo(existingProduct.getId());
        assertThat(result.getBody().getDescription().getText()).isEqualTo(requestDto.getDescription().getText());
    }

    @Test
    public void shouldUpdateNamePriceImageAndDescriptionOfExistingProduct(){
        //given
        ProductResponseDto existingProduct = productFacade.create(new ProductRequestDtoBuilder("product",
                new PriceDto("100", "PLN")).build());
        ProductRequestDto requestDto = new ProductRequestDtoBuilder("product2",
                new PriceDto("150", "EUR"))
                .withDescription(new DescriptionDto("DescriptionDupa"))
                .withImage(new ImageDto("https://via.placeholder.com/150")).build();
        final String productJson = mapToJson(requestDto);
        final String url = productsUrl + existingProduct.getId();
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.exchange(url,
                HttpMethod.PUT,
                getHttpRequest(productJson),
                ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody().getId()).isEqualTo(existingProduct.getId());
        assertThat(result.getBody().getName()).isEqualTo(requestDto.getName());
        assertThat(result.getBody().getPrice().getAmount()).isEqualTo(requestDto.getPrice().getAmount());
        assertThat(result.getBody().getPrice().getCurrency()).isEqualTo(requestDto.getPrice().getCurrency());
        assertThat(result.getBody().getImage().getUrl()).isEqualTo(requestDto.getImage().getUrl());
        assertThat(result.getBody().getDescription().getText()).isEqualTo(requestDto.getDescription().getText());
    }

    @Test
    public void shouldResponse400HttpCodeWhenDescriptionIsTooLong(){
        //given
        String longDescription = new String(new char[401]).replace('\0', ' ');
        System.out.println(longDescription.length());
        final ProductRequestDto product = new ProductRequestDtoBuilder("product",
                new PriceDto("100", "PLN"))
                .withDescription(new DescriptionDto(longDescription))
                .build();
        String productJson = mapToJson(product);
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.postForEntity(productsUrl, getHttpRequest(productJson), ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void shouldResponse400HttpCodeWhenUpdatesWithBadPriceField(){
        //given
        ProductResponseDto existingProduct = productFacade.create(new ProductRequestDtoBuilder("product",
                new PriceDto("100", "PLN"))
                .build());
        final String json = jsonWithBadPriceField("10.00");
        final String url = productsUrl + existingProduct.getId();
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.exchange(url,
                HttpMethod.PUT,
                getHttpRequest(json),
                ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void shouldResponse400HttpCodeWhenURLIsNotCorrect(){
        //given
        ProductRequestDto productRequestDto = new ProductRequestDtoBuilder("product",
                new PriceDto("100", "PLN"))
                .withImage(new ImageDto("siema")).build();
        String productJson = mapToJson(productRequestDto);
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.postForEntity(productsUrl, getHttpRequest(productJson), ProductResponseDto.class);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void shouldResponse404HttpCodeWhenUpdatesNonExistingProduct(){
        //given
        ProductResponseDto existingProduct = productFacade.create(new ProductRequestDtoBuilder("product",
                new PriceDto("100", "PLN"))
                .build());
        productFacade.deleteById(existingProduct.getId());
        ProductRequestDto requestDto = new ProductRequestDtoBuilder("product2",
                new PriceDto("100", "PLN"))
                .build();
        final String productJson = mapToJson(requestDto);
        final String url = productsUrl + existingProduct.getId();
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.exchange(url,
                HttpMethod.PUT,
                getHttpRequest(productJson),
                ProductResponseDto.class);
        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(404);
    }

    String mapToJson (ProductRequestDto productRequestDto){
        try {
            return objectMapper.writeValueAsString(productRequestDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpEntity<String> getHttpRequest(String json){
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.set("content-type","application/json");
        return new HttpEntity<>(json, httpHeaders);
    }

    String jsonWithoutPriceField (String name){
        return "{ \"name\": \""+ name +"\" }";
    }

    String jsonWithBadPriceField (String amount){
        return "{ " +
                "\"price\":" + "{" +
                "\"amount\":  \""+ amount +"\"" +
                "}" +
                " }";
    }
}