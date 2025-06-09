package com.example.ticketsystem.controller;

import com.example.ticketsystem.dao.MovieDAO;
import com.example.ticketsystem.model.Movie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * 控制器：新增電影畫面（營運人員使用）
 */
public class AddMovieController {

    // ====== 對應 FXML 中的元件 ======
    @FXML private TextField nameField;               // 電影名稱輸入框
    @FXML private TextField durationField;           // 片長輸入框（分鐘）
    @FXML private TextArea descriptionArea;          // 電影描述區塊
    @FXML private ComboBox<String> ratingBox;        // 分級選單（普遍級、輔導級...）
    @FXML private Label messageLabel;                // 顯示訊息（成功或錯誤）

    // ====== 資料存取物件 ======
    private final MovieDAO movieDAO = new MovieDAO();

    /**
     * 處理送出新增電影表單邏輯
     */
    @FXML
    private void handleSubmit() {
        try {
            // 從表單取得輸入值
            String name = nameField.getText().trim();
            int duration = Integer.parseInt(durationField.getText().trim());
            String description = descriptionArea.getText().trim();
            String rating = ratingBox.getValue();  // 取出分級選項

            // 驗證基本欄位
            if (name.isEmpty() || rating == null) {
                messageLabel.setText("❗ 電影名稱與分級不可為空");
                return;
            }

            // 將輸入值封裝為 Movie 物件
            Movie movie = new Movie(name, duration, description, rating);

            // 存入資料庫
            movieDAO.insert(movie);

            messageLabel.setText("✅ 新增成功！");
        } catch (NumberFormatException e) {
            messageLabel.setText("❌ 片長請輸入整數");
        } catch (Exception e) {
            messageLabel.setText("❌ 格式錯誤或新增失敗");
            e.printStackTrace();
        }
    }

    /**
     * 返回後台主選單
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin_menu.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
