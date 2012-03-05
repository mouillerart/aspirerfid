
package org.ow2.aspirerfid.commons.ale.wsdl.alelr;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.9
 * Tue Feb 08 19:02:25 EET 2011
 * Generated source version: 2.2.9
 * 
 */

@WebFault(name = "SecurityException", targetNamespace = "urn:epcglobal:alelr:wsdl:1")
public class SecurityExceptionResponse extends Exception {
    public static final long serialVersionUID = 20110208190225L;
    
    private org.ow2.aspirerfid.commons.ale.wsdl.alelr.SecurityException securityException;

    public SecurityExceptionResponse() {
        super();
    }
    
    public SecurityExceptionResponse(String message) {
        super(message);
    }
    
    public SecurityExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.alelr.SecurityException securityException) {
        super(message);
        this.securityException = securityException;
    }

    public SecurityExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.alelr.SecurityException securityException, Throwable cause) {
        super(message, cause);
        this.securityException = securityException;
    }

    public org.ow2.aspirerfid.commons.ale.wsdl.alelr.SecurityException getFaultInfo() {
        return this.securityException;
    }
}
