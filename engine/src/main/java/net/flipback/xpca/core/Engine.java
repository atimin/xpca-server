package net.flipback.xpca.core;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

public class Engine {
	private SessionFactory sessionFactory;
	private Session session;

	public void addObject(String path, XObject xobj) {		
		XGroup group = (XGroup)getObject(path);
		
		if (group != null) {
			xobj.setGroup(group);			
			
			session.save(xobj);
			session.flush();
		}
	}
	
	public void deleteObject(String path) {	
		XObject xobj = (XObject)getObject(path);
		
		if (xobj != null) {
			session.delete(xobj);
			session.flush();
		}
	}
	
	public void updateObject(String path, XObject xobj) {
		XObject old_obj = (XObject)getObject(path);
		
		if (xobj != null) {
			xobj.setId(old_obj.getId());
			session.merge(xobj);
			session.flush();
		}
	}
	
	public XObject getObject(String path) {	
		return (XObject) session.createQuery("from XObject where fullName = ?").setParameter(0, path).uniqueResult();
	}
		
	public Engine(Configuration cfg)
	{
		sessionFactory = cfg.buildSessionFactory();
		Session session = sessionFactory.openSession();
		
		// Create root group
		session.save(new XGroup("/"));
		
		session.close();
	}
	
	public void start() {
		session = sessionFactory.openSession();
	}
	
	public void stop() {
		session.close();
	}
}
