package net.flipback.xpca.core;

import org.hibernate.SessionFactory;
import org.hibernate.Session;

public class Engine {
	private SessionFactory sessionFactory;
	private XGroup root = new XGroup("/");
	public XGroup getRoot() {
		return root;
	}

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
		return getObject(path, root);
	}
	
	public Engine(SessionFactory sessionFactory, String root_name) {
		this.sessionFactory = sessionFactory;

		getRoot(root_name);
	}
		
	public Engine(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;

		getRoot("/");
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
		
		root = (XGroup) session.createQuery("from XObject where fullName = ?").setParameter(0, root.getFullName()).uniqueResult();
		
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
	
	private void getRoot(String root_name) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		root = (XGroup) session.createQuery("from XObject where fullName = ?").setParameter(0, root_name).uniqueResult();
		
		session.getTransaction().commit();
		session.close();
		
		if (root == null) {
			root = new XGroup(root_name);
			commit();
		}
	}
	
	private XObject getObject(String path, XObject xobj) {
		if (xobj.getName().equals(path)) {
			return xobj;
		}
		
		XObject result = null;
		
		path = path.replaceFirst(xobj.getName(), "");
		if (!xobj.getName().equals("/")) {
			path = path.replaceFirst("/", "");
		}
		
		if (xobj instanceof XGroup) {
			XGroup group = (XGroup)xobj;
			
			for (XObject child : group.getChildren()) {
				result = getObject(path, child);
				if (result != null) {
					break;
				}
			}
		}
		
		return result;
	}
	
}
