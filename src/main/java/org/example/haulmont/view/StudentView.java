package org.example.haulmont.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.example.haulmont.entity.Student;
import org.example.haulmont.services.InstituteService;

import java.util.List;

@PageTitle("Студенты")
@Route(value = "", layout = MainLayout.class)
public class StudentView extends VerticalLayout {
    private final InstituteService instituteService;
    private final Grid<Student> grid = new Grid<>(Student.class);

    private TextField lastNameFilter = new TextField("Фамилия");
    private TextField groupNumberFilter = new TextField("Номер группы");

    public StudentView(InstituteService instituteService) {
        this.instituteService = instituteService;
        setSizeFull();

        configureGrid();
        add(createFilterLayout(), grid, createButtonsLayout());

        updateList();
    }

    private HorizontalLayout createFilterLayout() {
        Button applyFilterBtn = new Button("Применить", e -> filterStudents());
        return new HorizontalLayout(lastNameFilter, groupNumberFilter, applyFilterBtn);
    }

    private void configureGrid() {
        grid.setColumns("firstName", "lastName", "middleName", "birthDate");
        grid.addColumn(s -> s.getGroup().getNumber()).setHeader("Группа");
        grid.asSingleSelect().addValueChangeListener(e -> {
            Student selected = e.getValue();
            if (selected != null) {
                openEditor(selected);
            }
        });
        grid.setSizeFull();
    }

    private void filterStudents() {
        String lnFilter = lastNameFilter.getValue();
        String groupFilter = groupNumberFilter.getValue();
        List<Student> filtered = instituteService.filterStudents(lnFilter.isEmpty() ? null : lnFilter,
                groupFilter.isEmpty() ? null : groupFilter);
        grid.setItems(filtered);
    }



    private HorizontalLayout createButtonsLayout() {
        Button addButton = new Button("Добавить студента");
        addButton.addClickListener(e -> openEditor(new Student()));
        return new HorizontalLayout(addButton);
    }

    private void openEditor(Student student) {
        StudentForm studentForm = new StudentForm(student, instituteService, this::updateList);
        studentForm.open();
    }


    private void updateList() {
        List<Student> allStudents = instituteService.findAllStudents();
        grid.setItems(allStudents);
    }
}
