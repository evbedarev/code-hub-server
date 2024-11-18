package ru.madjo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ru.madjo.dto.CodeDtoError;
import ru.madjo.dto.ProjectCodeDto;
import ru.madjo.models.CodeVersion;
import ru.madjo.models.ProjectCode;
import ru.madjo.serivces.CodeService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest({CodeRestController.class})
public class CodeRestControllerTest {

    private static final String TEST_TEXT_FILE = "test_file.txt";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodeService codeService;

    @Test
    public void shouldGetListOfProjects() throws Exception {
        List<ProjectCode> projectCodes = getProjects();
        Mockito.when(codeService.getAllProjects()).thenReturn(getProjects());
        List<ProjectCodeDto> projectCodeDto = projectCodes.stream().map(
                p -> new ProjectCodeDto(p.getId(), p.getProjectName())).toList();
        String expectedRes = objectMapper.writeValueAsString(projectCodeDto);
        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedRes));
    }

    @Test
    public void shouldCreateNewProject() throws Exception {
        ProjectCode projectCode = new ProjectCode(1L, "project1", new ArrayList<>());
        Mockito.when(codeService.save("project1")).thenReturn(projectCode);
        String expectedResult = objectMapper.writeValueAsString(projectCode);
        mockMvc.perform(post("/api/v1/project/new/project1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    public void shouldCorrectInsertNewProjectAndCodeText() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(TEST_TEXT_FILE);
        MockMultipartFile multipartFile = new MockMultipartFile("file", TEST_TEXT_FILE,
                MediaType.TEXT_PLAIN_VALUE,
                inputStream.readAllBytes());
        CodeVersion codeVersion = new CodeVersion(1,"text","any","1", 1);
        Mockito.when(codeService.saveProjectVersion(multipartFile, 1L, "1-1")).thenReturn(codeVersion);
        String expectedResult = objectMapper.writeValueAsString(new CodeDtoError(false, ""));
        mockMvc.perform(multipart("/api/v1/project/1/1-1").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
        //CodeDto codeDto = new CodeDto("ver-01","project1","/home","")

    }

    private List<ProjectCode> getProjects() {
        return List.of(new ProjectCode(1L,"proj1",new ArrayList<>()),
                new ProjectCode(2L,"proj2",new ArrayList<>()));
    }

}
