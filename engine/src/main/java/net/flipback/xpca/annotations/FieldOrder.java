package net.flipback.xpca.annotations;

import java.util.Comparator;

public class FieldOrder implements Comparator<FieldInfo>{

	@Override
	public int compare(FieldInfo f1, FieldInfo f2) {
		if (f1.getKlass() == f2.getKlass()) {
			if (f1.getField().order() < f2.getField().order())
				return -1;
			if (f1.getField().order() > f2.getField().order())
				return 1;
		} else {
			return compareClasses(f1.getKlass(), f2.getKlass());
			
		}
		return 0;
	}
	
	private int compareClasses(Class<?> k1, Class<?> k2)
	{
		if (k1 != k2) {
			if (k1 == k2.getSuperclass())
				return -1;
			if (k1.getSuperclass() == k2)
				return 1;
			if (k1.getSuperclass() != null)
				return compareClasses(k1.getSuperclass(), k2);
		}
		return 0;
	}
}
