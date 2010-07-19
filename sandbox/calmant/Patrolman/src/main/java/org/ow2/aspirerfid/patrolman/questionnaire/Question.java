package org.ow2.aspirerfid.patrolman.questionnaire;

/**
 * Questions standard interface
 * 
 * @author Thomas Calmant
 * 
 */
public interface Question {
	/**
	 * Retrieve question's identifier
	 * 
	 * @return question's identifier
	 */
	public String getId();

	/**
	 * Test if the answer is correct. If not verification can be done, return
	 * true
	 * 
	 * @return True if the answer is correct, or no control can be made
	 */
	public boolean isCorrect();
	
	/**
	 * Reset the question state (no entry/no selection/...)
	 */
	public void clear();
	
	/**
	 * Returns the XML representation of the current state of the answer
	 * @return A ECReport-ready XML representation
	 */
	public String toXML();
}
