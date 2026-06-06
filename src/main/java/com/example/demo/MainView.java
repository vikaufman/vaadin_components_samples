package com.example.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.example.demo.domain.Appointment;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;
import com.vaadin.flow.data.binder.Binder;
import java.time.LocalDate;
import java.time.ZoneId;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.local.ValueSignal;
import com.vaadin.flow.signals.shared.SharedListSignal;
import com.vaadin.flow.component.icon.VaadinIcon;

@Route("")
public class MainView extends VerticalLayout {
    private final ValueSignal<String> createTaskSignal = new ValueSignal<>("");
    
    private static final SharedListSignal<Todo> todos = new SharedListSignal<>(Todo.class);
    
    public MainView(TodoRepo repo) {
        add(new H1("Hello, Vaadin!"));

        if (todos.peek().isEmpty()) {
            repo.findAll().forEach(todos::insertLast);
        }

        TextField createTaskField = new TextField("Task");
        //<theme-editor-local-classname>
        createTaskField.addClassName("main-view-text-field-1");
        createTaskField.setValueChangeMode(ValueChangeMode.EAGER);        
        createTaskField.bindValue(createTaskSignal, createTaskSignal::set);
        
        Button createTaskButton = new Button("Create Task");
        LocalDate now = LocalDate.now(ZoneId.systemDefault());

        DatePicker datePicker = new DatePicker("Appointment Date");
        datePicker.setRequiredIndicatorVisible(true);
        datePicker.setMin(now);
        datePicker.setMax(now.plusDays(60));
        datePicker.setHelperText("Must be within 60 days from today");
        
        datePicker.setI18n(new DatePickerI18n()
                .setBadInputErrorMessage("Invalid date format")
                .setRequiredErrorMessage("Field  is required")
                .setMinErrorMessage("Too early, choose another date")
                .setMaxErrorMessage("Too late,  choose another date"));
        
        DatePicker datePicker1 = new DatePicker("Meeting Date");        
        datePicker1.setHelperText("Mondays - Fridays only");
        datePicker1.setI18n(new DatePickerI18n()
                .setBadInputErrorMessage("Invalid date format"));   
        Binder<Appointment> binder = new Binder <> (Appointment.class);
        binder.forField(datePicker1)
                .withValidator(localDate -> {
                 int dayOfWeek =  localDate.getDayOfWeek().getValue();
                 boolean validWeekDay = dayOfWeek >= 1 && dayOfWeek <= 5; 
                 return validWeekDay;
                }, "Please select a weekday")
                .bind(Appointment::getStartDate, Appointment::setStartDate);


        createTaskButton.addClickListener(click -> {
            Todo todo = new Todo();
            todo.setTask(createTaskSignal.peek());
            repo.save(todo);
            todos.insertLast(todo);
            createTaskSignal.set("");      
        });
        createTaskButton.addClickShortcut(Key.ENTER).listenOn(createTaskField);                
        createTaskButton.bindEnabled(() -> !createTaskSignal.get().isBlank());

        VerticalLayout todosLayout = new VerticalLayout();
        todosLayout.bindChildren(todos, todoSignal -> {
         ValueSignal<Boolean> editing = new ValueSignal<>(false);

            Signal <Boolean> doneSignal = todoSignal.map(Todo::isDone);  
            Signal<String> taskSignal = todoSignal.map(Todo::getTask);  
               
            Checkbox doneBox = new Checkbox();
            doneBox.bindValue(doneSignal, done-> {
               todoSignal.update(todo -> {
                    todo.setDone(done);                   
                    return repo.save(todo);
                });
            });
            TextField taskField = new TextField();
            taskField.addKeyDownListener(Key.ESCAPE, event -> editing.set(false));
                taskField.bindValue(taskSignal, task -> {
                    todoSignal.update(todo -> {
                        todo.setTask(task);
                        return repo.save(todo);
                    });
                    editing.set(false);
                 });
            taskField.bindVisible(editing);

            Span taskSpan = new Span(todoSignal.map(Todo::getTask));
            taskSpan.addDoubleClickListener(click -> {
                editing.set(true);
                taskField.focus();
            });

            taskSpan.getStyle().bind("text-decoration", () -> doneSignal.get() ? "line-through" : "none");
            taskSpan.bindVisible(Signal.not(editing));
            Button removeButton = new Button(VaadinIcon.TRASH.create());
            removeButton.addClickListener(click -> {
                repo.delete(todoSignal.peek());
                todos.remove(todoSignal);
            });
            removeButton.addThemeVariants(ButtonVariant.SMALL, ButtonVariant.TERTIARY);
            HorizontalLayout layout = new HorizontalLayout(doneBox, taskField,taskSpan, removeButton);
            layout.setAlignItems(Alignment.BASELINE);
            return layout;
              });
    
        Span summary = new Span(() -> "Remaining tasks: " + todos.getValues().filter(todo -> !todo.isDone()).count());
        add(createTaskField, createTaskButton, todosLayout, summary);
        add(datePicker);
        add(datePicker1);
      
    }
    
}