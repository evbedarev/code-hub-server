package ru.madjo.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.madjo.models.CodeText;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class CodeTextRepositoryTest {

    @Autowired
    private CodeTextRepository codeTextRepository;

    @Test
    public void shouldCorrectFindCodeTextById() {
        Optional<CodeText> codeText = codeTextRepository.findById(1L);
        assertThat(codeText.get().getCodeText()).isEqualTo("CodeText1");
        assertThat(codeText.get().getFilePath()).isEqualTo("FilePath1");
    }

    @Test
    public void shouldCorrectFindAllCodeText() {
        List<CodeText> codeText = codeTextRepository.findAll();
        assertThat(codeText.get(0).getCodeText()).isEqualTo("CodeText1");
        assertThat(codeText.get(0).getFilePath()).isEqualTo("FilePath1");
    }

    @Test
    public void shouldCorrectSaveCodeText() {
        CodeText codeText = new CodeText(0,"CodeText2", "FilePath2", "release/2", 1);
        codeTextRepository.save(codeText);
        Optional<CodeText> expected = codeTextRepository.findById(2L);
        assertThat(expected.get().getCodeText()).isEqualTo("CodeText2");
        assertThat(expected.get().getVersion()).isEqualTo("release/2");
    }

    @Test
    public void shouldReturnCodeByVersionAndCodeId() {
        Optional<CodeText> codeText = codeTextRepository
                .findByVersionAndProjectCodeId("release/1",1L);
        assertThat(codeText).isPresent();
    }
}
