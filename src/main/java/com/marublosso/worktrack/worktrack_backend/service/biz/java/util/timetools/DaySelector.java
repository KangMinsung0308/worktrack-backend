package com.marublosso.worktrack.worktrack_backend.service.biz.java.util.timetools;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class DaySelector {

    public LocalDate Firstday (LocalDate inputDate){

        // 해당 달의 첫째 날
        LocalDate firstDay = inputDate.withDayOfMonth(1);

        // 리스트로 반환 (0 = 첫날, 1 = 마지막날)
        return firstDay;
    }

    public LocalDate Lastday (LocalDate inputDate){

        // 해당 달의 마지막 날
        LocalDate lastDay = inputDate.withDayOfMonth(inputDate.lengthOfMonth());

        // 리스트로 반환 (0 = 첫날, 1 = 마지막날)
        return lastDay;
    }
}
