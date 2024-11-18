package ru.madjo.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.madjo.models.CodeVersion;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class CodeVersionRepositoryTest {

    @Autowired
    private CodeTextRepository codeTextRepository;

    @Test
    public void shouldCorrectFindCodeTextById() {
        Optional<CodeVersion> codeText = codeTextRepository.findById(1L);
        assertThat(codeText.get().getCodeText()).isEqualTo("CodeText1");
        assertThat(codeText.get().getFilePath()).isEqualTo("FilePath1");
    }

    @Test
    public void shouldCorrectFindAllCodeText() {
        List<CodeVersion> codeVersions = codeTextRepository.findAll();
        assertThat(codeVersions.get(0).getCodeText()).isEqualTo("CodeText1");
        assertThat(codeVersions.get(0).getFilePath()).isEqualTo("FilePath1");
    }

    @Test
    public void shouldCorrectSaveCodeText() {
        CodeVersion codeVersion = new CodeVersion(0,"CodeText2", "FilePath2", "release/2", 1);
        codeTextRepository.save(codeVersion);
        Optional<CodeVersion> expected = codeTextRepository.findById(2L);
        assertThat(expected.get().getCodeText()).isEqualTo("CodeText2");
        assertThat(expected.get().getVersion()).isEqualTo("release/2");
    }

    @Test
    public void shouldReturnCodeByVersionAndCodeId() {
        Optional<CodeVersion> codeText = codeTextRepository
                .findByVersionAndProjectCodeId("release/1",1L);
        assertThat(codeText).isPresent();
    }
}
