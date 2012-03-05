
package org.ow2.aspirerfid.commons.ale.wsdl.ale;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.9
 * Tue Feb 08 19:02:20 EET 2011
 * Generated source version: 2.2.9
 * 
 */

@WebFault(name = "ImplementationException", targetNamespace = "urn:epcglobal:ale:wsdl:1")
public class ImplementationExceptionResponse extends Exception {
    public static final long serialVersionUID = 20110208190220L;
    
    private org.ow2.aspirerfid.commons.ale.wsdl.ale.ImplementationException implementationException;

    public ImplementationExceptionResponse() {
        super();
    }
    
    public ImplementationExceptionResponse(String message) {
        super(message);
    }
    
    public ImplementationExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public ImplementationExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.ale.ImplementationException implementationException) {
        super(message);
        this.implementationException = implementationException;
    }

    public ImplementationExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.ale.ImplementationException implementationException, Throwable cause) {
        super(message, cause);
        this.implementationException = implementationException;
    }

    public org.ow2.aspirerfid.commons.ale.wsdl.ale.ImplementationException getFaultInfo() {
        return this.implementationException;
    }
}
