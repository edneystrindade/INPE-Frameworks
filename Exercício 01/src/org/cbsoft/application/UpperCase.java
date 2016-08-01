package org.cbsoft.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.cbsoft.framework.FormatterImplementation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@FormatterImplementation(UpperCaseFormatter.class)

public @interface UpperCase {
	
}
