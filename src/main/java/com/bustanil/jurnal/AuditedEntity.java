package com.bustanil.jurnal;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AuditedEntity {

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    private String modifiedBy;

}
