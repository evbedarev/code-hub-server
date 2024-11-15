package ru.madjo.serivces;

import ru.madjo.dto.CodeDto;
import ru.madjo.models.CodeText;
import ru.madjo.models.ProjectCode;

import java.util.List;

public interface CodeService {

    CodeText saveProjectVersion(CodeDto codeDto);

    List<String> getAllProjects();

    ProjectCode findById(long id);
}
