package critterrepos.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

public class StockOptionUtils {
    private Map<String, LocalDate> seriesMap;

    public StockOptionUtils() {
        populate();
    }

    public LocalDate seriesAsDate(String series) {
        String ys = series.substring(0,1);
        String ms = series.substring(1,2);
        int year = 0;
        int month = 0;
        switch (ys) {
            case "0":
                year = 2020;
                break;
            case "1":
                year = 2021;
                break;
            case "2":
                year = 2022;
                break;
            case "3":
                year = 2023;
                break;
            case "4":
                year = 2024;
                break;
        }
        switch (ms) {
            case "A":
            case "M":
                month = 1;
                break;
            case "B":
            case "N":
                month = 2;
                break;
            case "C":
            case "O":
                month = 3;
                break;
            case "D":
            case "P":
                month = 4;
                break;
            case "E":
            case "Q":
                month = 5;
                break;
            case "F":
            case "R":
                month = 6;
                break;
            case "G":
            case "S":
                month = 7;
                break;
            case "H":
            case "T":
                month = 8;
                break;
            case "I":
            case "U":
                month = 9;
                break;
            case "J":
            case "V":
                month = 10;
                break;
            case "K":
            case "W":
                month = 11;
                break;
            case "L":
            case "X":
                month = 12;
                break;
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        if (!startDate.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            startDate = startDate.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        }
        return startDate.plus(Period.ofDays(14));
    }

    private void populate() {
        seriesMap = new HashMap<>();
    }

}
