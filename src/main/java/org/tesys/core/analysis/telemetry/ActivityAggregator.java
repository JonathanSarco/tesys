package org.tesys.core.analysis.telemetry;

import java.util.List;

import org.tesys.core.db.ElasticsearchDao;
import org.tesys.core.estructures.Issue;
import org.tesys.core.estructures.Metric;
import org.tesys.core.plugins.activity.Activity;
import org.tesys.core.plugins.activity.ActivityPOJO;

public class ActivityAggregator extends AggregatorDecorator {

    private ElasticsearchDao<ActivityPOJO> daoR;
    private ElasticsearchDao<ActivityPOJO> daoW;

    public ActivityAggregator( Aggregator aggregator ) {
        super( aggregator );
        daoW = new ElasticsearchDao<ActivityPOJO>(ActivityPOJO.class, Activity.WRITES_PATH);
        daoR = new ElasticsearchDao<ActivityPOJO>(ActivityPOJO.class, Activity.READS_PATH);


    }
    


    @Override
    public List<Metric> getMetricsID() {
      //Se juntan la de los demas programas
      List<Metric> metrics = super.aggregator.getMetricsID();
      //se agregan las de este
      metrics.addAll( Activity.getInstance().getMetrics() );
      return metrics;
    }
    

    @Override
    public Issue agregateMetrics(Issue issueMetrics) {
      issueMetrics = super.agregateMetrics(issueMetrics);
      String key = issueMetrics.getIssueId();
      String user = issueMetrics.getUser();
      ActivityPOJO aux = new ActivityPOJO( user, key, 0, 0 ,(long) 0) ;
      
      ActivityPOJO reads = daoR.read( aux.getId() ) ;
      if ( reads != null ) {
          issueMetrics.addMetric( "reads", reads.getCount() );
          issueMetrics.addMetric( "reads_time", reads.getCount() );        
      }
      
      ActivityPOJO writes = daoW.read( aux.getId() ) ;
      if ( writes != null ) {
          issueMetrics.addMetric( "writes", writes.getCount() );
          issueMetrics.addMetric( "writes_time", writes.getCount() );        
      }

      return issueMetrics;
    }

}
