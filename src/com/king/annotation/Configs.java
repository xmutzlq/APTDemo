package com.king.annotation;

/**
 * 手动写入
 * @author zlq
 * @date 2017年8月28日 下午2:27:24
 */
public final class Configs {
	public static final String PACKAG_NAME = "com.dehong.dhcloud"; // 定义包名
	public static final String FILE_DIR = PACKAG_NAME + "." + "apt"; // 定义文件路径
	
	//================ 构造MODEL =====================//
	public static final String CONFIG_DIR = PACKAG_NAME + ".config";
	public static final String CLASS_DIR = PACKAG_NAME + ".http";
	public static final String GENERICS_CLASS_DIR = PACKAG_NAME + ".info";
	public static final String TYPET_TOKEN_DIR = "com.google.gson.reflect";

	public static final String CONFIG_CLASS_NAME = "Config";
	public static final String SUPER_CLASS_NAME = "HttpRequest";
	public static final String CALL_BACK_NAME = "RequestCallBack";
	public static final String TYPET_TOKEN_NAME = "TypeToken";
}
