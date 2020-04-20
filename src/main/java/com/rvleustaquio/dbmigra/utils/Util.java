package com.rvleustaquio.dbmigra.utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Util {
    public static String getCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        return dtf.format(now);
    }
}