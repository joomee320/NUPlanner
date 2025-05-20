package nuplanner.model;

/**
 * Defines the contract for a mutable system within the planning application, extending the
 * capabilities of {@code ReadOnlySystems} by allowing modifications.
 * This interface supports operations to upload and save schedules, manage users,
 * and manipulate events within the system.
 */
public interface MutableSystems extends ReadOnlySystems {
  /**
   * The XML file is converted into a schedule by using the XMLParser class.
   * @param filePath the string of where the file is located
   */
  void uploadXMLFileToSystem(String filePath);

  /**
   * The method that saves the schedule to a file.
   * @param filePath the string where the file should be saved
   */
  void saveXMLFileFromSystem(String filePath);

  /**
   * Adds a user to the list of users.
   * @param user the user to add
   */
  void addUser(IUser user);

  /**
   * The user that needs to be removed from the list of users.
   * @param user the user to be removed
   */
  void removeUser(IUser user);

  /**
   * Creates an event that is added to a user's schedule.
   * @param uid the string for the user id
   * @param event the event that is going to be created
   */
  void createEvent(String uid, IEvent event);

  /**
   * Changes an event with the given modified event.
   * @param uid the user id that the event is being change
   * @param event the event that is going to be changed
   * @param modifiedEvent the new modified event being changed
   */
  void modifyEvent(String uid, IEvent event, IEvent modifiedEvent);

  /**
   * The method that deletes an event from every user's schedule.
   * @param event the event to be deleted
   */
  void deleteEvent(IEvent event);
}