package ru.madjo.rest;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.madjo.dto.CodeDto;
import ru.madjo.dto.CodeDtoError;
import ru.madjo.exceptions.VersionAlreadyExistsException;
import ru.madjo.models.ProjectCode;
import ru.madjo.serivces.CodeService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

@RestController
@AllArgsConstructor
public class CodeRestController {

    private static final Logger logger = LoggerFactory.getLogger(CodeRestController.class);

    private final CodeService codeService;

    @GetMapping("/api/v1/projects")
    public List<String> getAllProjects() {
       return codeService.getAllProjects();
    }

    @PostMapping("/api/v1/project/{id}/{version}")
    public CodeDtoError saveProject(@RequestParam("file") MultipartFile file,
                                    @PathVariable("id") long id,
                                    @PathVariable("version") String version) {
        try {
            InputStream inputStream = file.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            ProjectCode projectCode = codeService.findById(id);
            String text = scanner.toString();
            codeService.saveProjectVersion(new CodeDto(version, projectCode.getProjectName(),
                    "", text));
            return new CodeDtoError(false, "");
        } catch (VersionAlreadyExistsException ex) {
            return new CodeDtoError(true, ex.getMessage());
        } catch (IOException e) {
            return new CodeDtoError(true, e.getMessage());
        }
    }

}
