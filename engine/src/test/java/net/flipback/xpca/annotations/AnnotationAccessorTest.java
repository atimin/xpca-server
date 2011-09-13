package net.flipback.xpca.annotations;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

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
		HashMap<Field, Object> fields = AnnotaionAccessor.getFieldValues(obj);
		Field[] keys = fields.keySet().toArray(new Field[fields.size()]);
		
		assertTrue(keys[0].title().equals("ID"));
	    assertEquals(fields.get(keys[0]), obj.getId());
	      
	    assertTrue(keys[1].title().equals("Name"));
		assertEquals(fields.get(keys[1]), obj.getName());

		assertTrue(keys[2].title().equals("Group"));
	    assertEquals(fields.get(keys[2]), obj.getGroup());
	}
	
	public void testGetFieldNames() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		HashMap<Field, Object> fields = AnnotaionAccessor.getFieldNames(obj);
		Field[] keys = fields.keySet().toArray(new Field[fields.size()]);
	
	    assertTrue(keys[0].title().equals("ID"));
	    assertEquals(fields.get(keys[0]), "id");
	    	    
		assertTrue(keys[1].title().equals("Name"));
		assertEquals(fields.get(keys[1]), "name");

	    assertTrue(keys[2].title().equals("Group"));
	    assertEquals(fields.get(keys[2]), "group");
	}
	
	@After
	public void tearDown() throws Exception {
		session.close();
	}
}
