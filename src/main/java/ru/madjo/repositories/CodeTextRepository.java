package ru.madjo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.madjo.models.CodeText;

import java.util.List;
import java.util.Optional;

public interface CodeTextRepository extends JpaRepository<CodeText, Long> {

    Optional<CodeText> findById(long id);

    @Override
    List<CodeText> findAll();

    Optional<CodeText> findByVersionAndProjectCodeId(String version, long projectCodeId);

}
