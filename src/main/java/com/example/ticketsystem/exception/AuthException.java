package com.example.ticketsystem.exception;

/**
 * AuthException 是一個自訂例外類別，表示使用者認證失敗的狀況，
 * 例如：登入失敗、帳號不存在、密碼錯誤等。
 */
public class AuthException extends Exception {

    /**
     * 建構子，接收錯誤訊息並傳遞給父類別 Exception。
     * @param message 錯誤訊息，例如 "帳號或密碼錯誤"
     */
    public AuthException(String message) {
        super(message);
    }
}
