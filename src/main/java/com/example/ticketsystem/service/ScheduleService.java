package com.example.ticketsystem.service;

import com.example.ticketsystem.dao.ScheduleDAO;
import com.example.ticketsystem.model.Schedule;

import java.util.List;

/**
 * ScheduleService 是場次資料的服務層，負責與 DAO 溝通取得場次清單。
 * 若未來有進階邏輯（例如時間格式轉換、權限過濾等）可集中寫在此層。
 */
public class ScheduleService {
    private final ScheduleDAO dao = new ScheduleDAO();

    /**
     * 取得所有電影場次（含電影名稱、廳別與時間等資訊）。
     *
     * @return 所有場次的列表
     */
    public List<Schedule> getAllSchedules() {
        return dao.getAll();
    }
}
