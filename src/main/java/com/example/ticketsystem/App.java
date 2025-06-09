package com.example.ticketsystem;

import com.example.ticketsystem.util.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        DatabaseInitializer.initialize(); // 初始化 SQLite 資料表
        launch(args); // ✅ 注意這裡是 launch(args)
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Movie Ticket System - Login");
        stage.setScene(scene);
        stage.show();
    }
}
