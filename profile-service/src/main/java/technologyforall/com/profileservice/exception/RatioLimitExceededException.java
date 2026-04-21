package technologyforall.com.profileservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RatioLimitExceededException extends RuntimeException {
    public RatioLimitExceededException(String message) {
        super(message);
    }
}
