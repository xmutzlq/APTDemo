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
 * Model���Զ�������
 * @author zlq
 * @date 2017��8��28�� ����5:35:08
 * @version 1.0
 */
public class DHModelProcessor implements IProcessor {

	ClassName mActivityClassName = ClassName.get("android.app", "Activity"); // Activity��
	ClassName configClass = ClassName.get(Configs.CONFIG_DIR, Configs.CONFIG_CLASS_NAME); // ������
	ClassName superClass = ClassName.get(Configs.CLASS_DIR, Configs.SUPER_CLASS_NAME); // �̳���
	ClassName callBackClass = ClassName.get(Configs.CLASS_DIR, Configs.CALL_BACK_NAME); // �ص���
	ClassName typeTokenClass = ClassName.get(Configs.TYPET_TOKEN_DIR, Configs.TYPET_TOKEN_NAME); // ������
	
	@Override
	public void process(RoundEnvironment roundEnv, KingProcessor mAbstractProcessor) {
		try {
			// ��ȡ���б�DHModelע���Ԫ��
			for (Element element : roundEnv.getElementsAnnotatedWith(DHModel.class)) {
				
				DHModel model = element.getAnnotation(DHModel.class);
				
				ClassName genericsClass = ClassName.get(Configs.GENERICS_CLASS_DIR, model.genericsClassName()); // ������
				
				TypeName typeToken = ParameterizedTypeName.get(typeTokenClass, genericsClass);
				
				TypeName clazz = ParameterizedTypeName.get(ClassName.get("java.lang", "Class"), genericsClass);
				
				//����public void getURL()����
		        MethodSpec.Builder getURLBuilder = MethodSpec.methodBuilder("getURL")
		                .addModifiers(Modifier.PUBLIC)
		                .addAnnotation(Override.class)
		                .returns(String.class)
		                .addStatement("return $T.getIP() + $S", configClass, model.url());
				
		        //����public void getType()����
		        MethodSpec.Builder getTypeBuilder = MethodSpec.methodBuilder("getType")
		                .addModifiers(Modifier.PUBLIC)
		                .addAnnotation(Override.class)
		                .returns(Type.class)
		                .addStatement("return new $T(){}.getType()", typeToken);
		        
		        //����public void getClazz()����
		        MethodSpec.Builder getClazzBuilder = MethodSpec.methodBuilder("getClazz")
		                .addModifiers(Modifier.PUBLIC)
		                .addAnnotation(Override.class)
		                .returns(clazz)
		                .addStatement("return $T.class", genericsClass);
		        
		        //����public void saveJsonType()����
		        MethodSpec.Builder saveJsonTypeBuilder = MethodSpec.methodBuilder("saveJsonType")
		                .addModifiers(Modifier.PROTECTED)
		                .addAnnotation(Override.class)
		                .returns(int.class)
		                .addStatement("return " + model.saveJsonType());
		        
		        //����public void doNet()����
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
		                // ��Ӽ̳�
		                .superclass(ParameterizedTypeName.get(superClass, genericsClass))
		                // ��ӹ��캯��
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
	
		        // ��Ӱ���
		        String packageName = Configs.FILE_DIR;
		        // ����file�ļ�
		        JavaFile javaFile = JavaFile.builder(packageName, finderClass).build();
		        // ��������ļ�
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
