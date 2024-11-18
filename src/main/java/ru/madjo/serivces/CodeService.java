package ru.madjo.serivces;

import org.springframework.web.multipart.MultipartFile;
import ru.madjo.models.CodeVersion;
import ru.madjo.models.ProjectCode;

import java.io.IOException;
import java.util.List;

public interface CodeService {

    CodeVersion saveProjectVersion(MultipartFile file, long projectId, String version) throws IOException;

    List<ProjectCode> getAllProjects();

    ProjectCode findById(long id);

    ProjectCode save(String name);
}
