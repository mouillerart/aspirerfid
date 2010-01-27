
package org.ow2.aspirerfid.commons.ale.wsdl.alelr;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.4
 * Tue Jan 26 18:57:22 EET 2010
 * Generated source version: 2.2.4
 * 
 */

@WebFault(name = "InUseException", targetNamespace = "urn:epcglobal:alelr:wsdl:1")
public class InUseExceptionResponse extends Exception {
    public static final long serialVersionUID = 20100126185722L;
    
    private org.ow2.aspirerfid.commons.ale.wsdl.alelr.InUseException inUseException;

    public InUseExceptionResponse() {
        super();
    }
    
    public InUseExceptionResponse(String message) {
        super(message);
    }
    
    public InUseExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public InUseExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.alelr.InUseException inUseException) {
        super(message);
        this.inUseException = inUseException;
    }

    public InUseExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.alelr.InUseException inUseException, Throwable cause) {
        super(message, cause);
        this.inUseException = inUseException;
    }

    public org.ow2.aspirerfid.commons.ale.wsdl.alelr.InUseException getFaultInfo() {
        return this.inUseException;
    }
}
