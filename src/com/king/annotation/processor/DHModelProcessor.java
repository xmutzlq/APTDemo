package com.king.annotation.processor;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.annotation.processing.FilerException;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import com.king.annotation.Configs;
import com.king.annotation.IProcessor;
import com.king.annotation.KingProcessor;
import com.king.annotation.apt.DHModel;
import com.king.annotation.poet.ClassName;
import com.king.annotation.poet.JavaFile;
import com.king.annotation.poet.MethodSpec;
import com.king.annotation.poet.ParameterizedTypeName;
import com.king.annotation.poet.TypeName;
import com.king.annotation.poet.TypeSpec;

import android.text.TextUtils;

/**
 * Model类自动生成器
 * @author zlq
 * @date 2017年8月28日 下午5:35:08
 * @version 1.0
 */
public class DHModelProcessor implements IProcessor {

	ClassName mActivityClassName = ClassName.get("android.app", "Activity"); // Activity类
	ClassName configClass = ClassName.get(Configs.CONFIG_DIR, Configs.CONFIG_CLASS_NAME); // 配置类
	ClassName superClass = ClassName.get(Configs.CLASS_DIR, Configs.SUPER_CLASS_NAME); // 继承类
	ClassName callBackClass = ClassName.get(Configs.CLASS_DIR, Configs.CALL_BACK_NAME); // 回调类
	ClassName typeTokenClass = ClassName.get(Configs.TYPET_TOKEN_DIR, Configs.TYPET_TOKEN_NAME); // 类型类
	
	@Override
	public void process(RoundEnvironment roundEnv, KingProcessor mAbstractProcessor) {
		try {
			// 获取所有被DHModel注解的元素
			for (Element element : roundEnv.getElementsAnnotatedWith(DHModel.class)) {
				
				DHModel model = element.getAnnotation(DHModel.class);
				
				ClassName genericsClass = ClassName.get(Configs.GENERICS_CLASS_DIR, model.genericsClassName()); // 泛型类
				
				TypeName typeToken = ParameterizedTypeName.get(typeTokenClass, genericsClass);
				
				TypeName clazz = ParameterizedTypeName.get(ClassName.get("java.lang", "Class"), genericsClass);
				
				//生成public void getURL()方法
		        MethodSpec.Builder getURLBuilder = MethodSpec.methodBuilder("getURL")
		                .addModifiers(Modifier.PUBLIC)
		                .addAnnotation(Override.class)
		                .returns(String.class)
		                .addStatement("return $T.getIP() + $S", configClass, model.url());
				
		        //生成public void getType()方法
		        MethodSpec.Builder getTypeBuilder = MethodSpec.methodBuilder("getType")
		                .addModifiers(Modifier.PUBLIC)
		                .addAnnotation(Override.class)
		                .returns(Type.class)
		                .addStatement("return new $T(){}.getType()", typeToken);
		        
		        //生成public void getClazz()方法
		        MethodSpec.Builder getClazzBuilder = MethodSpec.methodBuilder("getClazz")
		                .addModifiers(Modifier.PUBLIC)
		                .addAnnotation(Override.class)
		                .returns(clazz)
		                .addStatement("return $T.class", genericsClass);
		        
		        //生成public void saveJsonType()方法
		        MethodSpec.Builder saveJsonTypeBuilder = MethodSpec.methodBuilder("saveJsonType")
		                .addModifiers(Modifier.PROTECTED)
		                .addAnnotation(Override.class)
		                .returns(int.class)
		                .addStatement("return " + model.saveJsonType());
		        
		        //生成public void doNet()方法
		        MethodSpec.Builder doNetBuilder = MethodSpec.methodBuilder("doNet")
		                .addModifiers(Modifier.PUBLIC)
		                .returns(void.class);
	        	for (int i = 0; i < model.params().length; i++) {
	        		if(model.params()[i] != null && model.params()[i].length() != 0) {
	        			doNetBuilder.addParameter(String.class, model.params()[i], Modifier.FINAL);
	        			doNetBuilder.addStatement("mPostValues.put($S, " + model.params()[i] + ")", model.params()[i]);
	        		}
	        	}
		        doNetBuilder.addStatement("requestByPost()");
		        
		        TypeSpec finderClass = TypeSpec.classBuilder(model.className())
		                .addModifiers(Modifier.PUBLIC)
		                // 添加继承
		                .superclass(ParameterizedTypeName.get(superClass, genericsClass))
		                // 添加构造函数
		                .addMethod(MethodSpec.constructorBuilder()
		                		.addModifiers(Modifier.PUBLIC)
		                        .addParameter(mActivityClassName, "activity")
		                        .addParameter(ParameterizedTypeName.get(callBackClass, genericsClass), "callback")
		                        .addStatement("super(activity, callback)")
		                        .build())
		                .addMethod(getURLBuilder.build())
		                .addMethod(getTypeBuilder.build())
		                .addMethod(getClazzBuilder.build())
		                .addMethod(saveJsonTypeBuilder.build())
		                .addMethod(doNetBuilder.build())
		                .build();
	
		        // 添加包名
		        String packageName = Configs.FILE_DIR;
		        // 生成file文件
		        JavaFile javaFile = JavaFile.builder(packageName, finderClass).build();
		        // 输出最终文件
		        javaFile.writeTo(mAbstractProcessor.mFiler);
			}
		} catch (FilerException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
