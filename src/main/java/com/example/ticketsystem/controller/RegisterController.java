package com.example.ticketsystem.controller;

import com.example.ticketsystem.model.User;
import com.example.ticketsystem.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Period;

/**
 * 註冊畫面控制器
 * 處理使用者輸入資料並呼叫服務層進行註冊
 */
public class RegisterController {

    // === 與畫面元件綁定的欄位 ===
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField birthField;
    @FXML private Label messageLabel;

    private final UserService userService = new UserService(); // 使用者服務層，處理邏輯與 DAO 呼叫

    /**
     * 處理註冊按鈕點擊事件
     */
    @FXML
    private void handleRegister() {
        String email = emailField.getText();           // 取得信箱輸入
        String password = passwordField.getText();     // 取得密碼輸入
        String birth = birthField.getText();           // 取得生日輸入 (格式 yyyy-MM-dd)

        try {
            // ✅ 驗證 email 格式
            if (!email.contains("@")) {
                messageLabel.setText("Email 格式錯誤");
                return;
            }

            // ✅ 轉換生日字串為 LocalDate 並計算年齡
            LocalDate birthdate = LocalDate.parse(birth);
            int age = Period.between(birthdate, LocalDate.now()).getYears();

//            // ✅ 可選限制：最小年齡限制為 12 歲
//            if (age < 12) {
//                messageLabel.setText("年齡需滿 12 歲才能註冊");
//                return;
//            }

            // ✅ 建立使用者物件並送出註冊
            User user = new User(email, password, birth);
            boolean success = userService.register(user);

            if (success) {
                messageLabel.setText("✅ 註冊成功！");
                goToLogin(); // 自動導回登入頁面
            } else {
                messageLabel.setText("❌ 此 Email 已被註冊");
            }

        } catch (Exception e) {
            messageLabel.setText("❌ 輸入格式錯誤，請確認資料正確");
        }
    }

    /**
     * 點擊「返回登入」時切換回 login 畫面
     */
    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
