package com.king.annotation.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface DHModel {
	/**类名**/
	String className();
	/**泛型类<\T\>**/
	String genericsClassName();
	/**IP地址**/
	String url();
	/**请求参数**/
    String[] params() default {""};
    /**是否存储数据**/
    int saveJsonType() default -1;
}
