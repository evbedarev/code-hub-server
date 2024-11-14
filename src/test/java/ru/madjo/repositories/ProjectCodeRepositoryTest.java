package ru.madjo.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.madjo.models.CodeText;
import ru.madjo.models.ProjectCode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ProjectCodeRepositoryTest {

    @Autowired
    private ProjectCodeRepository projectCodeRepository;

    @Test
    public void shouldGetCorrectProjectCode() {
        Optional<ProjectCode> projectCode = projectCodeRepository.findById(1L);
        assertThat(projectCode.get().getProjectName()).isEqualTo("code_hub");
        List<CodeText> codeText = projectCode.get().getCodeText();
        System.out.println(codeText.get(0).getId());
        System.out.println(codeText.get(0).getFilePath());
        System.out.println(codeText.get(0).getCodeText());
    }

    @Test
    public void  shouldCorrectSaveProject() {
        ProjectCode projectCode = new ProjectCode(0,"proj_code2",
                new ArrayList<>());
        projectCodeRepository.save(projectCode);
        Optional<ProjectCode> optCode = projectCodeRepository.findById(2L);
        assertThat(optCode.get().getProjectName()).isEqualTo("proj_code2");
    }

    @Test
    public void shouldCorrectGetAllProjects() {
        ProjectCode projectCode = new ProjectCode(0,"proj_code2",
                new ArrayList<>());
        projectCodeRepository.save(projectCode);
        List<ProjectCode> projectCodes = projectCodeRepository.findAll();
        assertThat(projectCodes.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindByProjectName() {
        Optional<ProjectCode> projectCode = projectCodeRepository.findByProjectName("code_hub");
        assertThat(projectCode).isPresent();

    }
}
