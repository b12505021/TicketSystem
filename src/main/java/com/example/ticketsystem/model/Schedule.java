package com.example.ticketsystem.model;

/**
 * Schedule 類別代表一個電影場次的資訊，
 * 包含電影 ID、播放廳 ID、播放時間與電影名稱等資料。
 */
public class Schedule {
    private int id;               // 場次 ID（資料庫主鍵）
    private int movieId;          // 對應的電影 ID
    private int hallId;           // 放映廳 ID（1~10）
    private String showTime;      // 播放時間（建議格式為 "yyyy-MM-dd HH:mm"）
    private String movieName;     // 電影名稱（for 顯示用）
    private String hallType;      // 廳型（"小廳" 或 "大廳"）

    /**
     * 全參數建構子，常用於從資料庫撈取資料後建立 Schedule 物件。
     *
     * @param id         場次 ID
     * @param movieId    電影 ID
     * @param hallId     廳 ID
     * @param showTime   播放時間
     * @param movieName  電影名稱
     */
    public Schedule(int id, int movieId, int hallId, String showTime, String movieName) {
        this.id = id;
        this.movieId = movieId;
        this.hallId = hallId;
        this.showTime = showTime;
        this.movieName = movieName;
    }

    /**
     * 空建構子，常用於手動設定欄位後進行 insert 操作。
     */
    public Schedule() {}

    // Getter / Setter methods
    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getHallType() {
        return hallType;
    }

    public void setHallType(String hallType) {
        this.hallType = hallType;
    }

    /**
     * 顯示用格式：電影名稱 + 播放時間 + 廳 ID
     */
    @Override
    public String toString() {
        return movieName + " @ " + showTime + " (廳 " + hallId + ")";
    }
}
