package com.king.annotation;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import com.king.annotation.apt.AutoCreat;
import com.king.annotation.apt.DHModel;
import com.king.annotation.processor.DHModelProcessor;

public class KingProcessor extends AbstractProcessor {

	public Elements mElementUtils;
	public Filer mFiler;
	public Messager mMessager;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		// ��ʼ��������Ҫ�Ļ�������
		mElementUtils = processingEnv.getElementUtils();
		mFiler = processingEnv.getFiler();
		mMessager = processingEnv.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		new DHModelProcessor().process(roundEnv, this);
		return true;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		// ֧�ֵ�java�汾
		return SourceVersion.latestSupported();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		// ֧�ֵ�ע��
		Set<String> annotations = new LinkedHashSet<String>();
		annotations.add(DHModel.class.getCanonicalName());
		annotations.add(AutoCreat.class.getCanonicalName());
		return annotations;
	}
	
	public void error(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
	
	public void warning(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.WARNING, String.format(msg, args), e);
    }
}
