package org.fosstrak.ale.wsdl.alecc.epcglobal;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.6.1
 * 2013-10-03T11:31:18.882+02:00
 * Generated source version: 2.6.1
 * 
 */
@WebService(targetNamespace = "urn:epcglobal:alecc:wsdl:1", name = "ALECCServicePortType")
@XmlSeeAlso({ObjectFactory.class, org.fosstrak.ale.xsd.ale.epcglobal.ObjectFactory.class, org.fosstrak.ale.xsd.epcglobal.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ALECCServicePortType {

    @WebResult(name = "RemoveAssocTableEntriesResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "removeAssocTableEntriesReturn")
    @WebMethod
    public RemoveAssocTableEntriesResult removeAssocTableEntries(
        @WebParam(partName = "parms", name = "RemoveAssocTableEntries", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        RemoveAssocTableEntries parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse, InvalidPatternExceptionResponse;

    @WebResult(name = "GetStandardVersionResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getStandardVersionReturn")
    @WebMethod
    public java.lang.String getStandardVersion(
        @WebParam(partName = "parms", name = "GetStandardVersion", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        EmptyParms parms
    ) throws ImplementationExceptionResponse;

    @WebResult(name = "GetAssocTableResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getAssocTableReturn")
    @WebMethod
    public org.fosstrak.ale.xsd.ale.epcglobal.AssocTableSpec getAssocTable(
        @WebParam(partName = "parms", name = "GetAssocTable", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        GetAssocTable parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "UndefineAssocTableResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "undefineAssocTableReturn")
    @WebMethod
    public UndefineAssocTableResult undefineAssocTable(
        @WebParam(partName = "parms", name = "UndefineAssocTable", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        UndefineAssocTable parms
    ) throws SecurityExceptionResponse, InUseExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "PollResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "pollReturn")
    @WebMethod
    public org.fosstrak.ale.xsd.ale.epcglobal.CCReports poll(
        @WebParam(partName = "parms", name = "Poll", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        Poll parms
    ) throws SecurityExceptionResponse, ParameterExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "GetEPCCacheResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getEPCCacheReturn")
    @WebMethod
    public org.fosstrak.ale.xsd.ale.epcglobal.EPCCacheSpec getEPCCache(
        @WebParam(partName = "parms", name = "GetEPCCache", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        GetEPCCache parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "DefineAssocTableResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "defineAssocTableReturn")
    @WebMethod
    public DefineAssocTableResult defineAssocTable(
        @WebParam(partName = "parms", name = "DefineAssocTable", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        DefineAssocTable parms
    ) throws SecurityExceptionResponse, AssocTableValidationExceptionResponse, DuplicateNameExceptionResponse, InvalidAssocTableEntryExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "DefineRNGResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "defineRNGReturn")
    @WebMethod
    public DefineRNGResult defineRNG(
        @WebParam(partName = "parms", name = "DefineRNG", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        DefineRNG parms
    ) throws RNGValidationExceptionResponse, SecurityExceptionResponse, DuplicateNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "GetEPCCacheContentsResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getEPCCacheContentsReturn")
    @WebMethod
    public org.fosstrak.ale.xsd.ale.epcglobal.EPCPatternList getEPCCacheContents(
        @WebParam(partName = "parms", name = "GetEPCCacheContents", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        GetEPCCacheContents parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "GetCCSpecNamesResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getCCSpecNamesReturn")
    @WebMethod
    public ArrayOfString getCCSpecNames(
        @WebParam(partName = "parms", name = "GetCCSpecNames", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        EmptyParms parms
    ) throws SecurityExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "GetRNGResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getRNGReturn")
    @WebMethod
    public org.fosstrak.ale.xsd.ale.epcglobal.RNGSpec getRNG(
        @WebParam(partName = "parms", name = "GetRNG", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        GetRNG parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "DepleteEPCCacheResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "depleteEPCCacheReturn")
    @WebMethod
    public org.fosstrak.ale.xsd.ale.epcglobal.EPCPatternList depleteEPCCache(
        @WebParam(partName = "parms", name = "DepleteEPCCache", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        DepleteEPCCache parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "ReplenishEPCCacheResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "replenishEPCCacheReturn")
    @WebMethod
    public ReplenishEPCCacheResult replenishEPCCache(
        @WebParam(partName = "parms", name = "ReplenishEPCCache", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        ReplenishEPCCache parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse, InvalidPatternExceptionResponse;

    @WebResult(name = "GetAssocTableEntriesResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getAssocTableEntriesReturn")
    @WebMethod
    public org.fosstrak.ale.xsd.ale.epcglobal.AssocTableEntryList getAssocTableEntries(
        @WebParam(partName = "parms", name = "GetAssocTableEntries", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        GetAssocTableEntries parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse, InvalidPatternExceptionResponse;

    @WebResult(name = "ImmediateResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "immediateReturn")
    @WebMethod
    public org.fosstrak.ale.xsd.ale.epcglobal.CCReports immediate(
        @WebParam(partName = "parms", name = "Immediate", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        Immediate parms
    ) throws SecurityExceptionResponse, CCSpecValidationExceptionResponse, ImplementationExceptionResponse, ParameterForbiddenExceptionResponse;

    @WebResult(name = "UndefineRNGResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "undefineRNGReturn")
    @WebMethod
    public UndefineRNGResult undefineRNG(
        @WebParam(partName = "parms", name = "UndefineRNG", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        UndefineRNG parms
    ) throws SecurityExceptionResponse, InUseExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "PutAssocTableEntriesResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "putAssocTableEntriesReturn")
    @WebMethod
    public PutAssocTableEntriesResult putAssocTableEntries(
        @WebParam(partName = "parms", name = "PutAssocTableEntries", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        PutAssocTableEntries parms
    ) throws SecurityExceptionResponse, InvalidAssocTableEntryExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "GetCCSpecResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getCCSpecReturn")
    @WebMethod
    public org.fosstrak.ale.xsd.ale.epcglobal.CCSpec getCCSpec(
        @WebParam(partName = "parms", name = "GetCCSpec", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        GetCCSpec parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "DefineResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "defineReturn")
    @WebMethod
    public DefineResult define(
        @WebParam(partName = "parms", name = "Define", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        Define parms
    ) throws SecurityExceptionResponse, DuplicateNameExceptionResponse, CCSpecValidationExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "GetRNGNamesResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getRNGNamesReturn")
    @WebMethod
    public ArrayOfString getRNGNames(
        @WebParam(partName = "parms", name = "GetRNGNames", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        EmptyParms parms
    ) throws SecurityExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "RemoveAssocTableEntryResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "removeAssocTableEntryReturn")
    @WebMethod
    public RemoveAssocTableEntryResult removeAssocTableEntry(
        @WebParam(partName = "parms", name = "RemoveAssocTableEntry", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        RemoveAssocTableEntry parms
    ) throws SecurityExceptionResponse, InvalidEPCExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "GetVendorVersionResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getVendorVersionReturn")
    @WebMethod
    public java.lang.String getVendorVersion(
        @WebParam(partName = "parms", name = "GetVendorVersion", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        EmptyParms parms
    ) throws ImplementationExceptionResponse;

    @WebResult(name = "GetAssocTableValueResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getAssocTableValueReturn")
    @WebMethod
    public java.lang.String getAssocTableValue(
        @WebParam(partName = "parms", name = "GetAssocTableValue", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        GetAssocTableValue parms
    ) throws SecurityExceptionResponse, InvalidEPCExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "UndefineEPCCacheResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "undefineEPCCacheReturn")
    @WebMethod
    public org.fosstrak.ale.xsd.ale.epcglobal.EPCPatternList undefineEPCCache(
        @WebParam(partName = "parms", name = "UndefineEPCCache", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        UndefineEPCCache parms
    ) throws SecurityExceptionResponse, InUseExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "SubscribeResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "subscribeReturn")
    @WebMethod
    public SubscribeResult subscribe(
        @WebParam(partName = "parms", name = "Subscribe", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        Subscribe parms
    ) throws SecurityExceptionResponse, InvalidURIExceptionResponse, DuplicateSubscriptionExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse, ParameterForbiddenExceptionResponse;

    @WebResult(name = "DefineEPCCacheResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "defineEPCCacheReturn")
    @WebMethod
    public DefineEPCCacheResult defineEPCCache(
        @WebParam(partName = "parms", name = "DefineEPCCache", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        DefineEPCCache parms
    ) throws SecurityExceptionResponse, EPCCacheSpecValidationExceptionResponse, DuplicateNameExceptionResponse, ImplementationExceptionResponse, InvalidPatternExceptionResponse;

    @WebResult(name = "GetEPCCacheNamesResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getEPCCacheNamesReturn")
    @WebMethod
    public ArrayOfString getEPCCacheNames(
        @WebParam(partName = "parms", name = "GetEPCCacheNames", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        EmptyParms parms
    ) throws SecurityExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "GetAssocTableNamesResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getAssocTableNamesReturn")
    @WebMethod
    public ArrayOfString getAssocTableNames(
        @WebParam(partName = "parms", name = "GetAssocTableNames", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        EmptyParms parms
    ) throws SecurityExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "UndefineResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "undefineReturn")
    @WebMethod
    public UndefineResult undefine(
        @WebParam(partName = "parms", name = "Undefine", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        Undefine parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "GetSubscribersResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "getSubscribersReturn")
    @WebMethod
    public ArrayOfString getSubscribers(
        @WebParam(partName = "parms", name = "GetSubscribers", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        GetSubscribers parms
    ) throws SecurityExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;

    @WebResult(name = "UnsubscribeResult", targetNamespace = "urn:epcglobal:alecc:wsdl:1", partName = "unsubscribeReturn")
    @WebMethod
    public UnsubscribeResult unsubscribe(
        @WebParam(partName = "parms", name = "Unsubscribe", targetNamespace = "urn:epcglobal:alecc:wsdl:1")
        Unsubscribe parms
    ) throws SecurityExceptionResponse, InvalidURIExceptionResponse, NoSuchSubscriberExceptionResponse, NoSuchNameExceptionResponse, ImplementationExceptionResponse;
}