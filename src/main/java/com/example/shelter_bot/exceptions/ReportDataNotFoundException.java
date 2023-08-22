package com.example.shelter_bot.exceptions;

public class ReportDataNotFoundException extends RuntimeException {
    public ReportDataNotFoundException() {
        super("Дата не найдена");
    }
}