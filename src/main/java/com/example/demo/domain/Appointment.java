package com.example.demo.domain;

import java.time.LocalDate;

public class Appointment {
    
    private LocalDate startDate;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
