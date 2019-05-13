package pl.zaradny.springApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Product price can not be null")
public class ProductPriceIsNullException extends RuntimeException {
}
