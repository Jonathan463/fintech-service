package technologyforall.com.virtualaccountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBusinessIdException extends RuntimeException {
    public InvalidBusinessIdException(String message) {
        super(message);
    }
}
