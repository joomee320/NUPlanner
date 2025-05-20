package nuplanner.model;

import java.util.Map;

/**
 * Defines the contract for a central system within the planning application that doesn't allow
 * modification for the use of the views. This interface supports observation methods that returns
 * all the users registered in the system, and other observation methods.
 */
public interface ReadOnlySystems {

  /**
   * Gets the map of all the users to be used in for loops.
   * @return the map of all the users
   */
  Map<String, IUser> getAllUsers();

  /**
   * Retrieves a user by their UID.
   * @param uid the unique identifier of the user
   * @return the User object corresponding to the UID, or null if no such user exists
   */
  IUser getUser(String uid);

  /**
   * Gets the passed in user's schedule and displays to the client.
   * @param uid the string that represents the user
   * @return the given user's schedule
   */
  ISchedule getUserSchedule(String uid);

  /**
   * Checks if an event conflicts with the schedules of all invited users.
   *
   * @param event The event to check for conflicts.
   * @return true if the event conflicts with any invited user's schedule; false otherwise.
   */
  boolean isEventConflicting(IEvent event);
}
