package org.tesys.core.analysis.telemetry;

import java.util.LinkedList;
import java.util.List;

import org.tesys.core.analysis.skilltraceability.Skill;
import org.tesys.core.analysis.skilltraceability.SkillIndicator;
import org.tesys.core.analysis.skilltraceability.SkillTracker;
import org.tesys.core.analysis.skilltraceability.SkillUtils;
import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;
import org.tesys.core.project.scm.RevisionPOJO;

public class SkillsAggregator extends AggregatorDecorator {

    private ElasticsearchDao<SkillIndicator> daosi;
    private ElasticsearchDao<RevisionPOJO> daore;
    
    
    public SkillsAggregator(Aggregator aggregator) {
	super(aggregator);
	daosi = new ElasticsearchDao<SkillIndicator>(
			SkillIndicator.class, ElasticsearchDao.DEFAULT_RESOURCE_SKILL);
	
	daore = new ElasticsearchDao<RevisionPOJO>(
			RevisionPOJO.class, ElasticsearchDao.DEFAULT_RESOURCE_REVISION);
	
	
    }
    
    //TODO esta forma de hacerlo hace que por cada tarea del jira se
    //analizen todas las revisiones, habria que guardar lo que vale cada revision
    //en materia de skills y no analizarlas cada vez por cada tarea de jira
    @Override
    public Issue agregateMetrics(Issue issueMetrics) {
	issueMetrics = super.agregateMetrics(issueMetrics);
	
	SkillTracker st = new SkillTracker();
	
	List<SkillIndicator> skillsIndicators = daosi.readAll();
	for (SkillIndicator si : skillsIndicators) {
		st.addIndicator(si);
	}
	
	List<RevisionPOJO> revisiones = daore.readAll();
	
	List<Skill> skills = new LinkedList<Skill>();
	
	//si son la misma tarea del project tracking
	for (RevisionPOJO rev : revisiones) {
		if( rev.getProjectTrackingTask().equals( issueMetrics.getIssueId() ) ) {
			skills.addAll(st.getSkills( rev.getDiff() ));
		}
	}
	
	if( !skills.isEmpty() ) {
		issueMetrics.setSkills( SkillUtils.uniqSkills( skills ));
	}

	return issueMetrics;
    }

    
    /**
     * Las skills no son consideradas una medida ya que las realizan subjetivamente
     * por eso nomas se pasa los datos que otros programas agregan y listo 
     */
    @Override
    public List<Metric> getMetricsID() {
	List<Metric> metrics = super.aggregator.getMetricsID();
	return metrics;
    }
}
