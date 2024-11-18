package ru.madjo.rest;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.madjo.dto.CodeDtoError;
import ru.madjo.dto.ProjectCodeDto;
import ru.madjo.exceptions.ProjectCodeNotFoundException;
import ru.madjo.exceptions.VersionAlreadyExistsException;
import ru.madjo.models.CodeVersion;
import ru.madjo.models.ProjectCode;
import ru.madjo.serivces.CodeService;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class CodeRestController {

    private static final Logger logger = LoggerFactory.getLogger(CodeRestController.class);

    private final CodeService codeService;

    @GetMapping("/api/v1/projects")
    public List<ProjectCodeDto> getAllProjects() {
       List<ProjectCode> projectCodes = codeService.getAllProjects();
       return projectCodes.stream().map(p -> new ProjectCodeDto(p.getId(), p.getProjectName())).toList();
    }

    @PostMapping("/api/v1/project/{id}/{version}")
    public CodeDtoError saveProject(@RequestParam("file") MultipartFile file,
                                    @PathVariable("id") long id,
                                    @PathVariable("version") String version) {
        try {
            codeService.saveProjectVersion(file,id, version);
            return new CodeDtoError(false, "");
        } catch (VersionAlreadyExistsException | ProjectCodeNotFoundException ex) {
            return new CodeDtoError(true, ex.getMessage());
        } catch (IOException e) {
            return new CodeDtoError(true, e.getMessage());
        }
    }

    @PostMapping("/api/v1/project/new/{name}")
    public ProjectCode createNewProject(@PathVariable("name") String name) {
        return codeService.save(name);
    }

    @GetMapping("/api/v1/code/{proj_id}")
    public List<CodeVersion> getAllVersionsForProject(@PathVariable("proj_id") long proj_id) {

    }

}
