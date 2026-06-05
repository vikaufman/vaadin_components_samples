package com.example.demo;

// import com.vaadin.flow.component.button.Button;
// import com.vaadin.flow.component.Key;
// import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
//import com.vaadin.flow.component.html.Image;
//import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
// import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
//import com.vaadin.flow.signals.local.ListSignal;
//import com.vaadin.flow.dom.Element;
//import com.vaadin.flow.server.streams.DownloadHandler;
//import java.io.IOException;
//import java.lang.Math;
import com.vaadin.flow.signals.local.ValueSignal;

@Route("")
public class MainView extends VerticalLayout {
    ValueSignal<String> nameSignal = new ValueSignal<>("");
    public MainView() {
        add(new H1("Hello, Vaadin!"));
        TextField nameField = new TextField("Your name");
        nameField.setValueChangeMode(ValueChangeMode.EAGER);
        nameField.bindValue(nameSignal, nameSignal::set);
        Span greeting = new Span(() -> "Hello, " + nameSignal.get() + "!");
        greeting.bindVisible(() -> !nameSignal.get().isBlank());
        add(nameField, greeting);  
      
    }    
}
