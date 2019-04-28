package pl.zaradny.springApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Price is not correct")
public class BadPriceException extends RuntimeException {
}