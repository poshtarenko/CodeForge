package com.poshtarenko.codeforge.exception;

import com.poshtarenko.codeforge.entity.BaseEntity;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends APIException {

    public static final String EXCEPTION_CODE = "001";
    public static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    private final Class<? extends BaseEntity> entityClass;

    public EntityNotFoundException(Class<? extends BaseEntity> entityClass, String message) {
        super(message, EXCEPTION_CODE, HTTP_STATUS);
        this.entityClass = entityClass;
    }

    public Class<? extends BaseEntity> getEntityClass() {
        return entityClass;
    }
}
