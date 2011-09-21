package net.flipback.xpca.core;


import junit.framework.TestCase;

import net.flipback.xpca.AllTests;

import org.junit.Before;
import org.junit.Test;

public class EngineTest extends TestCase {
	private Engine eng;
	
	@Before
	public void setUp() throws Exception {
		eng = new Engine(AllTests.sessionFactory);
	}
	
	
	@Test
	public void testAddObject() {
		XObject xobj = new XObject("added_object");
		eng.addObject("/", xobj);
		assertEquals(xobj, eng.getObject("/added_object"));
	}
	
	@Test 
	public void testDeleteObject() {
		assertNotNull(eng.getObject("/group_1/object_1"));
		
		eng.deleteObject("/group_1/object_1");
		assertNull(eng.getObject("/group_1/object_1"));
	}
	
	@Test 
	public void testUpdateObject() {
		XObject xobj = new XObject("new_name");
		eng.updateObject("/group_1/object_1", xobj);
		assertNull(eng.getObject("/group_1/object_1"));
		assertEquals(xobj, eng.getObject("/group_1/new_name"));
	}
	
	@Test
	public void testRevert() {
		testAddObject();
		
		eng.revert();
		assertNull(eng.getObject("/added_object"));
	}
	
	@Test
	public void testCustumRootName() {
		Engine eng = new Engine(AllTests.sessionFactory, "/root");
		eng.addObject("/root", new XObject("test"));
		
		assertTrue(eng.getObject("/root/test").getName().equals("test"));
	}
	
	@Test 
	public void testCommit() {
		testAddObject();
		eng.addObject("/", new XGroup("group"));
		eng.addObject("/group", new XObject("obj"));
		
		eng.commit();
		eng.revert();
		assertNotNull(eng.getObject("/group/obj"));
	}
	
}
