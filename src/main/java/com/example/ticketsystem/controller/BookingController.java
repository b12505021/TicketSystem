package com.example.ticketsystem.controller;

import com.example.ticketsystem.dao.BookingDAO;
import com.example.ticketsystem.dao.MovieDAO;
import com.example.ticketsystem.model.Movie;
import com.example.ticketsystem.model.Schedule;
import com.example.ticketsystem.model.User;
import com.example.ticketsystem.util.SeatLayoutFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.*;

/**
 * 負責處理使用者選位與訂票的控制器
 */
public class BookingController {

    @FXML private GridPane seatGrid;           // 座位表區域
    @FXML private Label messageLabel;          // 顯示訊息（成功/錯誤）
    @FXML private Label titleLabel;            // 顯示場次標題資訊

    private final BookingDAO bookingDAO = new BookingDAO();           // 訂票 DAO
    private final Set<String> selectedSeats = new HashSet<>();        // 使用者目前選擇的座位
    private User currentUser;                                         // 當前登入的使用者

    private static final int ROWS = 5;         // 小廳座位行數
    private static final int COLS = 5;         // 小廳座位列數

    // 設定使用者資訊
    public void setUser(User user) {
        this.currentUser = user;
    }

    /**
     * 初始化方法，在載入畫面時自動呼叫
     */
    @FXML
    private void initialize() {
        Schedule schedule = SelectScheduleController.getSelectedSchedule();  // 取得目前選定場次
        if (schedule == null) {
            messageLabel.setText("⚠️ 錯誤：未選擇場次");
            return;
        }

        // 查詢已被訂走的座位
        Set<String> bookedSeats = bookingDAO.getBookedSeats(schedule.getId());
        String hallType = schedule.getHallType();
        titleLabel.setText("目前場次：" + hallType + "（場次ID " + schedule.getId() + "）");

        // 根據廳型載入不同座位表
        if ("小廳".equals(hallType)) {
            loadSmallHallSeats(bookedSeats);
        } else {
            GridPane layout = SeatLayoutFactory.createLayout(hallType, this::toggleSelect, new ArrayList<>(bookedSeats));
            seatGrid.getChildren().setAll(layout.getChildren());
        }
    }

    /**
     * 顯示小廳座位圖並標記已訂位
     */
    private void loadSmallHallSeats(Set<String> bookedSeats) {
        seatGrid.getChildren().clear();
        selectedSeats.clear();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                String seatId = (char) ('A' + i) + String.valueOf(j + 1);
                Button seat = new Button(seatId);
                seat.setPrefSize(45, 35);

                if (bookedSeats.contains(seatId)) {
                    seat.setDisable(true);
                    seat.setStyle("-fx-background-color: grey;");
                } else {
                    seat.setOnAction(e -> toggleSelect(seat));
                }

                seatGrid.add(seat, j, i);
            }
        }
    }

    /**
     * 切換座位選取與取消選取的狀態
     */
    private void toggleSelect(Button seat) {
        String seatId = seat.getText();
        if (selectedSeats.contains(seatId)) {
            seat.setStyle("");
            selectedSeats.remove(seatId);
        } else {
            seat.setStyle("-fx-background-color: lightgreen;");
            selectedSeats.add(seatId);
        }
    }

    /**
     * 檢查會員年齡是否符合電影分級限制
     */
    private boolean isAgeAllowed(int age, String rating) {
        return switch (rating) {
            case "普遍級" -> true;
            case "保護級" -> age >= 6;
            case "輔12級" -> age >= 12;
            case "輔15級" -> age >= 15;
            case "限制級" -> age >= 18;
            default -> true; // 若未定義等級則預設允許
        };
    }

    /**
     * 送出訂票動作
     */
    @FXML
    private void handleSubmit() {
        if (currentUser == null) {
            messageLabel.setText("⚠️ 未登入，無法訂票");
            return;
        }

        if (selectedSeats.isEmpty()) {
            messageLabel.setText("請選擇至少一個座位");
            return;
        }

        Schedule schedule = SelectScheduleController.getSelectedSchedule();
        if (schedule == null) {
            messageLabel.setText("⚠️ 尚未選擇場次");
            return;
        }

        // 查詢電影資料以判斷年齡限制
        String movieName = schedule.getMovieName();
        MovieDAO movieDAO = new MovieDAO();
        Movie movie = movieDAO.findByName(movieName);  // findByName 方法需已實作

        // 驗證使用者是否符合電影的觀看年齡限制
        if (!isAgeAllowed(currentUser.getAge(), movie.getRating())) {
            messageLabel.setText("❌ 您的年齡不符合該電影的分級要求");
            return;
        }

        // 插入訂票資料
        for (String seat : selectedSeats) {
            bookingDAO.insert(currentUser.getUid(), schedule.getId(), seat);
        }

        messageLabel.setText("✅ 訂票成功！");
        selectedSeats.clear();

        // 更新座位圖：灰色標記已被訂走的座位
        Set<String> bookedSeats = bookingDAO.getBookedSeats(schedule.getId());
        if ("小廳".equals(schedule.getHallType())) {
            loadSmallHallSeats(bookedSeats);
        } else {
            GridPane layout = SeatLayoutFactory.createLayout(
                    schedule.getHallType(), this::toggleSelect, new ArrayList<>(bookedSeats)
            );
            seatGrid.getChildren().setAll(layout.getChildren());
        }
    }

    /**
     * 返回場次選擇畫面
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/select_schedule.fxml"));
            Scene scene = new Scene(loader.load());

            SelectScheduleController controller = loader.getController();
            controller.setUser(currentUser);

            Stage stage = (Stage) seatGrid.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
