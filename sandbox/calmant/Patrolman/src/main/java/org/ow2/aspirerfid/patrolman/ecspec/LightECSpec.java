package org.ow2.aspirerfid.patrolman.ecspec;

import java.util.Enumeration;
import java.util.Vector;

import org.ow2.aspirerfid.patrolman.questionnaire.Questionnaire;

public class LightECSpec {
	/** ECSpec name */
	private String m_name;

	/** Logical readers list */
	private Vector m_logicalReaders = new Vector();

	/** Reports specifications */
	private Vector m_reportSpecs = new Vector();

	/** Boundary specifications : keep reports in memory */
	private boolean m_keepReports = false;

	/**
	 * Adds a logical reader
	 * 
	 * @param reportName
	 *            The added logical reader
	 */
	public void addLogicalReader(String name) {
		if (m_logicalReaders.contains(name))
			return;

		m_logicalReaders.addElement(name);
	}

	/**
	 * Adds a report specification
	 * 
	 * @param spec
	 *            the added specification
	 * @return False if the specification already exists
	 */
	public boolean addReportSpec(LightECReportSpec spec) {
		if (m_reportSpecs.contains(spec))
			return false;

		m_reportSpecs.addElement(spec);
		return true;
	}

	/**
	 * Retrieves a specification
	 * 
	 * @param reportName
	 *            the specification reportName
	 * @return the specification, or null if it doesn't exist
	 */
	public LightECReportSpec getReportSpec(String name) {
		Enumeration items = m_reportSpecs.elements();

		while (items.hasMoreElements()) {
			LightECReportSpec reportSpec = (LightECReportSpec) items
					.nextElement();

			if (reportSpec.getReportName().equals(name))
				return reportSpec;
		}

		return null;
	}

	/**
	 * Returns an enumerable list of contained report specifications
	 * 
	 * @return An Enumeration of LightECReportSpec objects
	 */
	public Enumeration getReportSpecs() {
		return m_reportSpecs.elements();
	}

	/**
	 * Returns the first questionnaire corresponding to the tag unique ID
	 * (pattern tag in ECSpec)
	 * 
	 * @param tagInfo
	 *            Tag information (unique ID or record type)
	 * @return The first questionnaire found, or null
	 */
	public Questionnaire findAssociatedQuestionnaire(String tagInfo) {
		if (tagInfo == null)
			return null;

		Enumeration items = m_reportSpecs.elements();

		while (items.hasMoreElements()) {
			LightECReportSpec reportSpec = (LightECReportSpec) items
					.nextElement();

			Questionnaire qst = reportSpec.getQuestionnaire(tagInfo);
			if (qst != null)
				return qst;
		}

		return null;
	}

	/**
	 * Returns the first questionnaire corresponding to the tag unique ID
	 * (pattern tag in ECSpec)
	 * 
	 * @param recordsTypes
	 *            Tag records types (String)
	 * @return The first questionnaire found, or null
	 */
	public Questionnaire findAssociatedQuestionnaire(Vector recordsTypes) {
		Enumeration items = m_reportSpecs.elements();

		while (items.hasMoreElements()) {
			LightECReportSpec reportSpec = (LightECReportSpec) items
					.nextElement();

			Enumeration records = recordsTypes.elements();
			while (records.hasMoreElements()) {
				Questionnaire qst = reportSpec.getQuestionnaire(records
						.nextElement().toString());
				if (qst != null)
					return qst;
			}
		}

		return null;
	}

	/**
	 * @param m_name
	 *            the ECSpec name
	 */
	public void setName(String name) {
		m_name = name;
	}

	/**
	 * @return the ECSpec name
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * @param m_keepReports
	 *            Do or do not keep reports in memory
	 */
	public void setKeepReports(boolean keepReports) {
		m_keepReports = keepReports;
	}

	/**
	 * @return Do we need to keep reports in memory ?
	 */
	public boolean getKeepReports() {
		return m_keepReports;
	}
}
