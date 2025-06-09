package com.example.ticketsystem.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * User 類別代表一位使用者的基本資料，包括帳號、密碼、生日與身分角色。
 * 可透過 getAge() 自動計算使用者年齡，供分級驗證使用。
 */
public class User {
    private int uid;             // 使用者 ID（資料庫主鍵）
    private String email;        // 使用者帳號（信箱）
    private String password;     // 使用者密碼（簡化處理，實務應加密）
    private String birthdate;    // 使用者生日（格式 "yyyy-MM-dd"）
    private String role;         // 使用者角色（"user" 或 "admin"）

    /**
     * 全參數建構子，通常由 DAO 從資料庫取出使用。
     *
     * @param uid       使用者 ID
     * @param email     信箱
     * @param password  密碼
     * @param birthdate 出生年月日（yyyy-MM-dd）
     * @param role      使用者角色
     */
    public User(int uid, String email, String password, String birthdate, String role) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.role = role;
    }

    /**
     * 建構註冊時的新使用者，預設角色為 "user"。
     */
    public User(String email, String password, String birthdate) {
        this(-1, email, password, birthdate, "user");
    }

    // === Getter methods ===
    public int getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getRole() {
        return role;
    }

    /**
     * 根據 birthdate 計算目前年齡，失敗時回傳 0。
     */
    public int getAge() {
        try {
            LocalDate birth = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return Period.between(birth, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0; // 若生日格式錯誤則回傳 0
        }
    }
}
