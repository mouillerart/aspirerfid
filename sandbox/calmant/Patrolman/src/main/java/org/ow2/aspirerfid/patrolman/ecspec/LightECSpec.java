package org.ow2.aspirerfid.patrolman.ecspec;

import java.util.Enumeration;
import java.util.Vector;

import org.ow2.aspirerfid.patrolman.questionnaire.Questionnaire;

public class LightECSpec {
	/** Logical readers list */
	private Vector m_logicalReaders = new Vector();

	/** Reports specifications */
	private Vector m_reportSpecs = new Vector();

	/** Boundary specifications : keep reports in memory */
	public boolean m_keepReports = false;

	/**
	 * Adds a logical reader
	 * 
	 * @param name
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
	 * @param name
	 *            the specification name
	 * @return the specification, or null if it doesn't exist
	 */
	public LightECReportSpec getReportSpec(String name) {
		Enumeration items = m_reportSpecs.elements();

		while (items.hasMoreElements()) {
			LightECReportSpec reportSpec = (LightECReportSpec) items
					.nextElement();

			if (reportSpec.name.equals(name))
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
	 * @param tagUID
	 *            Tag's unique ID
	 * @return The first questionnaire found, or null
	 */
	public Questionnaire findAssociatedQuestionnaire(String tagUID) {
		Enumeration items = m_reportSpecs.elements();

		while (items.hasMoreElements()) {
			LightECReportSpec reportSpec = (LightECReportSpec) items
					.nextElement();

			Questionnaire qst = reportSpec.getQuestionnaire();
			if (qst.handlesTag(tagUID))
				return qst;
		}

		return null;
	}
}
