package com.example.menu_electronics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(1002, "Category existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1003, "Category not existed", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_EXISTED(1004, "Product not existed", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(1005, "Product existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    ORDER_NOT_EXISTED(1008, "Order not existed", HttpStatus.NOT_FOUND),
    ORDER_EXISTED(1009, "Order existed", HttpStatus.NOT_FOUND),
    ORDERITEM_NOT_EXISTED(1010, "OrderItem not existed", HttpStatus.NOT_FOUND),
    ORDERITEM_EXISTED(1011, "OrderItem existed", HttpStatus.NOT_FOUND),
    COULD_NOT_READ_FILE(1012, "Could not read file", HttpStatus.BAD_REQUEST),
    FAILED_FILE(1013, "Failed to store empty file", HttpStatus.BAD_REQUEST),
    CANT_STORE_FILE(1014, "Cannot store file outside current directory", HttpStatus.BAD_REQUEST),
    FAILED_STORE_FILE(1015, "Failed to store file", HttpStatus.BAD_REQUEST),
    COULD_NOT_STORAGE(1016, "Could not initialize storage", HttpStatus.BAD_REQUEST),
    FEEDBACK_NOT_EXISTED(1017, "FeedBack not existed", HttpStatus.NOT_FOUND),
    SELECTION_NOT_EXISTED(1018, "Selection not existed", HttpStatus.NOT_FOUND),
    SELECTION_EXISTED(1019, "Selection existed", HttpStatus.NOT_FOUND),
    NOTIFICATION_NOT_EXISTED(1020, "Notification not existed", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTED(1021, "User not existed", HttpStatus.NOT_FOUND),
    USER_EXISTED(1022, "User existed", HttpStatus.NOT_FOUND),
    SEND_EMAIL_ERROR(1023, "Send email error", HttpStatus.BAD_REQUEST),
    TABLE_NOT_EXISTED(1024, "table not existed", HttpStatus.NOT_FOUND),
    TABLE_EXISTED(1025, "table existed", HttpStatus.NOT_FOUND),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
