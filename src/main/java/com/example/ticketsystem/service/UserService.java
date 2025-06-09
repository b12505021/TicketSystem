package com.example.ticketsystem.service;

import com.example.ticketsystem.dao.UserDAO;
import com.example.ticketsystem.exception.AuthException;
import com.example.ticketsystem.model.User;

/**
 * UserService 負責處理與使用者帳號相關的業務邏輯，
 * 如登入驗證與註冊，與資料庫的 UserDAO 互動。
 */
public class UserService {
    private final UserDAO userDAO = new UserDAO();

    /**
     * 使用者登入驗證流程。
     * 檢查是否存在該使用者並比對密碼是否正確。
     *
     * @param email 使用者輸入的 email
     * @param password 使用者輸入的密碼
     * @return 驗證成功後回傳 User 物件
     * @throws AuthException 若使用者不存在或密碼錯誤時拋出
     */
    public User login(String email, String password) throws AuthException {
        User user = userDAO.findByEmail(email);

        if (user == null) {
            throw new AuthException("User not found."); // 使用者不存在
        }

        if (!user.getPassword().equals(password)) {
            throw new AuthException("Incorrect password."); // 密碼不符
        }

        return user; // 驗證成功
    }

    /**
     * 註冊新使用者帳號。
     *
     * @param user 包含註冊資訊的 User 物件
     * @return 是否註冊成功（false 表示 email 已存在）
     */
    public boolean register(User user) {
        return userDAO.insert(user);
    }
}
