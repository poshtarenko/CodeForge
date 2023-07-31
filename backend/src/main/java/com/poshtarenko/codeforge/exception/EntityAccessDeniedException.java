package com.poshtarenko.codeforge.exception;

import com.poshtarenko.codeforge.entity.BaseEntity;
import org.springframework.http.HttpStatus;

public class EntityAccessDeniedException extends APIException {

    public static final String EXCEPTION_CODE = "002";
    public static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;

    private final Class<? extends BaseEntity> entityClass;
    private final long entityId;
    private final long userId;

    public static final String DEFAULT_MESSAGE = "User does not have access to process this entity";

    public EntityAccessDeniedException(Class<? extends BaseEntity> entityClass,
                                       long entityId,
                                       long userId,
                                       String message) {
        super(message, EXCEPTION_CODE, HTTP_STATUS);
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
