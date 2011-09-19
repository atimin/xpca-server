package net.flipback.xpca.core;


import junit.framework.TestCase;

import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EngineTest extends TestCase {
	private Engine eng;
	
	@Before
	public void setUp() throws Exception {
		eng = new Engine(new Configuration().configure());
		eng.start();
	}
	
	
	@Test
	public void testAddObject() {
		XObject xobj = new XObject("added_object");
		eng.addObject("/", xobj);
		assertEquals(xobj, eng.getObject("/added_object"));
	}
	
	@Test 
	public void testDeleteObject() {
		XObject xobj = new XObject("added_object");
		eng.addObject("/", xobj);
		assertEquals(xobj, eng.getObject("/added_object"));
		
		eng.deleteObject("/added_object");
		assertNull(eng.getObject("/added_object"));
	}
	
	@Test 
	public void testUpdateObject() {
		XObject xobj = new XObject("added_object");
		eng.addObject("/", xobj);
		assertEquals(xobj, eng.getObject("/added_object"));
		
		xobj = new XObject("new_name");
		xobj.setGroup((XGroup)eng.getObject("/"));
		eng.updateObject("/added_object", xobj);
		assertNull(eng.getObject("/added_object"));
		assertEquals(xobj, eng.getObject("/new_name"));
	}

	@After
	protected void tearDown() throws Exception {
		eng.stop();
	}
	
}
