package com.example.ticketsystem.controller;

import com.example.ticketsystem.dao.MovieDAO;
import com.example.ticketsystem.dao.ScheduleDAO;
import com.example.ticketsystem.model.Movie;
import com.example.ticketsystem.model.Schedule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * 控制器：新增場次（營運人員專用）
 */
public class AddScheduleController {

    // ====== 對應 FXML 的元件 ======
    @FXML private ComboBox<Movie> movieBox;       // 電影下拉選單
    @FXML private ComboBox<Integer> hallBox;      // 廳號下拉選單（1~10）
    @FXML private Label hallTypeLabel;            // 顯示小廳或大廳
    @FXML private TextField datetimeField;        // 放映時間輸入欄（yyyy-MM-dd HH:mm）
    @FXML private Label messageLabel;             // 狀態訊息顯示欄

    // ====== 資料庫操作物件 ======
    private final MovieDAO movieDAO = new MovieDAO();
    private final ScheduleDAO scheduleDAO = new ScheduleDAO();

    /**
     * 初始化表單欄位與下拉選單資料（自動在 FXML 載入後執行）
     */
    @FXML
    private void initialize() {
        // 載入電影清單供選擇
        List<Movie> movies = movieDAO.getAll();
        movieBox.getItems().addAll(movies);

        // 廳型顯示根據選擇的廳號自動更新
        hallBox.setOnAction(e -> {
            Integer id = hallBox.getValue();
            if (id != null) {
                hallTypeLabel.setText((id % 2 == 0) ? "大廳" : "小廳");
            } else {
                hallTypeLabel.setText("請選擇放映廳");
            }
        });
    }

    /**
     * 點擊「送出」按鈕後執行，進行場次資料驗證與新增
     */
    @FXML
    private void handleSubmit() {
        try {
            // 從表單取得使用者輸入的資料
            Movie selected = movieBox.getValue();
            Integer hallId = hallBox.getValue();
            String showTime = datetimeField.getText();

            // 基本欄位驗證
            if (selected == null || hallId == null || showTime.isEmpty()) {
                messageLabel.setText("❗ 請完整填寫所有欄位");
                return;
            }

            // 時間格式驗證（只接受到分鐘，不含秒）
            if (!showTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
                messageLabel.setText("⚠️ 時間格式應為 yyyy-MM-dd HH:mm");
                return;
            }

            // 播放時間衝突檢查
            int duration = selected.getDuration();
            boolean hasConflict = scheduleDAO.isConflict(hallId, showTime, duration, null);
            if (hasConflict) {
                messageLabel.setText("❌ 該時間段已有其他電影在播放");
                return;
            }

            // 根據廳號自動判斷廳型
            String hallType = (hallId % 2 == 0) ? "大廳" : "小廳";

            // 建立 Schedule 物件並填入欄位
            Schedule schedule = new Schedule();
            schedule.setMovieId(selected.getId());
            schedule.setHallId(hallId);
            schedule.setShowTime(showTime);   // 格式為 yyyy-MM-dd HH:mm
            schedule.setHallType(hallType);

            // 儲存進資料庫
            scheduleDAO.insert(schedule);
            messageLabel.setText("✅ 場次新增成功！");
        } catch (Exception e) {
            messageLabel.setText("❌ 發生錯誤，請檢查資料格式");
            e.printStackTrace();
        }
    }

    /**
     * 點擊返回按鈕：返回管理者主選單畫面
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin_menu.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) movieBox.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
