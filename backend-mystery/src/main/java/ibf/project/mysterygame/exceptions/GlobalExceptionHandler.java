package ibf.project.mysterygame.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({AppException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleAppException(AppException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(ex.getStatus())
            .body(new ErrorMessage(ex.getMessage()));
    }
}
