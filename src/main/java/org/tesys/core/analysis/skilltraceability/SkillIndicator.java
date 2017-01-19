package org.tesys.core.analysis.skilltraceability;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.util.MD5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillIndicator {
	private String skillName;
	private String indicator;
	
	public SkillIndicator() {

	}
	
	public SkillIndicator(String skillName, String indicator) {
		super();
		this.skillName = skillName;
		this.indicator = indicator;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public String getIndicator() {
		return indicator;
	}
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getId() {
		return MD5.generateId(skillName+indicator);
	}

}
