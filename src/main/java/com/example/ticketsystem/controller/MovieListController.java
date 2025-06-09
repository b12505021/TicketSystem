package com.example.ticketsystem.controller;

import com.example.ticketsystem.dao.MovieDAO;
import com.example.ticketsystem.model.Movie;
import com.example.ticketsystem.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * 瀏覽電影清單的控制器
 * 提供所有使用者查閱電影名稱、簡介與分級資訊，並可導向訂票流程
 */
public class MovieListController {

    @FXML
    private ListView<Movie> movieList;  // 顯示電影的 ListView

    private final MovieDAO movieDAO = new MovieDAO();  // DAO 負責與資料庫溝通

    /**
     * 畫面初始化時載入電影清單並設定顯示樣式
     */
    @FXML
    private void initialize() {
        if (movieList == null) {
            System.err.println("❌ movieList 為 null，請檢查 FXML 的 fx:id");
            return;
        }

        // 將所有電影加入 ListView
        movieList.getItems().addAll(movieDAO.getAll());

        // 自訂每筆顯示方式：名稱、分級、簡介
        movieList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                } else {
                    setText(movie.getName() + " | " + movie.getRating() + "\n" + movie.getDescription());
                }
            }
        });
    }

    /**
     * 點擊「前往訂票」按鈕時，切換至選擇場次頁面
     */
    @FXML
    private void goToBooking() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/select_schedule.fxml"));
            Scene scene = new Scene(loader.load());

            // 設定來源為 MOVIE_LIST 並傳遞目前登入者
            SelectScheduleController controller = loader.getController();
            controller.setUser(MainMenuController.getCurrentUser());
            controller.setSource(SelectScheduleController.SourcePage.MOVIE_LIST);
            controller.setFilterMovie(null); // 可根據需要過濾特定電影

            // 切換場景
            Stage stage = (Stage) movieList.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            Stage stage = (Stage) movieList.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
