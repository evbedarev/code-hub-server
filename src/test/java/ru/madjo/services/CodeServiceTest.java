package ru.madjo.services;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.madjo.dto.CodeDto;
import ru.madjo.exceptions.VersionAlreadyExistsException;
import ru.madjo.models.CodeText;
import ru.madjo.models.ProjectCode;
import ru.madjo.repositories.CodeTextRepository;
import ru.madjo.repositories.ProjectCodeRepository;
import ru.madjo.serivces.CodeService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CodeServiceTest {

    @Autowired
    private CodeService codeService;

    @MockBean
    private CodeTextRepository codeTextRepository;

    @MockBean
    private ProjectCodeRepository projectCodeRepository;


    @Test
    public void shouldCorrectSaveNewProjectAndCodeText() {
        CodeDto testDto = new CodeDto("ver1","proj1","file1", "Mytext");
        Mockito.when(projectCodeRepository.findByProjectName("proj1")).thenReturn(Optional.empty());
        ProjectCode resultCode = new ProjectCode(0,testDto.getProjectName(),new ArrayList<>());
        Mockito.when(projectCodeRepository.save(resultCode))
                .thenReturn(resultCode);
        CodeText cT = new CodeText(0,testDto.getCodeText(), testDto.getFilePath(), testDto.getVersion(), 0);
        Mockito.when(codeTextRepository.save(cT)).thenReturn(cT);
        CodeText codeText1 = codeService.saveProjectVersion(testDto);
        assertThat(codeText1.getProjectCodeId()).isEqualTo(0);

    }
    @Test
    public void shouldThrowExceptionWhenCreateNewProject() {
        CodeDto testDto = new CodeDto("ver1","proj1","file1", "Mytext");
        ProjectCode resultCode = new ProjectCode(1L,testDto.getProjectName(),new ArrayList<>());
        Mockito.when(projectCodeRepository.findByProjectName("proj1")).thenReturn(Optional.of(resultCode));
        CodeText cT = new CodeText(0,testDto.getCodeText(), testDto.getFilePath(), testDto.getVersion(), 0);
        Mockito.when(codeTextRepository.findByVersionAndProjectCodeId("ver1",1L))
                .thenReturn(Optional.of(cT));
        assertThatThrownBy(() -> codeService.saveProjectVersion(testDto)).isInstanceOf(VersionAlreadyExistsException.class);
    }

    @Test
    public void shouldReturnCorrectListOfProjects() {
        ProjectCode pc1 = new ProjectCode(1L,"proj1",new ArrayList<>());
        ProjectCode pc2 = new ProjectCode(2L,"proj2",new ArrayList<>());
        Mockito.when(projectCodeRepository.findAll()).thenReturn(List.of(pc1,pc2));
        List<String> projectNames = codeService.getAllProjects();
        assertThat(projectNames).contains(pc1.getProjectName(),pc2.getProjectName());
    }

    @Test
    public void shouldAddTextToExistingProject() {
        CodeDto testDto = new CodeDto("ver1","proj1","file1", "Mytext");
        ProjectCode resultCode = new ProjectCode(1,testDto.getProjectName(),new ArrayList<>());
        Mockito.when(projectCodeRepository.findByProjectName("proj1"))
                .thenReturn(Optional.of(resultCode));
        Mockito.when(codeTextRepository.findByVersionAndProjectCodeId("ver1",1))
                .thenReturn(Optional.empty());
        CodeText expectedText = new CodeText(0,testDto.getCodeText(), testDto.getFilePath(), testDto.getVersion(), 1);
        Mockito.when(codeTextRepository.save(expectedText)).thenReturn(expectedText);
        CodeText resultText = codeService.saveProjectVersion(testDto);
        assertThat(resultText).isEqualTo(expectedText);
    }
}
