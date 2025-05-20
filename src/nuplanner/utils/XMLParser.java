package nuplanner.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;

import nuplanner.model.ISchedule;
import nuplanner.model.IUser;
import nuplanner.model.MutableSystems;
import nuplanner.model.User;


/**
 * Creates the XML Parser class which allows the XML files to be read and to
 * write things to the XML files.
 */
public class XMLParser {

  /**
   * This method write items to the given file.
   * @param filePath the path to the file so the Document can be written into it
   * @param document the document to add to the file
   */
  public static void writeDocumentToFile(String filePath, Document document) {
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(new File(filePath));
      transformer.transform(source, result);
    } catch (TransformerException ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }

  /**
   * This method reads the schedule from the file.
   * @param filePath the string passed in to find the file.
   */
  public static void readScheduleFromFile(MutableSystems cs,
                                          String filePath,
                                          Map<String, IUser> users) {
    try {
      File xmlFile = new File(filePath);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();
      Node scheduleNode = doc.getElementsByTagName("schedule").item(0);
      if (scheduleNode.getNodeType() == Node.ELEMENT_NODE) {
        Element scheduleElement = (Element) scheduleNode;
        String userId = scheduleElement.getAttribute("id");
        ISchedule schedule;
        NodeList eventList = scheduleElement.getElementsByTagName("event");
        if (users.isEmpty()) {
          IUser u = new User(userId);
          cs.addUser(u);
          schedule = u.mergeSchedule(eventList);
          //u.setSchedule(schedule);
        }
        else {
          for (IUser u: users.values()) {
            schedule = u.mergeSchedule(eventList);
            u.setSchedule(schedule);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}