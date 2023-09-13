package dbs.bankingsystem.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MyJwtTokenExpiredException extends RuntimeException {

    public MyJwtTokenExpiredException(String message) {
        super(message);
    }

    public MyJwtTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

}
