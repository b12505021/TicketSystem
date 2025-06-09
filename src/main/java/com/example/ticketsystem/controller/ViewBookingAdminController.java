package com.example.ticketsystem.controller;

import com.example.ticketsystem.dao.BookingDAO;
import com.example.ticketsystem.model.Booking;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * 管理員查詢某場次訂票紀錄的控制器
 * 輸入場次 ID，顯示對應的所有訂票紀錄（包含座位、時間與用戶 email）
 */
public class ViewBookingAdminController {

    @FXML private TextField scheduleField;  // 輸入場次 ID 欄位
    @FXML private ListView<Booking> resultList; // 顯示查詢結果的列表

    private final BookingDAO dao = new BookingDAO(); // 操作訂票資料的 DAO 物件

    /**
     * 查詢按鈕點擊後執行此方法，從資料庫抓取該場次所有訂票資料
     */
    @FXML
    private void handleQuery() {
        try {
            int scheduleId = Integer.parseInt(scheduleField.getText());
            List<Booking> bookings = dao.getBySchedule(scheduleId); // 取得場次訂票紀錄
            resultList.getItems().setAll(bookings); // 顯示在列表中
        } catch (NumberFormatException e) {
            // 若輸入非數字，顯示錯誤訊息
            resultList.getItems().clear();
            resultList.getItems().add(new Booking(0, "格式錯誤", "", "請輸入數字 ID"));
        }
    }

    /**
     * 返回管理員主選單
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin_menu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) resultList.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
