package org.example.haulmont.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.example.haulmont.entity.Group;
import org.example.haulmont.services.InstituteService;

import java.util.List;

@PageTitle("Группы")
@Route(value = "groups", layout = MainLayout.class)
public class GroupView extends VerticalLayout {
    private final InstituteService instituteService;
    private final Grid<Group> grid = new Grid<>(Group.class);

    public GroupView(InstituteService instituteService) {
        this.instituteService = instituteService;
        setSizeFull();

        configureGrid();
        add(grid, createButtonsLayout());

        updateList();
    }

    private void configureGrid() {
        grid.setColumns("number", "facultyName");
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(event -> {
            Group selected = event.getValue();
            if (selected != null) {
                openEditor(selected);
            }
        });
    }

    private void updateList() {
        List<Group> groups = instituteService.findAllGroups();
        grid.setItems(groups);
    }

    private Button createButtonsLayout() {
        Button addButton = new Button("Добавить группу");
        addButton.addClickListener(e -> {
            openEditor(new Group());
        });
        return addButton;
    }

    private void openEditor(Group group) {
        GroupForm groupForm = new GroupForm(group, instituteService, this::updateList);
        groupForm.open();
    }
}
