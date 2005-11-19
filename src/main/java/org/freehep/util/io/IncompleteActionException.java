// Copyright 2001, FreeHEP.
package org.freehep.util.io;

import java.io.IOException;

/**
 * Exception for the TaggedInputStream. Signals that the inputstream contains
 * more bytes than the stream has read for this action.
 *
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: src/main/java/org/freehep/util/io/IncompleteActionException.java effd8b4f3966 2005/11/19 07:52:18 duns $
 */
public class IncompleteActionException extends IOException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6817511986951461967L;
	private Action action;
    private byte[] rest;

    public IncompleteActionException(Action action, byte[] rest) {
        super("Action "+action+" contains "+rest.length+" unread bytes");
        this.action = action;
        this.rest = rest;
    }

    public Action getAction() {
        return action;
    }

    public byte[] getBytes() {
        return rest;
    }
}
