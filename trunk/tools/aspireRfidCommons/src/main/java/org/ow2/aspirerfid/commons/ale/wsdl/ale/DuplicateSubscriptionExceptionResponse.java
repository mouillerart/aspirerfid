
package org.ow2.aspirerfid.commons.ale.wsdl.ale;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.4
 * Tue Jan 26 18:52:19 EET 2010
 * Generated source version: 2.2.4
 * 
 */

@WebFault(name = "DuplicateSubscriptionException", targetNamespace = "urn:epcglobal:ale:wsdl:1")
public class DuplicateSubscriptionExceptionResponse extends Exception {
    public static final long serialVersionUID = 20100126185219L;
    
    private org.ow2.aspirerfid.commons.ale.wsdl.ale.DuplicateSubscriptionException duplicateSubscriptionException;

    public DuplicateSubscriptionExceptionResponse() {
        super();
    }
    
    public DuplicateSubscriptionExceptionResponse(String message) {
        super(message);
    }
    
    public DuplicateSubscriptionExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateSubscriptionExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.ale.DuplicateSubscriptionException duplicateSubscriptionException) {
        super(message);
        this.duplicateSubscriptionException = duplicateSubscriptionException;
    }

    public DuplicateSubscriptionExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.ale.DuplicateSubscriptionException duplicateSubscriptionException, Throwable cause) {
        super(message, cause);
        this.duplicateSubscriptionException = duplicateSubscriptionException;
    }

    public org.ow2.aspirerfid.commons.ale.wsdl.ale.DuplicateSubscriptionException getFaultInfo() {
        return this.duplicateSubscriptionException;
    }
}
