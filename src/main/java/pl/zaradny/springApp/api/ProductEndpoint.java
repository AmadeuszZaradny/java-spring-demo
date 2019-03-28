package pl.zaradny.springApp.api;

import org.springframework.web.bind.annotation.*;
import pl.zaradny.springApp.domain.ProductFacade;
import pl.zaradny.springApp.domain.ProductRequestDto;
import pl.zaradny.springApp.domain.ProductResponseDto;

@RestController
@RequestMapping("/products")
public class ProductEndpoint {

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
    ProductResponseDto deleteProduct(@PathVariable String id){
        return productFacade.deleteById(id);
    }

    @PutMapping("/{id}")
    ProductResponseDto updateProduct(@PathVariable String id, @RequestBody ProductRequestDto productRequestDto){
        return  productFacade.updateById(id, productRequestDto);
    }

}
