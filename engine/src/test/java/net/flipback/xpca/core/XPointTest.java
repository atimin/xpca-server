package net.flipback.xpca.core;

import net.flipback.xpca.AllTests;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class XPointTest extends TestCase {
	private Session session;
	private XPoint point;
	
	@Before
	public void setUp() throws Exception {
		session = AllTests.sessionFactory.openSession();
		session.beginTransaction();
		point = (XPoint) session.createQuery("from XPoint where name=:name").setString("name", "point_1").uniqueResult();
		session.getTransaction().commit();	
	}
	
	@Test
	public void testInitialization() {
		XPoint point = new XPoint();
		assertTrue(point.getName().equals("new_point"));
		assertNull(point.getGroup());
	}
	
	@Test
	public void testToString() {
		String str = point.toString(); 
		assertTrue(str.equals("<XPoint:" + point.hashCode() + 
				">(ID=" + point.getId() + 
				",Name=" + point.getName() + 
 				",Group=" + point.getGroup().getFullName() +
				",Value=" + point.getValue() +
				",Timestamp=" + point.getTimestamp() +
				",Quality=" + point.getQuality() + ")"));
	}
	
	@After
	public void downThread() throws Exception {
		session.close();
	}
}
