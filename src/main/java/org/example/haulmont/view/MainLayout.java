package org.example.haulmont.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {
    public MainLayout() {
        addToNavbar(
                new Button(new RouterLink("Группы", GroupView.class)),
                new Button(new RouterLink("Студенты", StudentView.class))
        );
    }
}
