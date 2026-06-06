package com.example.demo;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;

public class DatePickerWeekNumbers extends Div {
    public DatePickerWeekNumbers() {
        var datePicker = new DatePicker("Vacation start date");
        datePicker.setWeekNumbersVisible(true);
        datePicker
        .setI18n(new DatePicker.DatePickerI18n().setFirstDayOfWeek(1));
         add(datePicker);
    } 
}
