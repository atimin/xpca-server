package net.flipback.xpca.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
	public enum InputType {
		NONE,TEXT
	}
	
	String title();
	int order();
	InputType inputType() default InputType.NONE;
}
