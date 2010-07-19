package org.ow2.aspirerfid.patrolman.ecspec;

import org.ow2.aspirerfid.patrolman.questionnaire.Questionnaire;

public class LightECReportSpec {
	/** ECSpec name */
	public String name = null;

	/** Report set (must be CURRENT) */
	public String set = "CURRENT";

	/** Report includes tag */
	public boolean includeTag = false;

	/** Report includes count */
	public boolean includeCount = false;
	
	/** Associated questionnaire */
	private Questionnaire m_questionnaire;

	/**
	 * LightECReportSpec objects are equals if they have the same name
	 */
	public boolean equals(Object obj) {
		if (obj instanceof LightECReportSpec) {
			return ((LightECReportSpec) obj).name.equals(name);
		}
		return super.equals(obj);
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		m_questionnaire = questionnaire;
		m_questionnaire.setReportSpec(this);
	}

	public Questionnaire getQuestionnaire() {
		return m_questionnaire;
	}
}
