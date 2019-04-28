package pl.zaradny.springApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Description can not be longer than 400 characters")
public class TooLongProductDescriptionException extends RuntimeException{
}