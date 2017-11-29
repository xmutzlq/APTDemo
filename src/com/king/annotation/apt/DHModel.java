package com.king.annotation.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface DHModel {
	/**����**/
	String className();
	/**������<\T\>**/
	String genericsClassName();
	/**IP��ַ**/
	String url();
	/**�������**/
    String[] params() default {""};
    /**�Ƿ�洢����**/
    int saveJsonType() default -1;
}
