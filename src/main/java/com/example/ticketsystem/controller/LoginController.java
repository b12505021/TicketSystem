package com.example.ticketsystem.controller;

import com.example.ticketsystem.model.User;
import com.example.ticketsystem.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 登入畫面控制器
 * 根據使用者輸入的帳號密碼進行登入判斷，並依身分跳轉對應主畫面
 */
public class LoginController {

    @FXML private TextField emailField;         // 輸入欄位：Email
    @FXML private PasswordField passwordField;  // 輸入欄位：密碼
    @FXML private Label messageLabel;           // 提示訊息顯示區（錯誤 / 成功）

    private final UserService userService = new UserService();  // 使用者驗證服務（連接 DAO）

    /**
     * 按下登入按鈕後執行
     * 驗證帳號密碼並依角色（admin 或一般使用者）導向不同主畫面
     */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            // 嘗試登入
            User user = userService.login(email, password);
            Stage stage = (Stage) emailField.getScene().getWindow(); // 取得目前視窗 Stage

            if ("admin".equals(user.getRole())) {
                // 若為管理者身分，載入後台畫面
                System.out.println("嘗試載入 admin_menu.fxml...");
                System.out.println("實際路徑: " + getClass().getResource("/view/admin_menu.fxml"));

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin_menu.fxml"));
                Scene scene = new Scene(loader.load());

                AdminMenuController controller = loader.getController();
                controller.setUser(user);  // 傳入目前使用者資訊

                stage.setScene(scene);
                stage.show();

            } else {
                // 一般使用者導向主畫面
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_menu.fxml"));
                Scene scene = new Scene(loader.load());

                MainMenuController controller = loader.getController();
                controller.setUser(user);  // 傳入目前使用者資訊

                stage.setScene(scene);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();  // ✅ 顯示詳細錯誤堆疊以利除錯
            messageLabel.setText("登入失敗：" + e.getMessage());  // 顯示錯誤原因
        }
    }

    /**
     * 點選「前往註冊」時觸發，切換至註冊畫面
     */
    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/register.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) emailField.getScene().getWindow();  // 用現有元件取得 Stage
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
