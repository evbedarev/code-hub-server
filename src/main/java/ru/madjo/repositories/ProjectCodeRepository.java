package ru.madjo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.madjo.models.ProjectCode;

import java.util.List;
import java.util.Optional;

public interface ProjectCodeRepository extends JpaRepository<ProjectCode, Long> {

    Optional<ProjectCode> findById(long id);

    Optional<ProjectCode> findByProjectName(String projectName);

    @Override
    List<ProjectCode> findAll();

}
