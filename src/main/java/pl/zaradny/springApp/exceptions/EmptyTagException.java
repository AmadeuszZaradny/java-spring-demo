package pl.zaradny.springApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Tag can not be empty")
public class EmptyTagException extends RuntimeException {
}
