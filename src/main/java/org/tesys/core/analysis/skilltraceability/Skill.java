package org.tesys.core.analysis.skilltraceability;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Skill {
	
	public String skillName;
	public Integer skillWeight;

	public Skill() {
		//empty
	}

	public Skill(String name, Integer weight) {
		super();
		this.skillName = name;
		this.skillWeight = weight;
	}

	public Integer getWeight() {
		return skillWeight;
	}

	public void setWeight(Integer weight) {
		this.skillWeight = weight;
	}

	public String getName() {
		return skillName;
	}

	public void setName(String name) {
		this.skillName = name;
	}

	@Override
	public String toString() {
		return "Skill [name=" + skillName + ", weight=" + skillWeight + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((skillName == null) ? 0 : skillName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Skill other = (Skill) obj;
		if (skillName == null) {
			if (other.skillName != null)
				return false;
		} else if (!skillName.equals(other.skillName))
			return false;
		return true;
	}

}
