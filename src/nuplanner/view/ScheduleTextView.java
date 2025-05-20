package nuplanner.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nuplanner.model.Day;
import nuplanner.model.DayOfWeek;
import nuplanner.model.IEvent;
import nuplanner.model.IUser;
import nuplanner.model.MutableSystems;

/**
 * Puts the schedule of a given model into a text view version of the schedule.
 * Converts the entire schedule of the model into a String version of the schedule that
 * allows for the user to read through the events and is properly formatted.
 */
public class ScheduleTextView {
  private final MutableSystems mutableSystems;

  /**
   * Creates the constructor that initializes the passed in model.
   * @param mutableSystems The passed in model for the view
   */
  public ScheduleTextView(MutableSystems mutableSystems) {
    this.mutableSystems = mutableSystems;
  }

  /**
   * The method that returns the string version of the view.
   * @return the string version of the view
   */
  public String toString() {
    StringBuilder result = new StringBuilder();
    Map<String, IUser> users = this.mutableSystems.getAllUsers();
    for (IUser u: users.values()) {
      result.append("user: ").append(u.getUid()).append("\n");
      List<DayOfWeek> daysInEvents = this.daysInEvents(u.getSchedule().getAllEvents());
      result.append(this.days(u.getSchedule().getAllEvents(), daysInEvents));
    }
    return result.toString();
  }

  /**
   * The method that returns what start days are in the given list of events.
   * @param events the passed in list of events
   * @return a list of all the days in the events
   */
  private List<DayOfWeek> daysInEvents(List<IEvent> events) {
    List<DayOfWeek> days = new ArrayList<>();
    for (IEvent e: events) {
      days.add(e.getStartDay());
    }
    return days;
  }

  /**
   * The method that goes through the events and puts them into string form.
   * @param events the passed in list of events
   * @param daysInEvents the days that are in the list of events
   * @return the string of all items in each event
   */
  private String days(List<IEvent> events, List<DayOfWeek> daysInEvents) {
    StringBuilder days = new StringBuilder();
    List<Day> daysOfWeek = new ArrayList<>(Arrays.asList(Day.SUNDAY,
            Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY,
            Day.FRIDAY, Day.SATURDAY));
    for (Day d: daysOfWeek) {
      if (!daysInEvents.contains(d)) {
        days.append(d.toString()).append(":\n");
      }
      else {
        List<IEvent> containsDay = this.containsDay(events, d);
        days.append(d.toString()).append(":\n");
        for (IEvent e: containsDay) {
          days.append("\tname: ").append(e.getName()).append("\n");
          days.append("\ttime: ").append(d).append(": ").append(
                  e.getStartTime().getHour()).append(
                  e.getStartTime().getMinute()).append(" -> ").append(
                  e.getEndDay().toString()).append(
                  ": ").append(e.getEndTime().getHour()).append(
                  e.getEndTime().getMinute()).append("\n");
          days.append("\tlocation: ").append(e.getPlace()).append("\n");
          days.append("\tonline: ").append(e.isOnline()).append("\n");
          days.append("\tinvitees: ").append(e.getHostUser().getUid()).append(
                  "\n").append(e.invites());
        }
      }
    }
    return days.toString();
  }

  /**
   * The method that sees if the list of events contains a certain day and makes a
   * list of the ones that do.
   * @param events the list of events to go through
   * @param d the day to compare it to
   * @return the list of events that has the passed in day in it
   */
  private List<IEvent> containsDay(List<IEvent> events, Day d) {
    List<IEvent> containsDay = new ArrayList<>();
    for (IEvent e: events) {
      if (e.getStartDay().equals(d)) {
        containsDay.add(e);
      }
    }
    return containsDay;
  }
}