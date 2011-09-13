package net.flipback.xpca.annotations;


import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.Enumeration;

import junit.framework.TestCase;

import net.flipback.xpca.AllTests;
import net.flipback.xpca.core.XObject;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnnotationAccessorTest extends TestCase {
	private Session session;
	private XObject obj;
	
	@Before
	public void setUp() throws Exception {
		session = AllTests.sessionFactory.openSession();
		session.beginTransaction();
		obj = (XObject) session.createQuery("from XObject where name=:name").setString("name", "object_1").uniqueResult();
		session.getTransaction().commit();	
	}

	@Test
	public void testGetFieldValues() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Dictionary<Field, Object> fields = AnnotaionAccessor.getFieldValues(obj);
		for(Enumeration<Field> keys = fields.keys(); keys.hasMoreElements();) {
			Field key = keys.nextElement();
			if (key.title().equals("Name"))
				assertEquals(fields.get(key), obj.getName());
			if (key.title().equals("ID"))
				assertEquals(fields.get(key), obj.getId());
			if (key.title().equals("Group"))
				assertEquals(fields.get(key), obj.getGroup());
		}
	}
	
	public void testGetFieldNames() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Dictionary<Field, String> fields = AnnotaionAccessor.getFieldNames(obj);
		for(Enumeration<Field> keys = fields.keys(); keys.hasMoreElements();) {
			Field key = keys.nextElement();
			if (key.title().equals("ame"))
				assertEquals(fields.get(key), "name");
			if (key.title().equals("ID"))
				assertEquals(fields.get(key), "id");
			if (key.title().equals("Group"))
				assertEquals(fields.get(key), "group");
		}
	}
	
	@After
	public void tearDown() throws Exception {
		session.close();
	}
}
