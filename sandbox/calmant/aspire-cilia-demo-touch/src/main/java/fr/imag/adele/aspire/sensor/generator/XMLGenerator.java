package fr.imag.adele.aspire.sensor.generator;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import liglab.adele.aspire.common.tag.Tag;
import fr.liglab.adele.cilia.Data;
import fr.liglab.adele.cilia.framework.IProcessor;

/**
 * Generate a XML file with the list of tags.
 * 
 * @author Gabriel
 * @author Anthony Gelibert
 * @version 2.0.0
 */
public class XMLGenerator implements IProcessor {

   /*
    * (non-Javadoc)
    * 
    * @see fr.liglab.adele.cilia.framework.IProcessor#process(java.util.List)
    */
   public List<Data> process(final List dataSet) {

      String tagsReport = "";
      String sensorReport = "";
      String report = "";

      for (Object object : dataSet) {
         Data data = (Data) object;
         String name = data.getName();

         if (name.equals("tags")) {
            int round = (Integer) data.getProperty("round");
            List<Tag> original = (List<Tag>) data.getContent();
            tagsReport = generateReportTag(original, round);
         }
         if (name.equals("sensor")) {
            List<Dictionary<String, Object>> sensorData = (List<Dictionary<String, Object>>) data.getContent();
            String sensorId = (String) data.getProperty("sensorId");
            sensorReport = sensorReport + generateReportSensor(sensorData, sensorId);
         }
      }

      report = tagsReport + sensorReport;

      final List<Data> temp = new ArrayList<Data>(1);
      temp.add(new Data(report));
      return temp;
   }

   /**
    * Generate a XML report.
    * 
    * 
    * @param tags The list of tags
    * @param round The current round
    * @return The report in XML (String)
    */
   private static String generateReportTag(List<Tag> tags, final int round) {
      final StringBuilder buf = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tag-list round=\""
            + round + "\">\n");
      buf.append("\t<count>").append(tags.size()).append("</count>\n");
      for (final Tag tag : tags) {
         buf.append("\t<tag>").append(tag.getId()).append("</tag>\n");

      }
      return buf.append("</tag-list>\n").toString();
   }

   private static String generateReportSensor(List<Dictionary<String, Object>> sensorData, String sensorId) {
      final StringBuilder buf = new StringBuilder("<sensor>\n");
      buf.append("\t<id>").append(sensorId).append("</id>\n");
      for (Dictionary<String, Object> dictionary : sensorData) {
         for (Enumeration<String> e = dictionary.keys(); e.hasMoreElements();) {
            String name = e.nextElement();
            buf.append("\t<entry>\n");
            buf.append("\t<name>").append(name).append("</name>\n");
            buf.append("\t<value>").append(dictionary.get(name)).append("</value>\n");
            buf.append("\t</entry>\n");
         }
      }

      return buf.append("</sensor>\n").toString();
   }

}
