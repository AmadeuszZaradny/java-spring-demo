package pl.zaradny.springApp.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zaradny.springApp.domain.ProductFacade;
import pl.zaradny.springApp.domain.ProductRequestDto;
import pl.zaradny.springApp.domain.ProductResponseDto;
import pl.zaradny.springApp.domain.ProductsResponseDto;

@RestController
@RequestMapping("/products")
class ProductEndpoint {

    private final ProductFacade productFacade;

    ProductEndpoint(ProductFacade productFacade){
        this.productFacade = productFacade;
    }

    @PostMapping
    ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto){
        return productFacade.create(productRequestDto);
    }

    @GetMapping("/{id}")
    ProductResponseDto getProduct(@PathVariable String id){
        return productFacade.findById(id);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable String id){
        return productFacade.deleteById(id);
    }

    @PutMapping("/{id}")
    ProductResponseDto updateProduct(@PathVariable String id, @RequestBody ProductRequestDto productRequestDto){
        return  productFacade.update(id, productRequestDto);
    }

    @GetMapping
    ProductsResponseDto getAllProducts(){
        return productFacade.getAll();
    }

}
