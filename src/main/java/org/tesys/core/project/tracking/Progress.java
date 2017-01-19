package org.tesys.core.project.tracking;

public class Progress {
    private String total;

    private String progress;

    private String percent;

    public String getTotal() {
	return total;
    }

    public void setTotal(String total) {
	this.total = total;
    }

    public String getProgress() {
	return progress;
    }

    public void setProgress(String progress) {
	this.progress = progress;
    }

    public String getPercent() {
	return percent;
    }

    public void setPercent(String percent) {
	this.percent = percent;
    }

    @Override
    public String toString() {
	return "Progress [total=" + total + ", progress=" + progress
		+ ", percent=" + percent + "]";
    }
}
