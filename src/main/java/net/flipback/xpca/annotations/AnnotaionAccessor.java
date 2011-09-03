package net.flipback.xpca.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

import net.flipback.xpca.core.XObject;

public class AnnotaionAccessor {
	public static Dictionary<Field, Object> getFields(XObject xobj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Dictionary<Field, Object>  fileds = new  Hashtable<Field, Object> ();
		for (Method method : xobj.getClass().getMethods()) {
			Field field = method.getAnnotation(Field.class);
			if (field != null) {			
				Object result = method.invoke(xobj);
				if (result != null)
					fileds.put(field, result);
			}
		} 
		return fileds;
	}
	
	public static HashSet<Object> calcFields(XObject xobj, ActionField action) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		HashSet<Object> result = new HashSet<Object>();
		Dictionary<Field, Object> fields = getFields(xobj);
		
		for (Enumeration<Field> field = fields.keys(); field.hasMoreElements(); ) {
			Field f = field.nextElement();
			result.add(action.calcField(f, fields.get(f)));
		}
		
		return result;
	}
}
