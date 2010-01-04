
package org.ow2.aspirerfid.programmableengine.ale.wsdl.ale;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.ow2.aspirerfid.programmableengine.model.*;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.ow2.aspirerfid.programmableengine.ale.wsdl.ale package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetStandardVersion_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "GetStandardVersion");
    private final static QName _Poll_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "Poll");
    private final static QName _ImplementationException_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "ImplementationException");
    private final static QName _GetStandardVersionResult_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "GetStandardVersionResult");
    private final static QName _GetECSpecResult_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "GetECSpecResult");
    private final static QName _DuplicateNameException_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "DuplicateNameException");
    private final static QName _Unsubscribe_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "Unsubscribe");
    private final static QName _GetECSpecNamesResult_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "GetECSpecNamesResult");
    private final static QName _ALEException_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "ALEException");
    private final static QName _ECSpecValidationException_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "ECSpecValidationException");
    private final static QName _ImmediateResult_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "ImmediateResult");
    private final static QName _GetVendorVersion_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "GetVendorVersion");
    private final static QName _GetSubscribersResult_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "GetSubscribersResult");
    private final static QName _GetVendorVersionResult_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "GetVendorVersionResult");
    private final static QName _GetECSpec_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "GetECSpec");
    private final static QName _NoSuchSubscriberException_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "NoSuchSubscriberException");
    private final static QName _DuplicateSubscriptionException_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "DuplicateSubscriptionException");
    private final static QName _Subscribe_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "Subscribe");
    private final static QName _Immediate_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "Immediate");
    private final static QName _Define_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "Define");
    private final static QName _SecurityException_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "SecurityException");
    private final static QName _NoSuchNameException_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "NoSuchNameException");
    private final static QName _Undefine_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "Undefine");
    private final static QName _VoidHolder_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "VoidHolder");
    private final static QName _GetECSpecNames_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "GetECSpecNames");
    private final static QName _GetSubscribers_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "GetSubscribers");
    private final static QName _PollResult_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "PollResult");
    private final static QName _InvalidURIException_QNAME = new QName("urn:epcglobal:ale:wsdl:1", "InvalidURIException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.ow2.aspirerfid.programmableengine.ale.wsdl.ale
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ECReportGroupListMemberExtension.FieldList }
     * 
     */
    public ECReportGroupListMemberExtension.FieldList createECReportGroupListMemberExtensionFieldList() {
        return new ECReportGroupListMemberExtension.FieldList();
    }

    /**
     * Create an instance of {@link ECGroupSpecExtension2 }
     * 
     */
    public ECGroupSpecExtension2 createECGroupSpecExtension2() {
        return new ECGroupSpecExtension2();
    }

    /**
     * Create an instance of {@link ECReportOutputFieldSpec }
     * 
     */
    public ECReportOutputFieldSpec createECReportOutputFieldSpec() {
        return new ECReportOutputFieldSpec();
    }

    /**
     * Create an instance of {@link ECReports }
     * 
     */
    public ECReports createECReports() {
        return new ECReports();
    }

    /**
     * Create an instance of {@link ECReportGroupListMemberExtension2 }
     * 
     */
    public ECReportGroupListMemberExtension2 createECReportGroupListMemberExtension2() {
        return new ECReportGroupListMemberExtension2();
    }

    /**
     * Create an instance of {@link ECReportGroupList }
     * 
     */
    public ECReportGroupList createECReportGroupList() {
        return new ECReportGroupList();
    }

    /**
     * Create an instance of {@link ECReportsExtension }
     * 
     */
    public ECReportsExtension createECReportsExtension() {
        return new ECReportsExtension();
    }

    /**
     * Create an instance of {@link ECReportSpecExtension }
     * 
     */
    public ECReportSpecExtension createECReportSpecExtension() {
        return new ECReportSpecExtension();
    }

    /**
     * Create an instance of {@link DuplicateNameException }
     * 
     */
    public DuplicateNameException createDuplicateNameException() {
        return new DuplicateNameException();
    }

    /**
     * Create an instance of {@link ECFilterSpec.IncludePatterns }
     * 
     */
    public ECFilterSpec.IncludePatterns createECFilterSpecIncludePatterns() {
        return new ECFilterSpec.IncludePatterns();
    }

    /**
     * Create an instance of {@link ECGroupSpec }
     * 
     */
    public ECGroupSpec createECGroupSpec() {
        return new ECGroupSpec();
    }

    /**
     * Create an instance of {@link DuplicateSubscriptionException }
     * 
     */
    public DuplicateSubscriptionException createDuplicateSubscriptionException() {
        return new DuplicateSubscriptionException();
    }

    /**
     * Create an instance of {@link ECReportOutputSpec }
     * 
     */
    public ECReportOutputSpec createECReportOutputSpec() {
        return new ECReportOutputSpec();
    }

    /**
     * Create an instance of {@link Subscribe }
     * 
     */
    public Subscribe createSubscribe() {
        return new Subscribe();
    }

    /**
     * Create an instance of {@link ECReportGroupListMemberExtension }
     * 
     */
    public ECReportGroupListMemberExtension createECReportGroupListMemberExtension() {
        return new ECReportGroupListMemberExtension();
    }

    /**
     * Create an instance of {@link ECFilterSpecExtension2 }
     * 
     */
    public ECFilterSpecExtension2 createECFilterSpecExtension2() {
        return new ECFilterSpecExtension2();
    }

    /**
     * Create an instance of {@link ECSpecExtension2 }
     * 
     */
    public ECSpecExtension2 createECSpecExtension2() {
        return new ECSpecExtension2();
    }

    /**
     * Create an instance of {@link ECReportGroupListExtension }
     * 
     */
    public ECReportGroupListExtension createECReportGroupListExtension() {
        return new ECReportGroupListExtension();
    }

    /**
     * Create an instance of {@link EPC }
     * 
     */
    public EPC createEPC() {
        return new EPC();
    }

    /**
     * Create an instance of {@link ECFilterListMemberExtension }
     * 
     */
    public ECFilterListMemberExtension createECFilterListMemberExtension() {
        return new ECFilterListMemberExtension();
    }

    /**
     * Create an instance of {@link Unsubscribe }
     * 
     */
    public Unsubscribe createUnsubscribe() {
        return new Unsubscribe();
    }

    /**
     * Create an instance of {@link ECFilterSpecExtension.FilterList }
     * 
     */
    public ECFilterSpecExtension.FilterList createECFilterSpecExtensionFilterList() {
        return new ECFilterSpecExtension.FilterList();
    }

    /**
     * Create an instance of {@link ECReportGroupCountExtension }
     * 
     */
    public ECReportGroupCountExtension createECReportGroupCountExtension() {
        return new ECReportGroupCountExtension();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     * 
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link ECBoundarySpecExtension.StopTriggerList }
     * 
     */
    public ECBoundarySpecExtension.StopTriggerList createECBoundarySpecExtensionStopTriggerList() {
        return new ECBoundarySpecExtension.StopTriggerList();
    }

    /**
     * Create an instance of {@link ECReportOutputSpecExtension }
     * 
     */
    public ECReportOutputSpecExtension createECReportOutputSpecExtension() {
        return new ECReportOutputSpecExtension();
    }

    /**
     * Create an instance of {@link ECReportGroupListMemberExtension.Stats }
     * 
     */
    public ECReportGroupListMemberExtension.Stats createECReportGroupListMemberExtensionStats() {
        return new ECReportGroupListMemberExtension.Stats();
    }

    /**
     * Create an instance of {@link ECTime }
     * 
     */
    public ECTime createECTime() {
        return new ECTime();
    }

    /**
     * Create an instance of {@link ECSpecExtension.PrimaryKeyFields }
     * 
     */
    public ECSpecExtension.PrimaryKeyFields createECSpecExtensionPrimaryKeyFields() {
        return new ECSpecExtension.PrimaryKeyFields();
    }

    /**
     * Create an instance of {@link ECReportMemberField }
     * 
     */
    public ECReportMemberField createECReportMemberField() {
        return new ECReportMemberField();
    }

    /**
     * Create an instance of {@link ECReportGroup }
     * 
     */
    public ECReportGroup createECReportGroup() {
        return new ECReportGroup();
    }

    /**
     * Create an instance of {@link ECReportOutputSpecExtension.FieldList }
     * 
     */
    public ECReportOutputSpecExtension.FieldList createECReportOutputSpecExtensionFieldList() {
        return new ECReportOutputSpecExtension.FieldList();
    }

    /**
     * Create an instance of {@link ECFilterSpec.ExcludePatterns }
     * 
     */
    public ECFilterSpec.ExcludePatterns createECFilterSpecExcludePatterns() {
        return new ECFilterSpec.ExcludePatterns();
    }

    /**
     * Create an instance of {@link ECSpec }
     * 
     */
    public ECSpec createECSpec() {
        return new ECSpec();
    }

    /**
     * Create an instance of {@link ECReportSpecExtension.StatProfileNames }
     * 
     */
    public ECReportSpecExtension.StatProfileNames createECReportSpecExtensionStatProfileNames() {
        return new ECReportSpecExtension.StatProfileNames();
    }

    /**
     * Create an instance of {@link ECReportGroupExtension }
     * 
     */
    public ECReportGroupExtension createECReportGroupExtension() {
        return new ECReportGroupExtension();
    }

    /**
     * Create an instance of {@link NoSuchNameException }
     * 
     */
    public NoSuchNameException createNoSuchNameException() {
        return new NoSuchNameException();
    }

    /**
     * Create an instance of {@link ALEException }
     * 
     */
    public ALEException createALEException() {
        return new ALEException();
    }

    /**
     * Create an instance of {@link Undefine }
     * 
     */
    public Undefine createUndefine() {
        return new Undefine();
    }

    /**
     * Create an instance of {@link ECReportGroupCount }
     * 
     */
    public ECReportGroupCount createECReportGroupCount() {
        return new ECReportGroupCount();
    }

    /**
     * Create an instance of {@link ImplementationException }
     * 
     */
    public ImplementationException createImplementationException() {
        return new ImplementationException();
    }

    /**
     * Create an instance of {@link ECSpecExtension }
     * 
     */
    public ECSpecExtension createECSpecExtension() {
        return new ECSpecExtension();
    }

    /**
     * Create an instance of {@link ECReportGroupListMember }
     * 
     */
    public ECReportGroupListMember createECReportGroupListMember() {
        return new ECReportGroupListMember();
    }

    /**
     * Create an instance of {@link GetECSpec }
     * 
     */
    public GetECSpec createGetECSpec() {
        return new GetECSpec();
    }

    /**
     * Create an instance of {@link ECReport }
     * 
     */
    public ECReport createECReport() {
        return new ECReport();
    }

    /**
     * Create an instance of {@link ECSpec.LogicalReaders }
     * 
     */
    public ECSpec.LogicalReaders createECSpecLogicalReaders() {
        return new ECSpec.LogicalReaders();
    }

    /**
     * Create an instance of {@link ECSpec.ReportSpecs }
     * 
     */
    public ECSpec.ReportSpecs createECSpecReportSpecs() {
        return new ECSpec.ReportSpecs();
    }

    /**
     * Create an instance of {@link ECTagStat.StatBlocks }
     * 
     */
    public ECTagStat.StatBlocks createECTagStatStatBlocks() {
        return new ECTagStat.StatBlocks();
    }

    /**
     * Create an instance of {@link ECTagStat }
     * 
     */
    public ECTagStat createECTagStat() {
        return new ECTagStat();
    }

    /**
     * Create an instance of {@link Define }
     * 
     */
    public Define createDefine() {
        return new Define();
    }

    /**
     * Create an instance of {@link ECSightingStat }
     * 
     */
    public ECSightingStat createECSightingStat() {
        return new ECSightingStat();
    }

    /**
     * Create an instance of {@link Poll }
     * 
     */
    public Poll createPoll() {
        return new Poll();
    }

    /**
     * Create an instance of {@link ECReportSpecExtension2 }
     * 
     */
    public ECReportSpecExtension2 createECReportSpecExtension2() {
        return new ECReportSpecExtension2();
    }

    /**
     * Create an instance of {@link ECReportOutputFieldSpecExtension }
     * 
     */
    public ECReportOutputFieldSpecExtension createECReportOutputFieldSpecExtension() {
        return new ECReportOutputFieldSpecExtension();
    }

    /**
     * Create an instance of {@link ECFilterSpecExtension }
     * 
     */
    public ECFilterSpecExtension createECFilterSpecExtension() {
        return new ECFilterSpecExtension();
    }

    /**
     * Create an instance of {@link ECReaderStat.Sightings }
     * 
     */
    public ECReaderStat.Sightings createECReaderStatSightings() {
        return new ECReaderStat.Sightings();
    }

    /**
     * Create an instance of {@link GetSubscribers }
     * 
     */
    public GetSubscribers createGetSubscribers() {
        return new GetSubscribers();
    }

    /**
     * Create an instance of {@link ECFilterListMember }
     * 
     */
    public ECFilterListMember createECFilterListMember() {
        return new ECFilterListMember();
    }

    /**
     * Create an instance of {@link ECReportExtension }
     * 
     */
    public ECReportExtension createECReportExtension() {
        return new ECReportExtension();
    }

    /**
     * Create an instance of {@link NoSuchSubscriberException }
     * 
     */
    public NoSuchSubscriberException createNoSuchSubscriberException() {
        return new NoSuchSubscriberException();
    }

    /**
     * Create an instance of {@link ECTagTimestampStat }
     * 
     */
    public ECTagTimestampStat createECTagTimestampStat() {
        return new ECTagTimestampStat();
    }

    /**
     * Create an instance of {@link ECBoundarySpecExtension }
     * 
     */
    public ECBoundarySpecExtension createECBoundarySpecExtension() {
        return new ECBoundarySpecExtension();
    }

    /**
     * Create an instance of {@link ECBoundarySpecExtension.StartTriggerList }
     * 
     */
    public ECBoundarySpecExtension.StartTriggerList createECBoundarySpecExtensionStartTriggerList() {
        return new ECBoundarySpecExtension.StartTriggerList();
    }

    /**
     * Create an instance of {@link ECFilterListMember.PatList }
     * 
     */
    public ECFilterListMember.PatList createECFilterListMemberPatList() {
        return new ECFilterListMember.PatList();
    }

    /**
     * Create an instance of {@link ECBoundarySpec }
     * 
     */
    public ECBoundarySpec createECBoundarySpec() {
        return new ECBoundarySpec();
    }

    /**
     * Create an instance of {@link ECReports.Reports }
     * 
     */
    public ECReports.Reports createECReportsReports() {
        return new ECReports.Reports();
    }

    /**
     * Create an instance of {@link ECReportOutputSpecExtension2 }
     * 
     */
    public ECReportOutputSpecExtension2 createECReportOutputSpecExtension2() {
        return new ECReportOutputSpecExtension2();
    }

    /**
     * Create an instance of {@link ECSpecValidationException }
     * 
     */
    public ECSpecValidationException createECSpecValidationException() {
        return new ECSpecValidationException();
    }

    /**
     * Create an instance of {@link SecurityException }
     * 
     */
    public SecurityException createSecurityException() {
        return new SecurityException();
    }

    /**
     * Create an instance of {@link ECReaderStat }
     * 
     */
    public ECReaderStat createECReaderStat() {
        return new ECReaderStat();
    }

    /**
     * Create an instance of {@link Immediate }
     * 
     */
    public Immediate createImmediate() {
        return new Immediate();
    }

    /**
     * Create an instance of {@link EmptyParms }
     * 
     */
    public EmptyParms createEmptyParms() {
        return new EmptyParms();
    }

    /**
     * Create an instance of {@link ECGroupSpecExtension }
     * 
     */
    public ECGroupSpecExtension createECGroupSpecExtension() {
        return new ECGroupSpecExtension();
    }

    /**
     * Create an instance of {@link VoidHolder }
     * 
     */
    public VoidHolder createVoidHolder() {
        return new VoidHolder();
    }

    /**
     * Create an instance of {@link ECFieldSpecExtension }
     * 
     */
    public ECFieldSpecExtension createECFieldSpecExtension() {
        return new ECFieldSpecExtension();
    }

    /**
     * Create an instance of {@link ECReportSetSpec }
     * 
     */
    public ECReportSetSpec createECReportSetSpec() {
        return new ECReportSetSpec();
    }

    /**
     * Create an instance of {@link ECFilterSpec }
     * 
     */
    public ECFilterSpec createECFilterSpec() {
        return new ECFilterSpec();
    }

    /**
     * Create an instance of {@link ECFieldSpec }
     * 
     */
    public ECFieldSpec createECFieldSpec() {
        return new ECFieldSpec();
    }

    /**
     * Create an instance of {@link ECBoundarySpecExtension2 }
     * 
     */
    public ECBoundarySpecExtension2 createECBoundarySpecExtension2() {
        return new ECBoundarySpecExtension2();
    }

    /**
     * Create an instance of {@link InvalidURIException }
     * 
     */
    public InvalidURIException createInvalidURIException() {
        return new InvalidURIException();
    }

    /**
     * Create an instance of {@link ECReportSpec }
     * 
     */
    public ECReportSpec createECReportSpec() {
        return new ECReportSpec();
    }

    /**
     * Create an instance of {@link ECReportMemberFieldExtension }
     * 
     */
    public ECReportMemberFieldExtension createECReportMemberFieldExtension() {
        return new ECReportMemberFieldExtension();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmptyParms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "GetStandardVersion")
    public JAXBElement<EmptyParms> createGetStandardVersion(EmptyParms value) {
        return new JAXBElement<EmptyParms>(_GetStandardVersion_QNAME, EmptyParms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Poll }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "Poll")
    public JAXBElement<Poll> createPoll(Poll value) {
        return new JAXBElement<Poll>(_Poll_QNAME, Poll.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImplementationException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "ImplementationException")
    public JAXBElement<ImplementationException> createImplementationException(ImplementationException value) {
        return new JAXBElement<ImplementationException>(_ImplementationException_QNAME, ImplementationException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "GetStandardVersionResult")
    public JAXBElement<String> createGetStandardVersionResult(String value) {
        return new JAXBElement<String>(_GetStandardVersionResult_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ECSpec }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "GetECSpecResult")
    public JAXBElement<ECSpec> createGetECSpecResult(ECSpec value) {
        return new JAXBElement<ECSpec>(_GetECSpecResult_QNAME, ECSpec.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DuplicateNameException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "DuplicateNameException")
    public JAXBElement<DuplicateNameException> createDuplicateNameException(DuplicateNameException value) {
        return new JAXBElement<DuplicateNameException>(_DuplicateNameException_QNAME, DuplicateNameException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Unsubscribe }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "Unsubscribe")
    public JAXBElement<Unsubscribe> createUnsubscribe(Unsubscribe value) {
        return new JAXBElement<Unsubscribe>(_Unsubscribe_QNAME, Unsubscribe.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "GetECSpecNamesResult")
    public JAXBElement<ArrayOfString> createGetECSpecNamesResult(ArrayOfString value) {
        return new JAXBElement<ArrayOfString>(_GetECSpecNamesResult_QNAME, ArrayOfString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ALEException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "ALEException")
    public JAXBElement<ALEException> createALEException(ALEException value) {
        return new JAXBElement<ALEException>(_ALEException_QNAME, ALEException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ECSpecValidationException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "ECSpecValidationException")
    public JAXBElement<ECSpecValidationException> createECSpecValidationException(ECSpecValidationException value) {
        return new JAXBElement<ECSpecValidationException>(_ECSpecValidationException_QNAME, ECSpecValidationException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ECReports }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "ImmediateResult")
    public JAXBElement<ECReports> createImmediateResult(ECReports value) {
        return new JAXBElement<ECReports>(_ImmediateResult_QNAME, ECReports.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmptyParms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "GetVendorVersion")
    public JAXBElement<EmptyParms> createGetVendorVersion(EmptyParms value) {
        return new JAXBElement<EmptyParms>(_GetVendorVersion_QNAME, EmptyParms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "GetSubscribersResult")
    public JAXBElement<ArrayOfString> createGetSubscribersResult(ArrayOfString value) {
        return new JAXBElement<ArrayOfString>(_GetSubscribersResult_QNAME, ArrayOfString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "GetVendorVersionResult")
    public JAXBElement<String> createGetVendorVersionResult(String value) {
        return new JAXBElement<String>(_GetVendorVersionResult_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetECSpec }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "GetECSpec")
    public JAXBElement<GetECSpec> createGetECSpec(GetECSpec value) {
        return new JAXBElement<GetECSpec>(_GetECSpec_QNAME, GetECSpec.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoSuchSubscriberException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "NoSuchSubscriberException")
    public JAXBElement<NoSuchSubscriberException> createNoSuchSubscriberException(NoSuchSubscriberException value) {
        return new JAXBElement<NoSuchSubscriberException>(_NoSuchSubscriberException_QNAME, NoSuchSubscriberException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DuplicateSubscriptionException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "DuplicateSubscriptionException")
    public JAXBElement<DuplicateSubscriptionException> createDuplicateSubscriptionException(DuplicateSubscriptionException value) {
        return new JAXBElement<DuplicateSubscriptionException>(_DuplicateSubscriptionException_QNAME, DuplicateSubscriptionException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Subscribe }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "Subscribe")
    public JAXBElement<Subscribe> createSubscribe(Subscribe value) {
        return new JAXBElement<Subscribe>(_Subscribe_QNAME, Subscribe.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Immediate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "Immediate")
    public JAXBElement<Immediate> createImmediate(Immediate value) {
        return new JAXBElement<Immediate>(_Immediate_QNAME, Immediate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Define }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "Define")
    public JAXBElement<Define> createDefine(Define value) {
        return new JAXBElement<Define>(_Define_QNAME, Define.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecurityException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "SecurityException")
    public JAXBElement<SecurityException> createSecurityException(SecurityException value) {
        return new JAXBElement<SecurityException>(_SecurityException_QNAME, SecurityException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoSuchNameException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "NoSuchNameException")
    public JAXBElement<NoSuchNameException> createNoSuchNameException(NoSuchNameException value) {
        return new JAXBElement<NoSuchNameException>(_NoSuchNameException_QNAME, NoSuchNameException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Undefine }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "Undefine")
    public JAXBElement<Undefine> createUndefine(Undefine value) {
        return new JAXBElement<Undefine>(_Undefine_QNAME, Undefine.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VoidHolder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "VoidHolder")
    public JAXBElement<VoidHolder> createVoidHolder(VoidHolder value) {
        return new JAXBElement<VoidHolder>(_VoidHolder_QNAME, VoidHolder.class, null, value);
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmptyParms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "GetECSpecNames")
    public JAXBElement<EmptyParms> createGetECSpecNames(EmptyParms value) {
        return new JAXBElement<EmptyParms>(_GetECSpecNames_QNAME, EmptyParms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscribers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "GetSubscribers")
    public JAXBElement<GetSubscribers> createGetSubscribers(GetSubscribers value) {
        return new JAXBElement<GetSubscribers>(_GetSubscribers_QNAME, GetSubscribers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ECReports }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "PollResult")
    public JAXBElement<ECReports> createPollResult(ECReports value) {
        return new JAXBElement<ECReports>(_PollResult_QNAME, ECReports.class, null, value);
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidURIException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:epcglobal:ale:wsdl:1", name = "InvalidURIException")
    public JAXBElement<InvalidURIException> createInvalidURIException(InvalidURIException value) {
        return new JAXBElement<InvalidURIException>(_InvalidURIException_QNAME, InvalidURIException.class, null, value);
    }

}
