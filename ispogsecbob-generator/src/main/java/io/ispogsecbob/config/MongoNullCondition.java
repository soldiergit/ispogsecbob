package io.ispogsecbob.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author: gxz gongxuanzhang@foxmail.com
 **/
public class MongoNullCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String database = context.getEnvironment().getProperty("ispogsecbob.database");
        return !"mongodb".equalsIgnoreCase(database);
    }
}
