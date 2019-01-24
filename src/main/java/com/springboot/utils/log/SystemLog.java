package com.springboot.utils.log;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    String description()  default "";

    boolean printInputParam() default true;


}
