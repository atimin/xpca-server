package net.flipback.xpca.core;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.flipback.xpca.annotations.Field;

@Entity
@Table(name="points")
public class XPoint extends XObject {
	@Field(title="Value", order=0)
	public Double getValue() { return value; }
	public void setValue(Double value) { this.value = value; }
	private Double value = 0.0;
	
	@Field(title="Timestamp", order=1)
	public Date getTimestamp() { return timestamp; }
	public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
	private Date timestamp = new Date();
	
	@Field(title="Quality", order=2)
	public Quality getQuality() { return quality; }
	public void setQuality(Quality quality) { this.quality = quality; }
	private Quality quality = Quality.INIT;

	public XPoint() {
		super();
		this.setName("new_point");
	}

	public XPoint(String name) {
		super(name);
	}
}