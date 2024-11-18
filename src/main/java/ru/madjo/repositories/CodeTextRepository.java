package ru.madjo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.madjo.models.CodeVersion;

import java.util.List;
import java.util.Optional;

public interface CodeTextRepository extends JpaRepository<CodeVersion, Long> {

    Optional<CodeVersion> findById(long id);

    @Override
    List<CodeVersion> findAll();

    Optional<CodeVersion> findByVersionAndProjectCodeId(String version, long projectCodeId);

}
