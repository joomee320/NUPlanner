package nuplanner.controller;

import nuplanner.strategy.Strategy;
import nuplanner.model.IEvent;

/**
 * The features interface contains all the methods for all kinds of user actions
 * that the view may send to the controller.
 */
public interface Features {
  /**
   * The method that uploads the xml file to the file path.
   * @param filePath the string of the file path
   */
  void onUploadXMLFile(String filePath);

  /**
   * The method that saves the schedules to the directory path.
   * @param directoryPath the string of the directory path
   */
  void onSaveSchedules(String directoryPath);

  /**
   * The method that creates an event when the create button is clicked.
   * @param uid the string of the user
   * @param event the event being created
   */
  void onCreateEvent(String uid, IEvent event);

  /**
   * Changes an event with the given modified event.
   * @param uid the user id that the event is being change
   * @param originalEvent the event that is going to be changed
   * @param newEvent the new modified event being changed
   */
  void onModifyEvent(String uid, IEvent originalEvent, IEvent newEvent);

  /**
   * The method that removes an event from the model.
   * @param event the event to be removed
   */
  void onRemoveEvent(IEvent event);

  /**
   * The method that calls the create event frame.
   */
  Strategy onCreateEventFrame();

  /**
   * The method that happens when the user is switched.
   * @param userId the string of the user
   */
  void onSwitchUser(String userId);

  /**
   * The method that handles when something has been clicked.
   * @param e the event created
   */
  void handleClick(IEvent e);
}
