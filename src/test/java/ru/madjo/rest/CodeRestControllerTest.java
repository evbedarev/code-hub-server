package ru.madjo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import ru.madjo.dto.CodeDto;
import ru.madjo.dto.CodeDtoError;
import ru.madjo.models.CodeText;
import ru.madjo.models.ProjectCode;
import ru.madjo.serivces.CodeService;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

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
        List<String> listOfProj = List.of("project1","project2");
        Mockito.when(codeService.getAllProjects()).thenReturn(listOfProj);
        String expectedRes = objectMapper.writeValueAsString(listOfProj);
        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedRes));
    }

    @Test
    public void shouldCorrectInsertNewProjectAndCodeText() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(TEST_TEXT_FILE);
        ProjectCode projectCode = new ProjectCode(1L,"proj1",new ArrayList<>());
        Scanner scanner = new Scanner(inputStream);
        String text = scanner.toString();
        CodeText codeText = new CodeText(1, text, "","24-01",1L);
        CodeDto codeDto = new CodeDto("24-01", projectCode.getProjectName(), "", text);

        Mockito.when(codeService.findById(1L)).thenReturn(projectCode);
        MockMultipartFile multipartFile = new MockMultipartFile("file", TEST_TEXT_FILE,
                MediaType.TEXT_PLAIN_VALUE,
                inputStream.readAllBytes());
        Mockito.when(codeService.saveProjectVersion(codeDto)).thenReturn(codeText);
        String expectedResult = objectMapper.writeValueAsString(new CodeDtoError(false, ""));
        mockMvc.perform(multipart("/api/v1/project/1/24-01").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
        //CodeDto codeDto = new CodeDto("ver-01","project1","/home","")

    }

}
