package ru.madjo.serivces;

import ru.madjo.dto.CodeDto;
import ru.madjo.models.CodeText;

import java.util.List;

public interface CodeService {

    CodeText saveProjectVersion(CodeDto codeDto);

    List<String> getAllProjects();
}
