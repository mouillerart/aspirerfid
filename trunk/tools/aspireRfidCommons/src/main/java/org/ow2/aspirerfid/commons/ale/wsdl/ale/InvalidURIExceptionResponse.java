
package org.ow2.aspirerfid.commons.ale.wsdl.ale;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.4
 * Tue Jan 26 18:52:19 EET 2010
 * Generated source version: 2.2.4
 * 
 */

@WebFault(name = "InvalidURIException", targetNamespace = "urn:epcglobal:ale:wsdl:1")
public class InvalidURIExceptionResponse extends Exception {
    public static final long serialVersionUID = 20100126185219L;
    
    private org.ow2.aspirerfid.commons.ale.wsdl.ale.InvalidURIException invalidURIException;

    public InvalidURIExceptionResponse() {
        super();
    }
    
    public InvalidURIExceptionResponse(String message) {
        super(message);
    }
    
    public InvalidURIExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidURIExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.ale.InvalidURIException invalidURIException) {
        super(message);
        this.invalidURIException = invalidURIException;
    }

    public InvalidURIExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.ale.InvalidURIException invalidURIException, Throwable cause) {
        super(message, cause);
        this.invalidURIException = invalidURIException;
    }

    public org.ow2.aspirerfid.commons.ale.wsdl.ale.InvalidURIException getFaultInfo() {
        return this.invalidURIException;
    }
}
