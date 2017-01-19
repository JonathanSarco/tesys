package org.tesys.core.analysis.skilltraceability;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SkillTracker {

	private List<SkillIndicator> indicators;


	public SkillTracker() {
		indicators = new LinkedList<SkillIndicator>();
	}
	
	public void addIndicator(SkillIndicator si) {
		indicators.add( si );
	}

	public List<Skill> getSkills(String diff) {
		List<Skill> s = new LinkedList<Skill>();
		for (SkillIndicator si : indicators) {
			int count = StringUtils.countMatches(diff, si.getIndicator());
			if( count != 0 ) {
				s.add( new Skill( si.getSkillName(), count ) );
			}
			
		}
		return SkillUtils.uniqSkills(s);

	}

}
