package nuplanner.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the class that represents an event with all the attributes that an event has.
 * The name of event, the place the event is being held are defined as String, Starting and Ending
 * days are defined by the enum class called Day, StartTime and EndTime are defined by the LocalTime
 * java class and the list of Users are represented as a list of users.
 */
public class Event implements IEvent {
  private final String eventName;
  private final Day startDay;
  private final LocalTime startTime;
  private final Day endDay;
  private final LocalTime endTime;
  private final boolean online;
  private final String place;
  private final IUser hostUser;
  private final List<IUser> invitedUsers;

  /**
   * Creates the constructor that creates the event.
   * @param name the name of the event
   * @param startDay the day the event starts
   * @param startTime the time the event starts
   * @param endDay the day the event ends
   * @param endTime the time the event ends
   * @param online if the event is online or not
   * @param place the place the event is occurring
   * @param hostUser the user hosting the event
   * @param invitedUsers the list of the invited users
   */
  public Event(String name, Day startDay, LocalTime startTime, Day endDay, LocalTime endTime,
               boolean online, String place, IUser hostUser, List<IUser> invitedUsers) {
    if (name == null || place == null || startDay == null || endDay == null || startTime == null
            || endTime == null || hostUser == null || invitedUsers == null) {
      throw new IllegalStateException("Cannot have null arguments.");
    }
    if (startDay.equals(endDay) && startTime.equals(endTime)) {
      throw new IllegalArgumentException("Event cannot start and end at the same time "
              + "on the same day.");
    }
    if (!isValidEventDuration(startDay, startTime, endDay, endTime)) {
      throw new IllegalArgumentException("The event duration exceeds the allowed maximum "
              + "of 6 days, 23 hours, and 59 minutes.");
    }
    this.eventName = name;
    this.place = place;
    this.online = online;
    this.startDay = startDay;
    this.startTime = startTime;
    this.endDay = endDay;
    this.endTime = endTime;
    this.hostUser = hostUser;
    this.invitedUsers = new ArrayList<>(invitedUsers);
  }

  /**
   * The method that calculates the duration of the event.
   * @param startDay the start day
   * @param startTime the start time
   * @param endDay the end day
   * @param endTime the end time
   * @return the duration as a long
   */
  protected long calculateEventDurationInMinutes(Day startDay, LocalTime startTime,
                                                 Day endDay, LocalTime endTime) {
    int startDayIndex = startDay.ordinal();
    int endDayIndex = endDay.ordinal();
    int dayDifference = endDayIndex - startDayIndex;
    if (dayDifference < 0) {
      dayDifference += 7;
    }
    if (dayDifference == 0 && startTime.isAfter(endTime)) {
      dayDifference += 6;
    }
    long durationMinutes;
    if (endTime.isBefore(startTime)) {
      durationMinutes = Duration.ofDays(1).toMinutes()
              + Duration.between(startTime, endTime).toMinutes();
    } else {
      durationMinutes = Duration.between(startTime, endTime).toMinutes();
    }
    return dayDifference * 24 * 60 + durationMinutes;
  }

  /**
   * Checks if the provided duration is valid.
   * @param startDay the start day
   * @param startTime the start time
   * @param endDay the end day
   * @param endTime the end time
   * @return true if the duration is valid
   */
  private boolean isValidEventDuration(Day startDay,
                                       LocalTime startTime,
                                       Day endDay,
                                       LocalTime endTime) {
    final long maxDurationMinutes = 6 * 24 * 60 + 23 * 60 + 59;
    long eventDurationMinutes = calculateEventDurationInMinutes(startDay,
            startTime, endDay, endTime);
    return eventDurationMinutes <= maxDurationMinutes;
  }

  @Override
  public String getName() {
    return eventName;
  }

  @Override
  public String getPlace() {
    return place;
  }

  @Override
  public boolean isOnline() {
    return online;
  }

  @Override
  public LocalTime getStartTime() {
    return startTime;
  }

  @Override
  public LocalTime getEndTime() {
    return endTime;
  }

  @Override
  public Day getStartDay() {
    return startDay;
  }

  @Override
  public Day getEndDay() {
    return endDay;
  }

  @Override
  public IUser getHostUser() {
    return hostUser;
  }

  @Override
  public List<IUser> getInvitedUsers() {
    return invitedUsers;
  }

  @Override
  public String invites() {
    StringBuilder result = new StringBuilder();
    for (IUser invite: this.getInvitedUsers()) {
      result.append("\t").append(invite.getUid()).append("\n");
    }
    return result.toString();
  }
}