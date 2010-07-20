package org.ow2.aspirerfid.patrolman.ecspec;

import java.util.Enumeration;
import java.util.Vector;

import org.ow2.aspirerfid.patrolman.questionnaire.Questionnaire;

public class LightECReportSpec {
	/** Parent ECSpec name */
	private String m_ecSpecName = null;
	
	/** ECReport name */
	public String reportName = null;

	/** Report set (should always be CURRENT) */
	public String set = "CURRENT";

	/** Report includes tag */
	public boolean includeTag = false;

	/** Report includes count */
	public boolean includeCount = false;

	/** Associated questionnaires */
	private Vector m_questionnaires;

	public LightECReportSpec(String ecSpecName) {
		m_questionnaires = new Vector();
		m_ecSpecName = ecSpecName;
	}

	/**
	 * LightECReportSpec objects are equals if they have the same reportName
	 */
	public boolean equals(Object obj) {
		if (obj instanceof LightECReportSpec) {
			return ((LightECReportSpec) obj).reportName.equals(reportName);
		}
		return super.equals(obj);
	}

	/**
	 * Adds a questionnaire to the report
	 * 
	 * @param questionnaire
	 *            Questionnaire to be added
	 */
	public void addQuestionnaire(Questionnaire questionnaire) {
		questionnaire.setReportSpec(this);
		m_questionnaires.addElement(questionnaire);
	}

	/**
	 * Returns the questionnaire corresponding to the given tag UID
	 * 
	 * @param tagUID
	 *            The UID of a read tag
	 * @return The questionnaire associated to the tag UID, or null
	 */
	public Questionnaire getQuestionnaire(String tagUID) {
		Enumeration en = m_questionnaires.elements();
		while (en.hasMoreElements()) {
			Questionnaire qst = (Questionnaire) en.nextElement();
			if (qst.handlesTag(tagUID))
				return qst;
		}
		return null;
	}

	/**
	 * Returns questionnaires associated to this ECReportSpec
	 * 
	 * @return All questionnaires of this ECReportSpec
	 */
	public Vector getQuestionnaires() {
		return m_questionnaires;
	}
	
	public String getECSpecName() {
		if(m_ecSpecName == null)
			return "PatrolmanECSpec";
		
		return m_ecSpecName;
	}
}
