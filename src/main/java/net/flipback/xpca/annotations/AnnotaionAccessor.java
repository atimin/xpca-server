package net.flipback.xpca.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

import net.flipback.xpca.core.XObject;

public class AnnotaionAccessor {
	public static Dictionary<Field, Object> getFieldValues(XObject xobj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Dictionary<Field, Object>  fields = new  Hashtable<Field, Object> ();
		for (Method method : xobj.getClass().getMethods()) {
			Field field = method.getAnnotation(Field.class);
			if (field != null) {			
				Object result = method.invoke(xobj);
				if (result != null)
					fields.put(field, result);
			}
		} 
		return fields;
	}
	
	public static Dictionary<Field, String> getFieldNames(XObject xobj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Dictionary<Field, String>  fields = new  Hashtable<Field, String> ();
		for (Method method : xobj.getClass().getMethods()) {
			Field field = method.getAnnotation(Field.class);
			if (field != null) {			
				String result = method.getName().replaceFirst("get", "");
				result = result.substring(0,1).toLowerCase() + result.substring(1, result.length());
				if (result != null)
					fields.put(field, result);
			}
		} 
		return fields;
	}
	
	public static HashSet<Object> calcFields(XObject xobj, ActionField action) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		HashSet<Object> result = new HashSet<Object>();
		Dictionary<Field, Object> fields = getFieldValues(xobj);
		
		for (Enumeration<Field> field = fields.keys(); field.hasMoreElements(); ) {
			Field f = field.nextElement();
			result.add(action.calcField(f, fields.get(f)));
		}
		
		return result;
	}
}
