package net.flipback.xpca.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import net.flipback.xpca.annotations.ActionField;
import net.flipback.xpca.annotations.AnnotaionAccessor;
import net.flipback.xpca.annotations.Field;
import net.flipback.xpca.annotations.Field.InputType;

@Entity
@Table(name="objects")
public class XObject {
	public XObject() {
		this.setName("new_object");
	}
	
	public XObject(String name) {
		this.setName(name);
	}
	
	@Id
	@GeneratedValue
	@Field(title="ID")
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	private Long id;
	
	@Field(title="Name", inputType=InputType.TEXT)
	public String getName() { return name; }
	public void setName(String name) { 
		this.name = name;
		genFullName();
	}
	private String name;	
	
	@ManyToOne
	@JoinColumn(name="object_id")
	@Field(title="Group")
	public XGroup getGroup() { return group; }
	public void setGroup(XGroup group) { 
		this.group = group;
		genFullName();
	}
	private XGroup group;
	
	public String getFullName() { return fullName; }
	public void setFullName(String fullName) { this.fullName = fullName; }
	private String fullName;
	
	@Transient
	public XGroup getRoot() {
		XGroup root = null;
		if (group == null && this instanceof XGroup) {
			root = (XGroup)this;
		} else {
			root = group.getRoot();
		}
		return root;
	}
	@Override
	public String toString() {
		String result = "<" + this.getClass().getSimpleName() + ":" + this.hashCode() + ">(";

		ActionField stringBuilder = new ActionField() {
			public Object calcField (Field field, Object value) {
				String result = "";
				result += field.title();
				if (value instanceof XObject) {
					XObject xobj = (XObject)value;
					result += "=" + xobj.getFullName();
				} else {	
					result += "=" + value.toString();
				}
				return result;
			}
		};
		
		try {
			result += StringUtils.join(AnnotaionAccessor.calcFields(this, stringBuilder).toArray(), ",");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result += ")";
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (obj == null)
			return false;
		
		if (!obj.getClass().equals(this.getClass()))
			return false;
	
		if (obj.hashCode() != this.hashCode())
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		long sum = 0;
		
		ActionField hashFields = new ActionField() {
			public Object calcField (Field field, Object value) {
				long result = 0;
				if (value != null) {
					if (value instanceof XObject) {
						value = ((XObject)value).fullName;
					}
					for (char chr : value.toString().toCharArray()) {
						result += chr;
					}
				}
				return (int)(0x9e370001 * result) >> 32;
			}
		};	
		
		try {
			for (Object h : AnnotaionAccessor.calcFields(this, hashFields).toArray()) {
				sum += (Integer)h;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return (int)((0x9e370001 * sum) >> 32);
	}
	
	private void genFullName(){
		String fullName = this.getName();

		if (this.getGroup() != null){
			XGroup grp = this.getGroup();
			if (grp.getFullName().equals("/")) {
				fullName = grp.getFullName() + fullName;
			} 
			else { 
				fullName = grp.getFullName() + "/" + fullName;
			}
		}
		
		this.fullName = fullName;
	}
}
