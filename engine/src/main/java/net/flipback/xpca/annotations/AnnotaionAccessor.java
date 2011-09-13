package net.flipback.xpca.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import net.flipback.xpca.core.XObject;

public class AnnotaionAccessor {
	public static LinkedHashMap<Field, Object> getFieldValues(XObject xobj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Dictionary<Field, Object>  fields = new  Hashtable<Field, Object> ();
		for (Method method : xobj.getClass().getMethods()) {
			Field field = method.getAnnotation(Field.class);
			if (field != null) {			
				Object result = method.invoke(xobj);
				if (result != null)
					fields.put(field, result);
			}
		} 
		return sortFields(fields);
	}
	
	public static LinkedHashMap<Field, Object> getFieldNames(XObject xobj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Dictionary<Field, Object>  fields = new  Hashtable<Field, Object> ();
		for (Method method : xobj.getClass().getMethods()) {
			Field field = method.getAnnotation(Field.class);
			if (field != null) {			
				String result = method.getName().replaceFirst("get", "");
				result = result.substring(0,1).toLowerCase() + result.substring(1, result.length());
				if (result != null)
					fields.put(field, result);
			}
		} 

		return sortFields(fields);
	}

	public static HashSet<Object> calcFields(XObject xobj, ActionField action) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		HashSet<Object> result = new HashSet<Object>();
		LinkedHashMap<Field, Object> fields = getFieldValues(xobj);
		
		for (Field field : fields.keySet()) {
			result.add(action.calcField(field, fields.get(field)));
		}
		
		return result;
	}
	
	private static LinkedHashMap<Field, Object> sortFields(Dictionary<Field, Object> fields) {
		ArrayList<Field> sorted_keys = Collections.list(fields.keys());
		Collections.sort(sorted_keys, new FieldOrder());
		LinkedHashMap<Field, Object>  sorted_fields = new  LinkedHashMap<Field, Object> ();
		
		for (Field field : sorted_keys) {
			sorted_fields.put(field, fields.get(field));
		}
		return sorted_fields;
	}
}
