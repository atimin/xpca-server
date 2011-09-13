package net.flipback.xpca.annotations;

import java.util.Comparator;

public class FieldOrder implements Comparator<Field>{

	@Override
	public int compare(Field f1, Field f2) {
		int result = 0;
		if (f1.order() < f2.order())
			result = -1;
		if (f1.order() > f2.order())
			result = 1;
		return result;
	}

}
