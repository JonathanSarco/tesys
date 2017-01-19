package org.tesys.core.plugins.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tesys.util.MD5;

/**
 * Almacena la informacion que corresponse a cada operacion (read/write) por
 * usuario
 * 
 * @author leandro
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivityPOJO {
    
    private String issueKey = "" ;
    private Long timeStamp ;
    private String user;
    private Integer minsAcum;
    private Integer count;

    public ActivityPOJO() {
    }

    public ActivityPOJO( String user,Integer minsAcum, Integer count, Long timeStamp ) {
        this.user = user;
        this.setMinsAcum( minsAcum );
        this.count = count ;
        this.setTimeStamp( timeStamp ) ;
    }
    
    public ActivityPOJO( String user, String issueKey, Integer minsAcum, Integer count, Long timeStamp ) {
        this.user = user;
        this.setMinsAcum( minsAcum );
        this.count = count ;
        this.setTimeStamp( timeStamp ) ;
        this.issueKey = issueKey ;
    }
    
    public ActivityPOJO( ActivityPOJO act ) {
        this.user = act.getUser();
        this.minsAcum = act.getMinsAcum() ;
        this.count = act.getCount() ;
        this.timeStamp = act.getTimeStamp() ;
        this.issueKey = act.getIssueKey() ;
    }

    public void add( ActivityPOJO act ) {
        this.minsAcum += act.getMinsAcum() ;
        this.count += act.getCount() ;
    }
    
    public String getId() {
        return MD5.generateId( user+issueKey ) ;
    }

    public String getUser() {
        return user;
    }

    public void setUser( String user ) {
        this.user = user;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount( Integer count ) {
        this.count = count;
    }

    public Integer getMinsAcum() {
        return minsAcum;
    }

    public void setMinsAcum( Integer minsAcum ) {
        this.minsAcum = minsAcum;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp( Long timeStamp ) {
        this.timeStamp = timeStamp;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey( String issueKey ) {
        this.issueKey = issueKey;
    }

}
