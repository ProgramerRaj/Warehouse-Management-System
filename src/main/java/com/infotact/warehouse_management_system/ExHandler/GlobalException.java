package com.infotact.warehouse_management_system.ExHandler;

import com.infotact.warehouse_management_system.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ProductExistsEx.class)
    public ResponseEntity<String> handleProductExistsEx(ProductExistsEx e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodeArgsValidationEx(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().
                forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProductNotFoundEx.class)
    public ResponseEntity<String> handleProductNotFoundEx(ProductNotFoundEx ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(WarehouseExistsEx.class)
    public ResponseEntity<String> handleWarehouseExistsEx(WarehouseExistsEx ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(WarehouseNotFoundEx.class)
    public ResponseEntity<String> handleWarehouseNotFoundEx(WarehouseNotFoundEx ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AisleNotFoundEx.class)
    public ResponseEntity<String> handleAisleNotFoundEx(AisleNotFoundEx ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ZoneNotFoundEx.class)
    public ResponseEntity<String> handleZoneNotFoundEx(ZoneNotFoundEx ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BinExistsEx.class)
    public ResponseEntity<String> handleBinExistsEx(BinExistsEx ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeEx(RuntimeException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
