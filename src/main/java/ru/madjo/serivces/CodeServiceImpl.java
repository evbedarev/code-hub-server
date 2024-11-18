package ru.madjo.serivces;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.madjo.dto.CodeDto;
import ru.madjo.exceptions.ProjectCodeNotFoundException;
import ru.madjo.exceptions.VersionAlreadyExistsException;
import ru.madjo.models.CodeVersion;
import ru.madjo.models.ProjectCode;
import ru.madjo.repositories.CodeTextRepository;
import ru.madjo.repositories.ProjectCodeRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
@AllArgsConstructor
public class CodeServiceImpl implements CodeService {

    private final CodeTextRepository codeTextRepository;

    private final ProjectCodeRepository projectCodeRepository;

    @Override
    public CodeVersion saveProjectVersion(MultipartFile file, long projectId, String version) throws IOException {
        String text = getTextFromMultipartFile(file);
        ProjectCode projectCode = findById(projectId);
        CodeDto codeDto = new CodeDto(version, projectCode.getProjectName(), "", text);
        return saveInExistingProject(codeDto, projectCode);
    }

    private String getTextFromMultipartFile(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\n");

        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            String line = scanner.next();
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

    private CodeVersion saveInExistingProject(CodeDto codeDto, ProjectCode projectCode) {
        Optional<CodeVersion> codeText = codeTextRepository.findByVersionAndProjectCodeId(
                codeDto.getVersion(), projectCode.getId());
        if (codeText.isPresent())
            throw new VersionAlreadyExistsException("Version %s in project %s exist".formatted(
                    codeText.get().getVersion(), projectCode.getProjectName()
            ));
        return codeTextRepository.save(new CodeVersion(0, codeDto.getCodeText(),
                codeDto.getFilePath(),
                codeDto.getVersion(),
                projectCode.getId()));
    }


    @Override
    public List<ProjectCode> getAllProjects() {
        return projectCodeRepository.findAll();
    }

    @Override
    public ProjectCode findById(long id) {
        return projectCodeRepository.findById(id)
                .orElseThrow(() -> new ProjectCodeNotFoundException("Project with id: %d not found"
                        .formatted(id)));
    }

    @Override
    public ProjectCode save(String name) {
        ProjectCode projectCode = new ProjectCode(0, name, new ArrayList<>());
        return projectCodeRepository.save(projectCode);
    }
}
