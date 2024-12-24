package org.example.haulmont.repositories;

import org.example.haulmont.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("""
       SELECT s
       FROM Student s
       WHERE 
         (:lastNameFilter IS NULL OR TRIM(:lastNameFilter) = '' 
          OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :lastNameFilter, '%')))
         AND 
         (:groupNumberFilter IS NULL OR TRIM(:groupNumberFilter) = ''
          OR LOWER(s.group.number) = LOWER(:groupNumberFilter))
    """)
    List<Student> findByFilters(
            @Param("lastNameFilter") String lastNameFilter,
            @Param("groupNumberFilter") String groupNumberFilter
    );
}
