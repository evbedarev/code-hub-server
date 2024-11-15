package ru.madjo.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class CodeDto {

    @NonNull
    private String version;

    @NonNull
    private String projectName;

    @NonNull
    private String filePath;

    @NonNull
    private String codeText;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj.getClass() != this.getClass() || obj == null) return false;
        CodeDto objDto = (CodeDto)  obj;
        StringBuilder objSb = new StringBuilder();
        StringBuilder thisSb = new StringBuilder();
        thisSb.append(version).append(projectName).append(filePath).append(codeText);
        objSb.append(objDto.version).append(objDto.projectName)
                .append(objDto.filePath).append(objDto.codeText);
        return objSb.compareTo(thisSb) == 0;
    }
}
