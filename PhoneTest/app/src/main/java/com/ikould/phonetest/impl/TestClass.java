package com.ikould.phonetest.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * describe
 * Created by liudong on 2017/1/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface TestClass {

}
