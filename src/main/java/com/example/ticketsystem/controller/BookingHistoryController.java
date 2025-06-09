package com.example.ticketsystem.controller;

import com.example.ticketsystem.dao.BookingDAO;
import com.example.ticketsystem.model.Booking;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 負責顯示使用者的歷史訂票紀錄，並提供退票功能
 */
public class BookingHistoryController {

    @FXML private ListView<Booking> bookingList;   // 訂票紀錄列表
    private final BookingDAO dao = new BookingDAO();  // 資料存取物件

    /**
     * 初始化方法，畫面載入時自動呼叫
     */
    @FXML
    private void initialize() {
        // 確認目前有登入的使用者
        if (MainMenuController.getCurrentUser() == null) {
            System.err.println("⚠️ BookingHistoryController: 無登入使用者！");
            return;
        }

        // 從資料庫取得該使用者的訂票紀錄
        List<Booking> bookings = dao.getByUser(MainMenuController.getCurrentUser().getUid());
        bookingList.getItems().addAll(bookings);

        // 自訂 ListView 的每個 cell，加入「退票」按鈕
        bookingList.setCellFactory(param -> new ListCell<>() {
            private final Button cancelButton = new Button("退票");

            {
                cancelButton.setOnAction(event -> {
                    Booking booking = getItem();
                    // 檢查是否可退票（需在開演前 30 分鐘以前）
                    if (!isCancelable(booking.getShowTime())) {
                        showAlert("無法退票", "已超過退票時間（需提前 30 分鐘）");
                        return;
                    }

                    // 執行退票動作
                    boolean success = dao.delete(booking.getBookingId());
                    if (success) {
                        bookingList.getItems().remove(booking);
                        showAlert("成功", "已成功退票");
                    } else {
                        showAlert("失敗", "退票失敗");
                    }
                });
            }

            @Override
            protected void updateItem(Booking booking, boolean empty) {
                super.updateItem(booking, empty);
                if (empty || booking == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(booking.toString());     // 顯示格式定義在 Booking 的 toString()
                    setGraphic(cancelButton);        // 顯示退票按鈕
                }
            }
        });
    }

    /**
     * 檢查是否符合可退票的條件（開演前至少 30 分鐘）
     */
    private boolean isCancelable(String showTime) {
        try {
            // 將字串轉成時間格式
            LocalDateTime show = LocalDateTime.parse(showTime.replace(" ", "T"));
            return LocalDateTime.now().isBefore(show.minusMinutes(30));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 顯示訊息彈窗
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * 返回主選單
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_menu.fxml"));
            Scene scene = new Scene(loader.load());

            MainMenuController controller = loader.getController();
            controller.setUser(MainMenuController.getCurrentUser());

            Stage stage = (Stage) bookingList.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
