package net.flipback.xpca.core;

import org.hibernate.SessionFactory;
import org.hibernate.Session;

public class Engine {
	private SessionFactory sessionFactory;
	private XGroup root;

	public void addObject(String path, XObject xobj) {		
		XGroup group = (XGroup)getObject(path);
		
		if (group != null) {
			xobj.setGroup(group);
			group.getChildren().add(xobj);
		}
	}
	
	public void deleteObject(String path) {	
		XObject xobj = (XObject)getObject(path);
		
		if (xobj != null) {
			xobj.getGroup().getChildren().remove(xobj);
		}
	}
	
	public void updateObject(String path, XObject xobj) {
		XObject old_obj = (XObject)getObject(path);
		
		if (old_obj != null) {
			xobj.setId(old_obj.getId());
			xobj.setGroup(old_obj.getGroup());
			
			deleteObject(path);
			addObject(old_obj.getGroup().getFullName(), xobj);
		}
	}
	
	public XObject getObject(String path) {	
		XObject xobj = root;
		
		if (path.equals("") || path.endsWith("/")) {
			return xobj;
		}
		
		String[] split_path = path.split("/");
		
		for (String name : split_path) {
			if (xobj == null) {
				break;
			}
			
			if (name.length() > 0 && xobj instanceof XGroup) {
				xobj = ((XGroup)xobj).getChild(name);
			}
		}
		return xobj;
	}
		
	public Engine(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;

		revert();	
		if (root == null) {
			root = new XGroup("/");
			commit();
		}
	}
	
	public void commit() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		commitObject(root, session);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public void revert() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		root = (XGroup) session.createQuery("from XObject where fullName = ?").setParameter(0, "/").uniqueResult();
		
		session.getTransaction().commit();
		session.close();
	}
	
	private void commitObject(XObject xobj, Session session) {
		if (xobj instanceof XGroup) {
			XGroup group = (XGroup) xobj;
			for( XObject child : group.getChildren()) {
				commitObject(child, session);
			}
		} 
		session.saveOrUpdate(xobj);
	}
	
}
