package ru.madjo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "code_text")
@AllArgsConstructor
@NoArgsConstructor
public class CodeText {

    @Id
    @Getter
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Column(name = "code_text", columnDefinition = "LONGTEXT", nullable = false)
    private String codeText;

    @Getter
    @Column(name = "file_path", nullable = false, unique = false)
    private String filePath;

    @Getter
    @Column(name = "version", nullable = false, unique = false)
    private String version;

    @Getter
    @Column(name = "project_id", nullable = false, unique = false)
    private long projectCodeId;

    @Override
    public boolean equals(Object obj) {
        Logger logger = LoggerFactory.getLogger(CodeText.class);
        logger.debug("Compare CodeText");
        if (obj == this) return true;
        if (getClass() != obj.getClass() || obj == null) return false;
        CodeText objCodeText = (CodeText) obj;
        StringBuilder thisString = new StringBuilder();
        StringBuilder objString = new StringBuilder();
        thisString.append(id)
                .append(codeText)
                .append(filePath)
                .append(version)
                .append(projectCodeId);
        objString.append(objCodeText.id)
                .append(objCodeText.codeText)
                .append(objCodeText.filePath)
                .append(objCodeText.version)
                .append(objCodeText.projectCodeId);
        logger.debug(thisString.toString());
        logger.debug(thisString.toString());
        return thisString.compareTo(objString) == 0;
    }
}
