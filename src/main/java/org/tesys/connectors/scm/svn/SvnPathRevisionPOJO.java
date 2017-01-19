package org.tesys.connectors.scm.svn;

public class SvnPathRevisionPOJO {
    
    private String path; //path ancestro
    private Integer lastRevision;
    private Integer actualRevision;
    
    public SvnPathRevisionPOJO() {
	//jackson
    }


	public SvnPathRevisionPOJO(String path, Integer lastRevision,
			Integer actualRevision) {
		super();
		this.path = path;
		this.lastRevision = lastRevision;
		this.actualRevision = actualRevision;
	}


    
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }



	public Integer getLastRevision() {
		return lastRevision;
	}



	public void setLastRevision(Integer lastRevision) {
		this.lastRevision = lastRevision;
	}



	public Integer getActualRevision() {
		return actualRevision;
	}



	public void setActualRevision(Integer actualRevision) {
		this.actualRevision = actualRevision;
	}



	@Override
	public String toString() {
		return "SvnPathRevisionPOJO [path=" + path + ", lastRevision="
				+ lastRevision + ", actualRevision=" + actualRevision + "]";
	}
    

}
