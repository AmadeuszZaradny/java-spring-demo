package pl.zaradny.springApp.api;

import com.fasterxml.jackson.core.JsonProcessingException;
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
        ProductRequestDto requestDto = new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN")).build();
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
        ProductResponseDto prd1 = productFacade.create(new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN")).build());
        ProductResponseDto prd2 = productFacade.create(new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("150", "EUR")).build());

        //when
        ResponseEntity<ProductsResponseDto> result = httpClient.getForEntity(productsUrl, ProductsResponseDto.class);

        //then
        assertTrue(result.getBody().getProducts().contains(prd1));
        assertTrue(result.getBody().getProducts().contains(prd2));
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
        final ProductRequestDto product = new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN")).build();
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
        final ProductRequestDto product = new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .setImage(new ImageDto("https://via.placeholder.com/150")).build();
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
        final ProductRequestDto product = new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .setDescription(new DescriptionDto("DescriptionDupa")).build();
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
        ProductRequestDto requestDto = new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN")).build();
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
        ProductResponseDto existingProduct = productFacade.create(new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN")).build());
        ProductRequestDto requestDto = new ProductRequestDto
                .ProductRequestDtoBuilder("product2", new PriceDto("100", "PLN")).build();
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
        ProductResponseDto existingProduct = productFacade.create(new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN")).build());
        ProductRequestDto requestDto = new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .setDescription(new DescriptionDto("DescriptionDupa")).build();
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
        ProductResponseDto existingProduct = productFacade.create(new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN")).build());
        ProductRequestDto requestDto = new ProductRequestDto
                .ProductRequestDtoBuilder("product2", new PriceDto("150", "EUR"))
                .setDescription(new DescriptionDto("DescriptionDupa"))
                .setImage(new ImageDto("https://via.placeholder.com/150")).build();
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
    public void shouldNotAddDescriptionToCreatingProductWhenItIsTooLong(){
        //given
        String longDescription = new String(new char[401]).replace('\0', ' ');
        System.out.println(longDescription.length());
        final ProductRequestDto product = new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN"))
                .setDescription(new DescriptionDto(longDescription)).build();
        String productJson = mapToJson(product);
        //when
        ResponseEntity<ProductResponseDto> result = httpClient.postForEntity(productsUrl, getHttpRequest(productJson), ProductResponseDto.class);
        //then
        assertThat(result.getBody().getDescription().getText()).isEqualTo(null);
    }

    @Test
    public void shouldResponse400HttpCodeWhenUpdatesWithBadPriceField(){
        //given
        ProductResponseDto existingProduct = productFacade.create(new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN")).build());
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
    public void shouldResponse404HttpCodeWhenUpdatesNonExistingProduct(){
        //given
        ProductResponseDto existingProduct = productFacade.create(new ProductRequestDto
                .ProductRequestDtoBuilder("product", new PriceDto("100", "PLN")).build());
        productFacade.deleteById(existingProduct.getId());
        ProductRequestDto requestDto = new ProductRequestDto
                .ProductRequestDtoBuilder("product2", new PriceDto("100", "PLN")).build();
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
