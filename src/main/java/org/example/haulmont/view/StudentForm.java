package org.example.haulmont.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.example.haulmont.entity.Group;
import org.example.haulmont.entity.Student;
import org.example.haulmont.services.InstituteService;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class StudentForm extends Dialog {

    private final TextField firstNameField = new TextField("Имя");
    private final TextField lastNameField = new TextField("Фамилия");
    private final TextField middleNameField = new TextField("Отчество");
    private final DatePicker birthDateField = new DatePicker("Дата рождения");
    private final ComboBox<Group> groupComboBox = new ComboBox<>("Группа");

    private final Button saveButton = new Button("OK");
    private final Button cancelButton = new Button("Отменить");
    private final Button deleteButton = new Button("Удалить");

    private final InstituteService instituteService;
//    private final Consumer<Void> onSuccess;
    private Student currentStudent;
    private Runnable onSuccess;

    public StudentForm(Student student, InstituteService instituteService, Runnable onSuccess) {
        this.instituteService = instituteService;
        this.onSuccess = onSuccess;
        this.currentStudent = student;

        // Настраиваем поля
        firstNameField.setValue(student.getFirstName() != null ? student.getFirstName() : "");
        lastNameField.setValue(student.getLastName() != null ? student.getLastName() : "");
        middleNameField.setValue(student.getMiddleName() != null ? student.getMiddleName() : "");
        birthDateField.setValue(student.getBirthDate() != null ? student.getBirthDate() : LocalDate.now());

        // Список групп для выбора
        List<Group> groups = instituteService.findAllGroups();
        groupComboBox.setItems(groups);
        groupComboBox.setItemLabelGenerator(Group::getNumber);
        if (student.getGroup() != null) {
            groupComboBox.setValue(student.getGroup());
        }

        // События на кнопках
        saveButton.addClickListener(e -> saveStudent());
        cancelButton.addClickListener(e -> close());
        deleteButton.addClickListener(e -> deleteStudent());

        FormLayout formLayout = new FormLayout(
                firstNameField,
                lastNameField,
                middleNameField,
                birthDateField,
                groupComboBox,
                saveButton,
                cancelButton,
                deleteButton
        );

        add(new Text("Редактирование студента"), formLayout);

        setModal(true);
        setWidth("500px");
    }

    private void saveStudent() {
        currentStudent.setFirstName(firstNameField.getValue());
        currentStudent.setLastName(lastNameField.getValue());
        currentStudent.setMiddleName(middleNameField.getValue());
        currentStudent.setBirthDate(birthDateField.getValue());
        currentStudent.setGroup(groupComboBox.getValue());

        instituteService.saveStudent(currentStudent);
        onSuccess.run();
        close();
    }

    private void deleteStudent() {
        if (currentStudent.getId() != null) {
            instituteService.deleteStudent(currentStudent.getId());
            onSuccess.run();
        }
        close();
    }
}
