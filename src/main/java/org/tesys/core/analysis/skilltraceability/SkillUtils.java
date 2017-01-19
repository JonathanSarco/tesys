package org.tesys.core.analysis.skilltraceability;

import java.util.List;

public class SkillUtils {

	private SkillUtils() {
	}
	
	//Dada una lista de skills suma los iguales
	public static List<Skill> uniqSkills(List<Skill> l1) {
		for (int i = 0; i < l1.size(); i++) {
			for (int j = i+1; j < l1.size(); j++) {
				if( l1.get(i).equals(l1.get(j)) ) {
					l1.get(i).setWeight(l1.get(i).getWeight()+l1.get(j).getWeight());
					l1.remove(j);
					j--;
				}
			}
		}
		return l1;
	}

}
