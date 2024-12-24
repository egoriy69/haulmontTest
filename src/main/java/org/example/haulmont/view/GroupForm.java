package org.example.haulmont.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import org.example.haulmont.entity.Group;
import org.example.haulmont.services.InstituteService;

public class GroupForm extends Dialog {

    private final TextField numberField = new TextField("Номер группы");
    private final TextField facultyField = new TextField("Факультет");
    private final InstituteService instituteService;
    private Group currentGroup;
    private Runnable onSuccess;

    public GroupForm(Group group, InstituteService instituteService, Runnable onSuccess) {
        this.instituteService = instituteService;
        this.onSuccess = onSuccess;
        this.currentGroup = group;

        numberField.setValue(group.getNumber() != null ? group.getNumber() : "");
        facultyField.setValue(group.getFacultyName() != null ? group.getFacultyName() : "");

        Button saveButton = new Button("OK", e -> saveGroup());
        Button cancelButton = new Button("Отменить", e -> close());
        Button deleteButton = new Button("Удалить", e -> deleteGroup());

        FormLayout formLayout = new FormLayout(numberField, facultyField, saveButton, cancelButton, deleteButton);
        add(new Text("Редактировать группу"), formLayout);

        setModal(true);
        setWidth("400px");
    }

    private void saveGroup() {
        currentGroup.setNumber(numberField.getValue());
        currentGroup.setFacultyName(facultyField.getValue());
        instituteService.saveGroup(currentGroup);
        onSuccess.run();
        close();
    }

    private void deleteGroup() {
        if (currentGroup.getId() != null) {
            try {
                instituteService.deleteGroup(currentGroup.getId());
                onSuccess.run();
                close();
            }
            catch (Exception e) {
                Notification.show(e.getMessage());
            }

        }

    }
}
