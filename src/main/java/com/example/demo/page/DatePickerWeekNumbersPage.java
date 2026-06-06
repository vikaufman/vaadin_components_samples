package com.example.demo.page;

import com.example.demo.DatePickerWeekNumbers;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("date-picker-week-numbers")
public class DatePickerWeekNumbersPage extends Div {
    
    public DatePickerWeekNumbersPage() {
        add(new DatePickerWeekNumbers());
    }
}
