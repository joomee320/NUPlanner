package nuplanner.model;

import org.w3c.dom.Document;
import java.util.List;

/**
 * The interface for a schedule and all its methods.
 */
public interface ISchedule {
  /**
   * Adds an event to the schedule.
   * @param event the event to be added
   */
  void addEvent(IEvent event);

  /**
   * It modifies an existing event to new Event detail provided.
   * @param event the event to be modified
   * @param newEventDetails the modified event
   */
  void modifyEvent(IEvent event, IEvent newEventDetails);

  /**
   * It removes an event from the schedule.
   * @param event the event to be removed
   */
  void removeEvent(IEvent event);

  /**
   * The method that merges an event to a Schedule.
   * @param event the event being merged
   */
  void merge(IEvent event);

  /**
   * The method that decides if an event is overlapping or not.
   * @param newEvent the new event
   * @param exclude the events to exclude
   * @return true if the new event overlaps with any other events
   */
  boolean isOverlapping(IEvent newEvent, IEvent... exclude);

  /**
   * It returns the list of all events in the schedule.
   * @return the list of the events
   */
  List<IEvent> getAllEvents();

  /**
   * This method changes the schedule object to Document object.
   * @return the schedule to a document.
   */
  Document toDocument();
}
