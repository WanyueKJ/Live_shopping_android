package com.wanyue.common.server.annotation;

import com.alibaba.fastjson.annotation.JSONField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})


public @interface Text2 {

         String value();
         @JSONField String name() default "";
}
