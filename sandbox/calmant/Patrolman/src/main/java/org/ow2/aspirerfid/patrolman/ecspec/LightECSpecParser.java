package org.ow2.aspirerfid.patrolman.ecspec;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Vector;

import org.kxml2.io.KXmlParser;
import org.ow2.aspirerfid.patrolman.Patrolman;
import org.ow2.aspirerfid.patrolman.questionnaire.Questionnaire;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Parses a LightECSpec XML file with kXML2
 * 
 * @author Thomas Calmant
 */
public class LightECSpecParser {
	/** kXML2 parser */
	private KXmlParser m_parser;
	
	/** Parent MIDlet */
	private Patrolman m_patrolman;

	/**
	 * Initializes the kXml parser
	 */
	public LightECSpecParser(Patrolman midlet) {
		m_parser = new KXmlParser();
		m_patrolman = midlet;
	}

	/**
	 * Parses an XML ECSpec string
	 * 
	 * @param data
	 *            XML file content
	 * @return The parsed ECSpec
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	public LightECSpec parseString(String data) throws XmlPullParserException,
			IOException {
		m_parser.setInput(new InputStreamReader(new ByteArrayInputStream(data
				.getBytes())));

		m_parser.nextTag();
		LightECSpec ecSpec = parseECSpec();
		m_patrolman.addECSpec(ecSpec);
		return ecSpec;
	}

	/**
	 * Internal parsing start point
	 * 
	 * @return The parsed ECSpec
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	private LightECSpec parseECSpec() throws IOException,
			XmlPullParserException {
		LightECSpec ecspec = new LightECSpec();

		System.out
				.println(m_parser.getName() + " - " + m_parser.getNamespace());

		m_parser.require(XmlPullParser.START_TAG, null, "ale:ECSpec");

		while (m_parser.nextTag() != XmlPullParser.END_TAG) {
			String tagName = m_parser.getName();
			if (tagName.equals("reportSpecs"))
				parseReportSpecs(ecspec);
			else if (tagName.equals("boundarySpec"))
				parseBoundarySpec(ecspec);
			else if (tagName.equals("logicalReaders"))
				parseLogicalReaders(ecspec);
			else
				ignoreTag(tagName);
		}

		m_parser.require(XmlPullParser.END_TAG, null, "ale:ECSpec");
		return ecspec;
	}

	/**
	 * Parses the reportSpecs tag, containing the form to be shown
	 * 
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	private void parseReportSpecs(LightECSpec ecspec)
			throws XmlPullParserException, IOException {
		m_parser.require(XmlPullParser.START_TAG, null, "reportSpecs");

		// list of report specifications
		while (m_parser.nextTag() != XmlPullParser.END_TAG) {
			m_parser.require(XmlPullParser.START_TAG, null, "reportSpec");

			LightECReportSpec reportSpec = new LightECReportSpec();

			for (int i = 0; i < m_parser.getAttributeCount(); i++)
				if (m_parser.getAttributeName(i).equals("reportName")) {
					reportSpec.name = m_parser.getAttributeValue(i);
					break;
				}

			// report specification properties
			while (m_parser.nextTag() != XmlPullParser.END_TAG) {
				String tagName = m_parser.getName();

				// Report set may always be current
				if (tagName.equals("reportSet")) {
					for (int i = 0; i < m_parser.getAttributeCount(); i++)
						if (m_parser.getAttributeName(i).equals("set")) {
							reportSpec.set = m_parser.getAttributeValue(i);
						}
					m_parser.nextTag();
					m_parser.require(XmlPullParser.END_TAG, null, "reportSet");
				}

				// Output may include tags, count or no extra information
				else if (tagName.equals("output")) {
					for (int i = 0; i < m_parser.getAttributeCount(); i++) {
						String attrName = m_parser.getAttributeName(i);

						if (attrName.equals("includeCount"))
							reportSpec.includeCount = true;
						else if (attrName.equals("includeTag"))
							reportSpec.includeTag = true;
					}

					m_parser.nextTag();
					m_parser.require(XmlPullParser.END_TAG, null, "output");
				}

				// GroupSpec : filtered with a pattern, contains the
				// questionnaire
				// => Parse in a special function
				else if (tagName.equals("groupSpec"))
					parseGroupSpec(reportSpec);

				else
					ignoreTag(tagName);
			}

			// Add read report specification to ECSpec
			ecspec.addReportSpec(reportSpec);

			m_parser.require(XmlPullParser.END_TAG, null, "reportSpec");
		}

		m_parser.require(XmlPullParser.END_TAG, null, "reportSpecs");
	}

	/**
	 * Parses the boundarySpec list
	 * 
	 * In lwECSpec format, we just look for the extension tag to see if we need
	 * to save old (sent) reports in memory. All other tags are ignored.
	 * 
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	private void parseBoundarySpec(LightECSpec ecspec)
			throws XmlPullParserException, IOException {
		m_parser.require(XmlPullParser.START_TAG, null, "boundarySpec");

		while (m_parser.nextTag() != XmlPullParser.END_TAG) {
			String tagName = m_parser.getName();

			// Extension tag (optional, so don't require it)
			if (tagName.equals("extension")) {
				// Extensions
				while (m_parser.nextTag() != XmlPullParser.END_TAG) {

					// keep_reports tag : Keep reports in memory
					if (m_parser.getName().equals("keep_reports")) {
						// False unless we read true.
						if (m_parser.nextText().equals("true"))
							ecspec.m_keepReports = true;
					}
					// Other extensions ignored
					else
						ignoreTag(m_parser.getName());
				}
			}
			// All other tags ignored
			else
				ignoreTag(tagName);

			m_parser.require(XmlPullParser.END_TAG, null, tagName);
		}

		m_parser.require(XmlPullParser.END_TAG, null, "boundarySpec");
	}

	/**
	 * Parses the logicalReaders list
	 * 
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	private void parseLogicalReaders(LightECSpec ecspec)
			throws XmlPullParserException, IOException {
		m_parser.require(XmlPullParser.START_TAG, null, "logicalReaders");

		while (m_parser.nextTag() != XmlPullParser.END_TAG) {
			m_parser.require(XmlPullParser.START_TAG, null, "logicalReader");

			// Add the logical reader name to the list
			ecspec.addLogicalReader(m_parser.nextText());

			m_parser.require(XmlPullParser.END_TAG, null, "logicalReader");
		}

		m_parser.require(XmlPullParser.END_TAG, null, "logicalReaders");
	}

	/**
	 * Parses a groupSpec tag (can contain a questionnaire)
	 * 
	 * @param reportSpec
	 *            The parent report specification
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	private void parseGroupSpec(LightECReportSpec reportSpec)
			throws XmlPullParserException, IOException {
		m_parser.require(XmlPullParser.START_TAG, null, "groupSpec");

		String pattern = null;
		Questionnaire qst = null;

		while (m_parser.nextTag() != XmlPullParser.END_TAG) {
			String tagName = m_parser.getName();

			// Pattern (optional)
			if (tagName.equals("pattern")) {
				pattern = m_parser.nextText();
			}

			// Extension (Questionnaire)
			else if (tagName.equals("extension")) {
				qst = parseExtension();
			}

			// Ignore the rest
			else
				ignoreTag(tagName);
		}

		if (qst != null) {
			qst.setPattern(pattern);
			reportSpec.setQuestionnaire(qst);
		}

		m_parser.require(XmlPullParser.END_TAG, null, "groupSpec");
	}

	/**
	 * Parses the extension tag of the groupSpec tag. Tries to create a
	 * questionnaire from the XML file
	 * 
	 * @return A questionnaire, if found or null
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	private Questionnaire parseExtension() throws XmlPullParserException,
			IOException {
		Questionnaire qst = null;
		m_parser.require(XmlPullParser.START_TAG, null, "extension");

		while (m_parser.nextTag() != XmlPullParser.END_TAG) {
			if (m_parser.getName().equals("questionnaire"))
				qst = createQuestionnaire();
			else
				ignoreTag(m_parser.getName());
		}

		m_parser.require(XmlPullParser.END_TAG, null, "extension");
		return qst;
	}

	/**
	 * Creates a questionnaire from the XML file
	 * 
	 * @return The described questionnaire, null if not found or invalid
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	private Questionnaire createQuestionnaire() throws XmlPullParserException,
			IOException {
		m_parser.require(XmlPullParser.START_TAG, null, "questionnaire");

		String title = null, id = null;

		for (int i = 0; i < m_parser.getAttributeCount(); i++) {
			String attrName = m_parser.getAttributeName(i);

			if (attrName.equals("title"))
				title = m_parser.getAttributeValue(i);
			else if (attrName.equals("id"))
				id = m_parser.getAttributeValue(i);
		}

		if (id == null || id.length() == 0)
			return null;

		Questionnaire qst = new Questionnaire(m_patrolman, title, id);

		while (m_parser.nextTag() != XmlPullParser.END_TAG) {
			String tagName = m_parser.getName();

			if (tagName.equals("textual"))
				addTextual(qst);
			else if (tagName.equals("choiceList"))
				addChoiceList(qst);
		}
		
		qst.finalizeUI();

		m_parser.require(XmlPullParser.END_TAG, null, "questionnaire");
		return qst;
	}

	/**
	 * Prepares a textual question and adds it to the parent questionnaire
	 * 
	 * @param qst
	 *            Parent questionnaire
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	private void addTextual(Questionnaire qst) throws XmlPullParserException,
			IOException {
		m_parser.require(XmlPullParser.START_TAG, null, "textual");

		String id = null;
		String label = null;
		String default_answer = null;
		String correct_answer = null;

		for (int i = 0; i < m_parser.getAttributeCount(); i++) {
			String attrName = m_parser.getAttributeName(i);

			if (attrName.equals("id"))
				id = m_parser.getAttributeValue(i);
			else if (attrName.equals("default")) {
				default_answer = m_parser.getAttributeValue(i);
			}
		}

		while (m_parser.nextTag() != XmlPullParser.END_TAG) {
			String tagName = m_parser.getName();

			if (tagName.equals("label"))
				label = m_parser.nextText();
			else if (tagName.equals("default"))
				default_answer = m_parser.nextText();
			else if (tagName.equals("answer"))
				correct_answer = m_parser.nextText();
		}

		qst.addTextualQuestion(id, label, default_answer, correct_answer);
		m_parser.require(XmlPullParser.END_TAG, null, "textual");
	}

	/**
	 * Prepares a choice list question (unique or multiple) and adds it to the
	 * questionnaire
	 * 
	 * @param qst
	 *            Parent questionnaire
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	private void addChoiceList(Questionnaire qst)
			throws XmlPullParserException, IOException {
		m_parser.require(XmlPullParser.START_TAG, null, "choiceList");

		boolean multiple = false;
		String id = null;
		String label = null;
		int default_answer = 0;
		Vector choices = new Vector();

		Vector correct_answers = null;
		int correct_answer = 0;

		// Get parameters
		for (int i = 0; i < m_parser.getAttributeCount(); i++) {
			String attrName = m_parser.getAttributeName(i);

			if (attrName.equals("multiple")
					&& m_parser.getAttributeValue(i).equals("true")) {
				multiple = true;
			} else if (attrName.equals("id"))
				id = m_parser.getAttributeValue(i);
			else if (attrName.equals("default")) {
				default_answer = Integer
						.parseInt(m_parser.getAttributeValue(i));
			}
		}

		if (multiple)
			correct_answers = new Vector();

		// List choices
		while (m_parser.nextTag() != XmlPullParser.END_TAG) {
			String tagName = m_parser.getName();

			if (tagName.equals("label"))
				label = m_parser.nextText();
			else if (tagName.equals("choice")) {
				choices.addElement(m_parser.nextText());

				// TODO: test if value is an answer
			}
		}

		// Convert the choice vector to a string table
		String[] str_choices = new String[choices.size()];
		int i = 0;
		Enumeration e = choices.elements();

		while (e.hasMoreElements())
			str_choices[i++] = (String) e.nextElement();

		// Convert the answers vector to an integer table
		int[] int_correct_answers = null;

		if (correct_answers != null) {
			int_correct_answers = new int[correct_answers.size()];
			i = 0;
			e = correct_answers.elements();

			while (e.hasMoreElements())
				int_correct_answers[i++] = ((Integer) e.nextElement())
						.intValue();
		}

		// Add it to the form
		if (multiple)
			qst.addMultipleChoice(id, label, str_choices, default_answer,
					int_correct_answers);
		else
			qst.addUniqueChoice(id, label, str_choices, default_answer,
					correct_answer);

		m_parser.require(XmlPullParser.END_TAG, null, "choiceList");
	}

	/**
	 * Ignores the content of the indicated tag. The parser must be on the
	 * START_TAG position of this tag.
	 * 
	 * @param tagName
	 *            Tag to be ignored
	 * @throws XmlPullParserException
	 *             Error while parsing the file
	 * @throws IOException
	 *             Error while opening/reading the file
	 */
	private void ignoreTag(String tagName) throws XmlPullParserException,
			IOException {
		m_parser.require(XmlPullParser.START_TAG, null, tagName);

		while (m_parser.next() != XmlPullParser.END_TAG
				&& (m_parser.getName() == null || !m_parser.getName().equals(
						tagName))) {
			// ignore the tag
		}

		m_parser.require(XmlPullParser.END_TAG, null, tagName);
	}
}
