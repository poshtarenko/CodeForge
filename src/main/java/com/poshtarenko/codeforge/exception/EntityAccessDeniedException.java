package com.poshtarenko.codeforge.exception;

import com.poshtarenko.codeforge.entity.BaseEntity;

public class EntityAccessDeniedException extends RuntimeException {

    private final Class<? extends BaseEntity> entityClass;
    private final long entityId;
    private final long userId;

    public static final String DEFAULT_MESSAGE = "User does not have access to process this entity";

    public EntityAccessDeniedException(Class<? extends BaseEntity> entityClass,
                                       long entityId,
                                       long userId,
                                       String message) {
        super(message);
        this.entityClass = entityClass;
        this.entityId = entityId;
        this.userId = userId;
    }

    public EntityAccessDeniedException(Class<? extends BaseEntity> entityClass,
                                       long entityId,
                                       long userId) {
        this(entityClass, entityId, userId, DEFAULT_MESSAGE);
    }

    public Class<? extends BaseEntity> getEntityClass() {
        return entityClass;
    }

    public long getEntityId() {
        return entityId;
    }

    public long getUserId() {
        return userId;
    }
}
