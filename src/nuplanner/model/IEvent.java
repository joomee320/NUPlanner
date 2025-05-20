package nuplanner.model;

import java.time.LocalTime;
import java.util.List;

/**
 * The interface for an Event and all its methods.
 */
public interface IEvent {
  /**
   * This returns the name of the Event.
   * @return the name as a string
   */
  String getName();

  /**
   * This returns the place of the Event.
   * @return the location as a string
   */
  String getPlace();

  /**
   * This returns whether the Event is online or not.
   * @return if it is online as a boolean
   */
  boolean isOnline();

  /**
   * This returns the start time of the Event.
   * @return the start time
   */
  LocalTime getStartTime();

  /**
   * This returns the end time of the Event.
   * @return the end time
   */
  LocalTime getEndTime();

  /**
   * This returns the start day of the Event.
   * @return the start day
   */
  DayOfWeek getStartDay();

  /**
   * This returns the end day of the Event.
   * @return the end day
   */
  DayOfWeek getEndDay();

  /**
   * This returns the host user of the Event.
   * @return the host user
   */
  IUser getHostUser();

  /**
   * This returns the invited users of the Event.
   * @return the list of the users
   */
  List<IUser> getInvitedUsers();

  /**
   * The list of the users invited to the event.
   * @return the string of the users invited to the event
   */
  String invites();
}
