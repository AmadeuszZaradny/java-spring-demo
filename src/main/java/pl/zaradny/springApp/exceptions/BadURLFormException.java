package pl.zaradny.springApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "URL form is not correct")
public class BadURLFormException extends RuntimeException {
}
