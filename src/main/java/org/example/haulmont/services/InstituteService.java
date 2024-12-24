package org.example.haulmont.services;


import lombok.RequiredArgsConstructor;
import org.example.haulmont.entity.Group;
import org.example.haulmont.entity.Student;
import org.example.haulmont.repositories.GroupRepository;
import org.example.haulmont.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstituteService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    public void deleteGroup(Long groupId) {
        List<Student> studentsInGroup = studentRepository.findAll().stream()
                .filter(s -> s.getGroup().getId().equals(groupId))
                .toList();
        if (!studentsInGroup.isEmpty()) {
            throw new RuntimeException("Невозможно удалить группу: в ней есть студенты!");
        }
        groupRepository.deleteById(groupId);
    }

    public Optional<Group> findGroupById(Long id) {
        return groupRepository.findById(id);
    }

    // Студенты
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    public Optional<Student> findStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> filterStudents(String lastNameFilter, String groupNumberFilter) {
        return studentRepository.findAll().stream()
                .filter(s -> lastNameFilter == null || s.getLastName().toLowerCase()
                        .contains(lastNameFilter.toLowerCase()))
                .filter(s -> groupNumberFilter == null || s.getGroup().getNumber()
                        .equalsIgnoreCase(groupNumberFilter))
                .toList();
    }
}
