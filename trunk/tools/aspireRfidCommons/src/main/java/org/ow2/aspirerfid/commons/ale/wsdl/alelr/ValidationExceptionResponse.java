
package org.ow2.aspirerfid.commons.ale.wsdl.alelr;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.4
 * Tue Jan 26 18:57:22 EET 2010
 * Generated source version: 2.2.4
 * 
 */

@WebFault(name = "ValidationException", targetNamespace = "urn:epcglobal:alelr:wsdl:1")
public class ValidationExceptionResponse extends Exception {
    public static final long serialVersionUID = 20100126185722L;
    
    private org.ow2.aspirerfid.commons.ale.wsdl.alelr.ValidationException validationException;

    public ValidationExceptionResponse() {
        super();
    }
    
    public ValidationExceptionResponse(String message) {
        super(message);
    }
    
    public ValidationExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.alelr.ValidationException validationException) {
        super(message);
        this.validationException = validationException;
    }

    public ValidationExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.alelr.ValidationException validationException, Throwable cause) {
        super(message, cause);
        this.validationException = validationException;
    }

    public org.ow2.aspirerfid.commons.ale.wsdl.alelr.ValidationException getFaultInfo() {
        return this.validationException;
    }
}
