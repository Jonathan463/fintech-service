package technologyforall.com.transferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateTransferException extends RuntimeException {
  public DuplicateTransferException(String message) {
    super(message);
  }
}