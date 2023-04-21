package com.poshtarenko.codeforge.exception;

import com.poshtarenko.codeforge.entity.BaseEntity;

public class EntityNotFoundException extends RuntimeException {
    private final Class<? extends BaseEntity> entityClass;

    public EntityNotFoundException(Class<? extends BaseEntity> entityClass, String message) {
        super(message);
        this.entityClass = entityClass;
    }

    public Class<? extends BaseEntity> getEntityClass() {
        return entityClass;
    }
}
