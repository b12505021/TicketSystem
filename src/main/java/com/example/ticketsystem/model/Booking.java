package com.example.ticketsystem.model;

/**
 * Booking 是訂票紀錄的資料模型，封裝了每筆訂票的資訊，
 * 包含座位編號、場次時間、電影名稱、廳號、會員 Email 等欄位。
 */
public class Booking {

    private int bookingId;        // 訂票編號（資料庫主鍵）
    private String seat;          // 座位編號（如 A1, B5）
    private String showTime;      // 放映時間（格式：yyyy-MM-dd HH:mm）
    private String movieName;     // 電影名稱（用於使用者查詢）
    private int hallId;           // 廳號
    private String userEmail;     // 使用者 Email（僅 admin 查詢時顯示）

    /**
     * 使用者端顯示歷史訂票資訊用的建構子。
     *
     * @param bookingId 訂票 ID
     * @param seat      座位編號
     * @param showTime  放映時間
     * @param movieName 電影名稱
     * @param hallId    廳號
     */
    public Booking(int bookingId, String seat, String showTime, String movieName, int hallId) {
        this.bookingId = bookingId;
        this.seat = seat;
        this.showTime = showTime;
        this.movieName = movieName;
        this.hallId = hallId;
    }

    /**
     * 管理員查詢某場次訂票資訊用的建構子。
     *
     * @param bookingId 訂票 ID
     * @param seat      座位編號
     * @param time      放映時間
     * @param userEmail 訂票使用者的 Email
     */
    public Booking(int bookingId, String seat, String time, String userEmail) {
        this.bookingId = bookingId;
        this.seat = seat;
        this.showTime = time;
        this.userEmail = userEmail;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getSeat() {
        return seat;
    }

    public String getShowTime() {
        return showTime;
    }

    public String getMovieName() {
        return movieName;
    }

    public int getHallId() {
        return hallId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    /**
     * 根據不同使用情境，回傳不同的字串：
     * - 使用者查詢時顯示電影名稱與座位
     * - 管理員查詢時顯示使用者 Email 與座位
     */
    @Override
    public String toString() {
        if (movieName != null) {
            return movieName + " @ " + showTime + " | 座位: " + seat + " | 廳: " + hallId;
        } else if (userEmail != null) {
            return userEmail + " 訂 " + seat + " @ " + showTime;
        } else {
            return seat + " @ " + showTime;
        }
    }
}
