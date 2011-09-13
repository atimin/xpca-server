package net.flipback.xpca.annotations;

import java.lang.reflect.Method;

public class FieldInfo {
	private Field field;
	public Field getField() { return field; }
	
	private Class<? >klass;
	public Class<?> getKlass() { return klass; 	}
	
	private Method method;
	public Method getMethod() {	return method;	}

	public FieldInfo(Field field, Class<?> klass, Method method) {
		this.field = field;
		this.klass = klass;
		this.method = method;
	}
}