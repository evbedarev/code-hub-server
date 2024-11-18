package ru.madjo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Entity
@Table(name = "code_hub")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCode {

    @Id
    @Column
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Column(name = "project_name", nullable = false, unique = true)
    private String projectName;

    @Getter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private List<CodeVersion> codeVersions;

    @Override
    public boolean equals(Object obj) {
        Logger logger = LoggerFactory.getLogger(CodeVersion.class);
        logger.debug("Compare ProjectCode");
        if (this == obj) return true;
        if (obj.getClass() != this.getClass() || obj == null) return false;
        ProjectCode pCode = (ProjectCode) obj;
        StringBuilder thisString = new StringBuilder();
        StringBuilder objString = new StringBuilder();
        thisString.append(id)
                .append(projectName);
        objString.append(pCode.id)
                .append(pCode.projectName);

        if (!compareTwoCodeTextList(codeVersions, pCode.getCodeVersions())) return false;
        if (codeVersions != null && pCode.getCodeVersions() != null) {
            for (CodeVersion codeT : pCode.getCodeVersions()) {
                if (!codeVersions.contains(codeT)) {
                    return false;
                }
            }
        }
        logger.debug(thisString.toString());
        logger.debug(objString.toString());
        return thisString.compareTo(objString) == 0;
    }

    private boolean compareTwoCodeTextList(List<CodeVersion> one, List<CodeVersion> two) {
        if (one!= null && two != null) {
            for (CodeVersion codeT : two) {
                if (!one.contains(codeT)) {
                    return false;
                }
            }
        }
        return true;
    }
}
