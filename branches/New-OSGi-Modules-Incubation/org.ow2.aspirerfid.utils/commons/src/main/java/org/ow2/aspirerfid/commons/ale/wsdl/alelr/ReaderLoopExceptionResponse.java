
package org.ow2.aspirerfid.commons.ale.wsdl.alelr;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.5.2
 * 2012-03-13T15:55:20.643+02:00
 * Generated source version: 2.5.2
 */

@WebFault(name = "ReaderLoopException", targetNamespace = "urn:epcglobal:alelr:wsdl:1")
public class ReaderLoopExceptionResponse extends Exception {
    
    private org.ow2.aspirerfid.commons.ale.wsdl.alelr.ReaderLoopException readerLoopException;

    public ReaderLoopExceptionResponse() {
        super();
    }
    
    public ReaderLoopExceptionResponse(String message) {
        super(message);
    }
    
    public ReaderLoopExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaderLoopExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.alelr.ReaderLoopException readerLoopException) {
        super(message);
        this.readerLoopException = readerLoopException;
    }

    public ReaderLoopExceptionResponse(String message, org.ow2.aspirerfid.commons.ale.wsdl.alelr.ReaderLoopException readerLoopException, Throwable cause) {
        super(message, cause);
        this.readerLoopException = readerLoopException;
    }

    public org.ow2.aspirerfid.commons.ale.wsdl.alelr.ReaderLoopException getFaultInfo() {
        return this.readerLoopException;
    }
}