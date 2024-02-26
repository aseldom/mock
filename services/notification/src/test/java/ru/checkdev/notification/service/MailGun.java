package ru.checkdev.notification.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MailGun {

    public static void main(String[] args) {
        try {
            System.out.println(new SimpleDateFormat("d MMM y, HH:mm", new Locale("ru")).parse("8 авг 18, 05:15"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
