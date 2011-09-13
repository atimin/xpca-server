package net.flipback.xpca.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;

import net.flipback.xpca.core.XObject;

public class AnnotaionAccessor {
	public static LinkedHashMap<Field, Object> getFieldValues(XObject xobj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		LinkedHashMap<Field, Object>  fields = new  LinkedHashMap<Field, Object> ();
		
		for (FieldInfo fi : getInfoFields(xobj)) {		
			Object result = fi.getMethod().invoke(xobj);
			if (result != null)
				fields.put(fi.getField(), result);
		} 
		return fields;
	}
	
	public static LinkedHashMap<Field, Object> getFieldNames(XObject xobj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		LinkedHashMap<Field, Object>  fields = new  LinkedHashMap<Field, Object> ();
		
		for (FieldInfo fi : getInfoFields(xobj)) {
			String result = fi.getMethod().getName().replaceFirst("get", "");
			result = result.substring(0,1).toLowerCase() + result.substring(1, result.length());
			if (result != null)
				fields.put(fi.getField(), result);			
		} 

		return fields;
	}

	public static HashSet<Object> calcFields(XObject xobj, ActionField action) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		HashSet<Object> result = new HashSet<Object>();
		LinkedHashMap<Field, Object> fields = getFieldValues(xobj);
		
		for (Field field : fields.keySet()) {
			result.add(action.calcField(field, fields.get(field)));
		}
		
		return result;
	}
	
	public static ArrayList<FieldInfo> getInfoFields(XObject xobj) {
		ArrayList<FieldInfo> sorted_fields = new ArrayList<FieldInfo>(0);
		
		for (Method method : xobj.getClass().getMethods()) {
			Class<?> klass =  method.getDeclaringClass();
			Field field = method.getAnnotation(Field.class);
			if (field != null) {			
				sorted_fields.add(new FieldInfo(field, klass, method));
			}
		}
		
		Collections.sort(sorted_fields, new FieldOrder());
		
		return sorted_fields;
	}
}
