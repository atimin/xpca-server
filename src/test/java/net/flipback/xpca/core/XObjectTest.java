package net.flipback.xpca.core;

import junit.framework.TestCase;
import net.flipback.xpca.AllTests;
import net.flipback.xpca.core.XObject;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XObjectTest extends TestCase {
	private Session session;
	private XObject obj, other_obj;
	private XGroup root;
	
	@Before
	public void setUp() throws Exception {
		session = AllTests.sessionFactory.openSession();
		session.beginTransaction();
		obj = (XObject) session.createQuery("from XObject where name=:name").setString("name", "object_1").uniqueResult();
		root = (XGroup) session.createQuery("from XGroup where name=:name").setString("name", "/").uniqueResult();
		session.getTransaction().commit();	
		
		other_obj = new XObject();
		other_obj.setId(obj.getId());
		other_obj.setName(obj.getName());
		other_obj.setGroup(obj.getGroup());
	}
	
	@Test
	public void testGetId() {
		assertTrue(obj.getId() > 0);
	}
	
	@Test
	public void testGetName() {
		assertTrue(obj.getName().equals("object_1"));
	}
	
	@Test
	public void testGetFullName() {
		assertTrue(obj.getFullName().equals("/group_1/object_1"));
	}
	
	@Test
	public void testGenNewFullName() {
		obj.setName("new_name");
		assertTrue(obj.getFullName().equals("/group_1/new_name"));
	}
	
	@Test
	public void testGetGroup() {
		assertTrue(obj.getGroup().getName().equals("group_1"));
	}
	
	@Test
	public void testToString() {
		String str = obj.toString(); 
		assertTrue(str.contains("<XObject:" + obj.hashCode() +">"));
		assertTrue(str.contains("Name=object_1"));
		assertTrue(str.contains("ID=" + obj.getId()));
		assertTrue(str.contains("Group=" + obj.getGroup().getFullName()));
	}

	@Test
	public void testEquals() {
		assertTrue(obj.equals(other_obj));
		
		other_obj.setId(obj.getId() + 1);
		assertFalse(obj.equals(other_obj));
	}
	
	@Test
	public void testHashCode() {
		assertEquals(other_obj.hashCode(), obj.hashCode());
	
		other_obj.setName(obj.getName() + ".");
		assertFalse(other_obj.hashCode() == obj.hashCode());
	}
	
	@Test
	public void testRoot() {
		assertEquals(obj.getRoot(), root);
	}	
	
	@After
	public void downThread() throws Exception {
		session.close();
	}
}
