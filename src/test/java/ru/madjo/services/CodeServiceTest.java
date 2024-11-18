package ru.madjo.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.madjo.exceptions.ProjectCodeNotFoundException;
import ru.madjo.models.CodeVersion;
import ru.madjo.models.ProjectCode;
import ru.madjo.repositories.CodeTextRepository;
import ru.madjo.repositories.ProjectCodeRepository;
import ru.madjo.serivces.CodeService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CodeServiceTest {

    private static final String TEST_TEXT_FILE = "test_file.txt";

    private static final String TEST_VER = "ver1";

    @Autowired
    private CodeService codeService;

    @MockBean
    private CodeTextRepository codeTextRepository;

    @MockBean
    private ProjectCodeRepository projectCodeRepository;


    @Test
    public void shouldCorrectSaveCodeTextInProject() throws IOException {
        ProjectCode projectCode = new ProjectCode(1L,"proj1",new ArrayList<>());
        Mockito.when(projectCodeRepository.findById(1L)).thenReturn(Optional.of(projectCode));
        Mockito.when(codeTextRepository.findByVersionAndProjectCodeId(TEST_VER,1L))
                .thenReturn(Optional.empty());
        MockMultipartFile mockMultipartFile = getMultipart();
        CodeVersion codeVersionExpected = new CodeVersion(0,
                getTextFromMultipartFile(getMultipart()),
                "",TEST_VER,1L);
        Mockito.when(codeTextRepository.save(codeVersionExpected)).thenReturn(codeVersionExpected);
        CodeVersion codeVersion = codeService.saveProjectVersion(mockMultipartFile,1L,TEST_VER);
        assertThat(codeVersion).isEqualTo(codeVersionExpected);
    }

    @Test
    public void shouldThrowExceptionWhenCreateNewProject() throws IOException {
        Mockito.when(projectCodeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> codeService.saveProjectVersion(
                getMultipart(),1L, TEST_VER
        )).isInstanceOf(ProjectCodeNotFoundException.class);
    }

    @Test
    public void shouldReturnCorrectListOfProjects() {
        List<ProjectCode> projects = getProjects();
        Mockito.when(projectCodeRepository.findAll()).thenReturn(projects);
        List<ProjectCode> projectNames = codeService.getAllProjects();
        assertThat(projectNames).contains(projects.get(0),
                projects.get(1));
    }

    @Test
    public void shouldReturnAllProjects() {
        List<ProjectCode> projectCodes = getProjects();
        Mockito.when(projectCodeRepository.findAll()).thenReturn(projectCodes);
        List<ProjectCode> projNames = codeService.getAllProjects();
        assertThat(projNames).contains(projectCodes.get(0),
                projectCodes.get(1));
    }

    private MockMultipartFile getMultipart() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(TEST_TEXT_FILE);
        return new MockMultipartFile("file", TEST_TEXT_FILE,
                MediaType.TEXT_PLAIN_VALUE, inputStream.readAllBytes());
    }

    private String getTextFromMultipartFile(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\n");;
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            String line = scanner.next();
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

    private List<ProjectCode> getProjects() {
        return List.of(new ProjectCode(1L,"proj1",new ArrayList<>()),
                new ProjectCode(2L,"proj2",new ArrayList<>()));
    }
}
