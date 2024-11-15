package ru.madjo.serivces;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.madjo.dto.CodeDto;
import ru.madjo.exceptions.ProjectCodeNotFoundException;
import ru.madjo.exceptions.VersionAlreadyExistsException;
import ru.madjo.models.CodeText;
import ru.madjo.models.ProjectCode;
import ru.madjo.repositories.CodeTextRepository;
import ru.madjo.repositories.ProjectCodeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CodeServiceImpl implements CodeService {

    private final CodeTextRepository codeTextRepository;

    private final ProjectCodeRepository projectCodeRepository;

    @Override
    public CodeText saveProjectVersion(CodeDto codeDto) {
        Optional<ProjectCode> projectCode = projectCodeRepository.findByProjectName(codeDto.getProjectName());
        CodeText savedCodeText;
        if (projectCode.isPresent()) {
            Optional<CodeText> codeText = codeTextRepository.findByVersionAndProjectCodeId(
                    codeDto.getVersion(), projectCode.get().getId());
            if (codeText.isPresent())
                throw new VersionAlreadyExistsException("Version %s in project %s exist".formatted(
                        codeText.get().getVersion(), projectCode.get().getProjectName()
                ));
            savedCodeText = codeTextRepository.save(new CodeText(0, codeDto.getCodeText(),
                    codeDto.getFilePath(),
                    codeDto.getVersion(),
                    projectCode.get().getId()));
        } else {
            ProjectCode savedProj = projectCodeRepository.save(
                    new ProjectCode(0, codeDto.getProjectName(), new ArrayList<>()));
            savedCodeText = codeTextRepository.save(new CodeText(0,codeDto.getCodeText(),
                    codeDto.getFilePath(),
                    codeDto.getVersion(),
                    savedProj.getId()));
        }
        return savedCodeText;
    }

    @Override
    public List<String> getAllProjects() {
        List<ProjectCode> projects = projectCodeRepository.findAll();
        return projects.stream().map( p -> p.getProjectName()).toList();
    }

    @Override
    public ProjectCode findById(long id) {
        return projectCodeRepository.findById(id)
                .orElseThrow(() -> new ProjectCodeNotFoundException("Project with id: %d not found"
                        .formatted(id)));
    }
}
