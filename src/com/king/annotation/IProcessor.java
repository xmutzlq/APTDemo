package com.king.annotation;

import javax.annotation.processing.RoundEnvironment;

public interface IProcessor {
	void process(RoundEnvironment roundEnv, KingProcessor mAbstractProcessor);
}
