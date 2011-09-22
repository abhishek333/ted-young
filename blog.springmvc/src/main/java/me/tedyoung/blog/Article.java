package me.tedyoung.blog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@JsonSerialize(include=Inclusion.NON_NULL)
@JsonAutoDetect(value=JsonMethod.FIELD, fieldVisibility=Visibility.ANY)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Article {
	@Id @GeneratedValue
	@XmlAttribute
	private Long id;
	
	@NotBlank @Size(max=20)
	@XmlAttribute
	private String title;
	
	@NotNull @Size(max=1024)
	@XmlValue
	private String content;
	
	@XmlTransient
	private boolean imagePresent;
	
	public Article() {
	}
	
	@Override
	public String toString() {
		return title + "[" + id + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isImagePresent() {
		return imagePresent;
	}

	public void setImagePresent(boolean imagePresent) {
		this.imagePresent = imagePresent;
	}
}
