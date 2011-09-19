package net.flipback.xpca.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name="groups")
public class XGroup extends XObject {	
	@OneToMany
	@JoinColumn(name="object_id")
	@OrderBy(value="name")
	public Set<XObject> getChildren() { return children; }
	public void setChildren(Set<XObject> children) { this.children = children; 	}
	private Set<XObject> children;
	
	public XObject getChild(String name) {
		XObject child = null;	
		for (XObject xobj : children) {
			if (xobj.getName().equals(name)) {
				child = xobj;
			}
		}
		return child;
	}
	public XObject getObject(String path) {
		XObject result = null;
		
		if (path.equals("")) {
			return this;
		}
		
		if (children != null) {
			String[] split_path = path.split("/");
			
			if (split_path[0].equals("."))
				split_path = Arrays.copyOfRange(split_path, 1, split_path.length);
			
			for (XObject child : children) {
				if (child.getName().equals(split_path[0])) {
					if (split_path.length == 1) {
						result = child;
					} else {
						if (child instanceof XGroup) {
							result = ((XGroup)child).getObject(
									StringUtils.join(Arrays.copyOfRange(split_path, 1, split_path.length)));
						}
					}
				}
					
			}
		}
		return result;
	}
	
	@Override
	public void genFullName() {
		super.genFullName();
		
		if (children != null) {
			for (XObject child : children) {
				child.genFullName();
			}
		}
	}
	
	public XGroup() {		
		super();
		this.setName("new_group");
	}
	
	public XGroup(String name) {
		super(name);
	}
	
	{
		setChildren(new HashSet<XObject>());
	}
}
