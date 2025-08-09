package org.AlanTitor.Exception;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // В кошельке недостаточно средств
    @ExceptionHandler(LessThanZeroException.class)
    public ResponseEntity<Map<String, String>> onInsufficientFunds(){
        return ResponseEntity.badRequest().body(Map.of("Error!", "You don't have enough money for that operation!"));
    }

    // Кошелек с ID не найден
    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<Map<String, String>> onNotFound(WalletNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error!", "Wallet with that id doesn't exist!"));
    }

    // Ошибка в JSON теле
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> onNotReadableJson(HttpMessageNotReadableException exception){
        return ResponseEntity.badRequest().body(Map.of("Error!", "Can't parse JSON!"));
    }

    // Ошибка в значении тела JSON (ошибка валидации)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> onValidationError(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    // Проверка ID на тип данных
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> onTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        String requiredType = ex.getRequiredType().getSimpleName();
        String message = String.format("Path param '%s' must be '%s'.", paramName, requiredType);
        Map<String, String> error = Map.of("Error!", message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Ошибка при большом колличестве запросов
    @ExceptionHandler(ConcurrencyFailureException.class)
    public BigDecimal recoverOptimisticLock() {
        throw new ConcurrencyFailureException("Couldn't update sum after some tries!");
    }


}
