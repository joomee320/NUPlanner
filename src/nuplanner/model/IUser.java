package nuplanner.model;

import org.w3c.dom.NodeList;

/**
 * This is the user interface that has all the user methods.
 */
public interface IUser {
  /**
   * This method merges the schedule based on the event list from the XML file.
   * @param eventList the list of events
   * @return the new schedule
   */
  ISchedule mergeSchedule(NodeList eventList);

  /**
   * Sets the schedule to a new schedule.
   * @param s the schedule to be set to
   */
  void setSchedule(ISchedule s);

  /**
   * The string of the user's ID.
   * @return the string of the ID
   */
  String getUid();

  /**
   * The method that gets the user's schedule.
   * @return the user's schedule
   */
  ISchedule getSchedule();

  /**
   * The method that adds the event to the schedule.
   * @param event the event to be added
   */
  void addEventToSchedule(IEvent event);

  /**
   * The method that removes the event from the schedule.
   * @param event the event to be removed.
   */
  void removeEventFromSchedule(IEvent event);

  /**
   * Modifies an existing event in the user's schedule.
   * If the event exists and is successfully modified,
   * the method returns true, otherwise false.
   * @param originalEvent The name of the event to be modified.
   * @param newEventDetails The new details of the event.
   */
  void modifyEvent(IEvent originalEvent, IEvent newEventDetails);
}
