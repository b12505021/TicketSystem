package com.example.ticketsystem.util;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.function.Consumer;

/**
 * SeatLayoutFactory 是一個工具類別，用來根據指定放映廳名稱產生對應的座位配置。
 * 本類別目前實作的是「大廳」的標準排版，包含行列標籤與區塊上色。
 */
public class SeatLayoutFactory {

    /**
     * 建立一個 GridPane 作為座位圖。
     *
     * @param hallName        廳別名稱（目前僅用於標示，可擴充）
     * @param onSeatClick     點擊座位時的處理方法（Consumer<Button>）
     * @param disabledSeats   已被預訂或應禁用的座位清單（以 seatId 命名，如 A3）
     * @return 建構完成的 GridPane，可直接嵌入 JavaFX 畫面中
     */
    public static GridPane createLayout(String hallName, Consumer<Button> onSeatClick, List<String> disabledSeats) {
        GridPane grid = new GridPane();
        int rows = 13;   // A–M 對應 13 列
        int cols = 18;   // 1–18 對應 18 欄

        // 👉 建立欄標籤（橫向 1–18）於第 0 列
        for (int col = 0; col < cols; col++) {
            Label header = new Label(String.valueOf(col + 1));
            header.setStyle("-fx-font-weight: bold;");
            grid.add(header, col + 1, 0);  // col+1: 第 0 欄預留給行標籤
        }

        // 👉 建立行標籤與實際座位按鈕（A–M x 1–18）
        for (int row = 0; row < rows; row++) {
            char rowChar = (char) ('A' + row);

            // 行標籤放在第 0 欄
            Label rowLabel = new Label(String.valueOf(rowChar));
            rowLabel.setStyle("-fx-font-weight: bold;");
            grid.add(rowLabel, 0, row + 1);  // row+1: 第 0 列為欄標籤

            for (int col = 0; col < cols; col++) {
                String seatId = rowChar + String.valueOf(col + 1);
                Button seat = new Button(seatId);
                seat.setPrefWidth(45);  // 座位寬度
                seat.setPrefHeight(30); // 座位高度

                // 座位狀態：已被預訂 → 灰色禁用
                if (disabledSeats.contains(seatId)) {
                    seat.setDisable(true);
                    seat.setStyle("-fx-background-color: grey;");
                } else {
                    // 區塊上色：左/中/右 區域
                    seat.setStyle(getStyleByRegion(col));
                    seat.setOnAction(e -> onSeatClick.accept(seat));
                }

                // 插入座位按鈕至 (col+1, row+1)，避開邊界標籤列/欄
                grid.add(seat, col + 1, row + 1);
            }
        }

        return grid;
    }

    /**
     * 根據欄位編號給予座位按鈕顏色（可視為座位區域的顏色區分）。
     *
     * @param col 欄號（從 0 開始）
     * @return 對應區域的背景色樣式字串
     */
    private static String getStyleByRegion(int col) {
        if (col < 6) return "-fx-background-color: #87CEEB;";     // 左區（藍色）
        if (col > 11) return "-fx-background-color: #F08080;";    // 右區（紅色）
        return "-fx-background-color: #FFD580;";                  // 中間（橘色）
    }
}
