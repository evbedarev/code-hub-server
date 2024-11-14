package ru.madjo.rest;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.madjo.serivces.CodeService;

import java.util.List;

@RestController
@AllArgsConstructor
public class CodeRestController {

    private static final Logger logger = LoggerFactory.getLogger(CodeRestController.class);

    private final CodeService codeService;

    @GetMapping("/api/v1/projects")
    public List<String> getAllProjects() {
       return codeService.getAllProjects();
    }
}
