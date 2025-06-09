package com.example.ticketsystem.model;

/**
 * Movie 類別代表一部電影，包含電影基本資訊如名稱、時長、描述與分級。
 */
public class Movie {
    private int id;              // 電影在資料庫中的主鍵 ID
    private String name;         // 電影名稱
    private int duration;        // 片長（分鐘）
    private String description;  // 電影簡介
    private String rating;       // 分級（如：普遍級、限制級等）

    /**
     * 建構子（用於新增電影時）
     *
     * @param name        電影名稱
     * @param duration    電影片長（分鐘）
     * @param description 電影簡介
     * @param rating      分級
     */
    public Movie(String name, int duration, String description, String rating) {
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.rating = rating;
    }

    /**
     * 建構子（資料庫查詢後，只有部分欄位可用時）
     *
     * @param id          電影 ID
     * @param name        電影名稱
     * @param description 電影簡介
     * @param rating      分級
     */
    public Movie(int id, String name, String description, String rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
    }

    /**
     * 建構子（完整欄位）
     *
     * @param id          電影 ID
     * @param name        電影名稱
     * @param duration    電影片長
     * @param description 電影簡介
     * @param rating      分級
     */
    public Movie(int id, String name, int duration, String description, String rating) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.rating = rating;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    /**
     * 在 ListView 顯示時會用此方法顯示電影名稱。
     */
    @Override
    public String toString() {
        return name;
    }
}
