//package com.example.JsonDataset.exception;
//
//import com.example.JsonDataset.dto.ApiError;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import jakarta.servlet.http.HttpServletRequest;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
//        ApiError err = new ApiError(400, "Bad Request", ex.getMessage(), req.getRequestURI());
//        return ResponseEntity.badRequest().body(err);
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
//        ApiError err = new ApiError(404, "Not Found", ex.getMessage(), req.getRequestURI());
//        return ResponseEntity.stats(HttpStatus.NOT_FOUND).body(err);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
//        // Pick the first validation error message
//        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
//        ApiError err = new ApiError(400, "Bad Request", msg, req.getRequestURI());
//        return ResponseEntity.badRequest().body(err);
//    }
//
//    // (Optional) Catch-all for unexpected exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
//        ApiError err = new ApiError(500, "Internal Server Error", ex.getMessage(), req.getRequestURI());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
//    }
//}
// com/example/JsonDataset/exception/GlobalExceptionHandler.java
package com.example.JsonDataset.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

record ErrorResponse(String error, String message, int status) {}

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badReq(BadRequestException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Bad Request", ex.getMessage(), 400));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> nf(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Not Found", ex.getMessage(), 404));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> beanValid(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst().map(f -> f.getField() + ": " + f.getDefaultMessage())
                .orElse("Validation error");
        return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", msg, 400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generic(Exception ex) {
        return ResponseEntity.status(500).body(new ErrorResponse("Internal Server Error", ex.getMessage(), 500));
    }
}
