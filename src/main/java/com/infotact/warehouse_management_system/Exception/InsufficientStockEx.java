package com.infotact.warehouse_management_system.Exception;

public class InsufficientStockEx extends RuntimeException {
  public InsufficientStockEx(String message) {
    super(message);
  }
}
