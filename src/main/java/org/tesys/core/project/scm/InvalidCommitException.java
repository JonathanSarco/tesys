package org.tesys.core.project.scm;

public class InvalidCommitException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidCommitException(String msg) {
	super(msg);
    }

    public InvalidCommitException(String msg, Throwable c) {
	super(msg, c);
    }
}
