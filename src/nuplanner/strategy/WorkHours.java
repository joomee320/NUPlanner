package nuplanner.strategy;

import java.time.LocalTime;
import java.util.List;

import nuplanner.model.Day;
import nuplanner.model.Event;
import nuplanner.model.IEvent;
import nuplanner.model.IUser;
import nuplanner.model.MutableSystems;
import nuplanner.model.User;

/**
 * Creates the class for scheduling an event on the first possible time from Monday to Friday
 * (inclusive) between the hours of 0900 and 1700 (inclusive) where all invitees and the host can
 * attend the even and return an event with that block of time.
 * Note this means it is impossible to schedule an event that goes to next week.
 */
public class WorkHours implements Strategy {
  private MutableSystems model;

  public WorkHours(MutableSystems model) {
    this.model = model;
  }

  @Override
  public IEvent createEventWithDuration(String eventName, boolean isOnline, String location,
                                        String duration, String selectedUser, List<IUser> users) {
    int duration1 = Integer.parseInt(duration);
    int hour = 9;
    int minute = 0;
    Day startDay = Day.MONDAY;
    Day endDay = Day.MONDAY;
    LocalTime endTime = LocalTime.of((duration1 / 60) + 9, duration1 % 60);
    Event possible = new Event(eventName, startDay, LocalTime.of(hour, minute),
            endDay, endTime, isOnline, location, new User(selectedUser), users);
    while (this.model.isEventConflicting(possible)) {
      if (duration1 >= 800 && duration1 <= 1600) {
        endDay = Day.TUESDAY;
      }
      else if (duration1 >= 1600 && duration1 <= 2400) {
        endDay = Day.WEDNESDAY;
      }
      else if (duration1 >= 2400 && duration1 <= 3200) {
        endDay = Day.THURSDAY;
      }
      else if (duration1 >= 3200 && duration1 <= 4000) {
        endDay = Day.FRIDAY;
      }
      else {
        return null;
      }
      if (endTime.getHour() > 17) {
        endTime = LocalTime.of(9, endTime.getMinute());
      }
      while (minute < 60 && hour < 18) {
        possible = new Event(eventName, startDay, LocalTime.of(hour, minute),
                endDay, endTime, isOnline, location, new User(selectedUser), users);
        minute++;
      }
      minute = 0;
      hour++;
      startDay = endDay;
    }
    return possible;
  }
}
