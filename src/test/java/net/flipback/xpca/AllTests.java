package net.flipback.xpca;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

import net.flipback.xpca.core.XGroup;
import net.flipback.xpca.core.XGroupTest;
import net.flipback.xpca.core.XObject;
import net.flipback.xpca.core.XObjectTest;
import net.flipback.xpca.core.XPoint;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	public final static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
	
	
	@SuppressWarnings("unchecked")
	public static Test suite() {
		InitDB();
		
		Class[] testClasses  = { XObjectTest.class, XGroupTest.class };
		TestSuite suite = new TestSuite(testClasses);
		
		return suite;
	}
	
	private static void InitDB(){
		Session session =  sessionFactory.openSession();	
		session.beginTransaction();
		// Create root group
		XGroup root = new XGroup("/");
		session.save(root);
		// Create groups into root
		XGroup group_1 = new XGroup("group_1");
		group_1.setGroup(root);
		session.save(group_1);
		
		// Create objects into group_1
		XObject object_1 = new XObject("object_1");
		object_1.setGroup(group_1);
		session.save(object_1);
		
		XPoint point_1 = new XPoint("point_1");
		point_1.setGroup(group_1);
		session.save(point_1);
		
		session.getTransaction().commit();
		session.close();
	}

}
