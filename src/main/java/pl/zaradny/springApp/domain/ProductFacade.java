package pl.zaradny.springApp.domain;

import org.springframework.http.ResponseEntity;

public interface ProductFacade {

    ProductResponseDto findById(String id);

    ProductResponseDto create(ProductRequestDto productRequest);

    ProductResponseDto update(String id, ProductRequestDto productRequestDto);

    ResponseEntity<Void> deleteById(String id);

    ProductsResponseDto getAll();
}
