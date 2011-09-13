package net.flipback.xpca.core;

import java.util.HashSet;

import net.flipback.xpca.AllTests;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;


public class XGroupTest extends TestCase {
	private Session session;
	private XGroup root, group_1;
	private XObject object_1;
	
	@Before
	public void setUp() throws Exception {
		session = AllTests.sessionFactory.openSession();
		session.beginTransaction();
		root = (XGroup) session.createQuery("from XObject where fullName=:name").setString("name", "/").uniqueResult();
		group_1 = (XGroup) session.createQuery("from XObject where fullName=:name").setString("name", "/group_1").uniqueResult();
		object_1 = (XObject) session.createQuery("from XObject where fullName=:name").setString("name", "/group_1/object_1").uniqueResult();
		session.getTransaction().commit();	
	}
	
	@Test
	public void testInitialization() {
		XGroup group = new XGroup();
		assertTrue(group.getName().equals("new_group"));
		assertTrue(group.getChildren().equals(new HashSet<XObject>()));
	}
	
	@Test
	public void testHaveChildren() {
		assertEquals(group_1, root.getChildren().toArray()[0]);
	}
	
	@Test
	public void testGetObj() {
		assertEquals(object_1, root.getObject("group_1/object_1"));
		assertEquals(object_1, root.getObject("./group_1/object_1"));
		assertEquals(null, root.getObject("error_in_path/object_1"));
		assertEquals(root, root.getObject(""));
	}
	
	@Test
	public void testToString() {
		String str = root.toString(); 
		assertTrue(str.contains("<XGroup:" + root.hashCode() +">"));
		assertTrue(str.contains("Name=/"));
		assertTrue(str.contains("ID=" + root.getId()));
	}
	
	@After
	public void downThread() throws Exception {
		session.close();
	}
}
