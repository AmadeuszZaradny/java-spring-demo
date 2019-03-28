package pl.zaradny.springApp.domain;

public interface ProductFacade {

    //get
    ProductResponseDto findById(String id);

    //create
    ProductResponseDto create(ProductRequestDto productRequest);

    //update
    ProductResponseDto updateById(String id, ProductRequestDto productRequestDto);

    //delete
    ProductResponseDto deleteById(String id);
}
