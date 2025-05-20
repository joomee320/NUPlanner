package nuplanner.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Objects;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Each user has a schedule of their own. A user can always add, modify, or remove events
 * from their schedule. All edits to the event are then made visible to all invitees. This
 * behavior of a user is allowed since our system is based on trust, which also allows users to
 * modify events that they aren't hosting. Since each user owns one schedule, the schedule of
 * a user is created whenever the user is being defined.
 */
public class User implements IUser {
  private final String uid;
  private ISchedule schedule;

  /**
   * This is the constructor for the user.
   * All users' names should be unique for ease.
   */
  public User(String uid) {
    this.uid = uid;
    this.schedule = new Schedule(uid);
  }

  /**
   * A constructor for a user with a string id and a given schedule.
   * @param uid the string user id
   * @param schedule the passed in schedule for the user
   */
  public User(String uid, ISchedule schedule) {
    this.uid = Objects.requireNonNull(uid);
    this.schedule = Objects.requireNonNull(schedule);
  }

  @Override
  public ISchedule mergeSchedule(NodeList eventList) {
    for (int n = 0; n < eventList.getLength(); n++) {
      Node node = eventList.item(n);
      this.schedule.merge(this.toEvent(node));
    }
    return this.schedule;
  }

  /**
   * This method converts the node to an event.
   * @param node the node to be converted
   * @return the event returned
   */
  private IEvent toEvent(Node node) {
    String name = "";
    Day startDay = null;
    LocalTime startTime = null;
    Day endDay = null;
    LocalTime endTime = null;
    boolean online = false;
    String place = "";
    IUser user = this;
    List<IUser> invitedUsers = new ArrayList<>();
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      Element elem = (Element) node;
      if (!elem.getTextContent().trim().isEmpty()) {
        name = elem.getElementsByTagName("name").item(0).getTextContent();
        NodeList time = elem.getElementsByTagName("time");
        Element timeElem = (Element) time.item(0);
        startDay = this.convert(timeElem.getElementsByTagName("start-day").
                item(0).getTextContent());
        startTime = LocalTime.parse(timeElem.getElementsByTagName("start").item(0).getTextContent(),
                DateTimeFormatter.ofPattern("HHmm"));
        endDay = this.convert(timeElem.getElementsByTagName("end-day").item(0).getTextContent());
        endTime = LocalTime.parse(timeElem.getElementsByTagName("end").item(0).getTextContent(),
                DateTimeFormatter.ofPattern("HHmm"));
        Element locationElement = (Element) elem.getElementsByTagName("location").
                item(0);
        online = Boolean.parseBoolean(locationElement.getElementsByTagName("online").
                item(0).getTextContent());
        place = locationElement.getElementsByTagName("place").item(0).
                getTextContent();
        Element usersElement = (Element) elem.getElementsByTagName("users").
                item(0);
        NodeList uids = usersElement.getElementsByTagName("uid");
        invitedUsers = new ArrayList<>();
        for (int i = 1; i < uids.getLength(); i++) {
          String invitedUserId = uids.item(i).getTextContent();
          invitedUsers.add(new User(invitedUserId));
        }
      }
    }
    return new Event(name, startDay, startTime, endDay, endTime,
            online, place, user, invitedUsers);
  }

  /**
   * Converts the given string to a day.
   * @param day the string of the day
   * @return the Day enum
   */
  private Day convert(String day) {
    switch (day) {
      case "Sunday":
        return Day.SUNDAY;
      case "Monday":
        return Day.MONDAY;
      case "Tuesday":
        return Day.TUESDAY;
      case "Wednesday":
        return Day.WEDNESDAY;
      case "Thursday":
        return Day.THURSDAY;
      case "Friday":
        return Day.FRIDAY;
      case "Saturday":
        return Day.SATURDAY;
      default:
        throw new IllegalStateException("Not a valid day: " + day);
    }
  }

  @Override
  public void setSchedule(ISchedule s) {
    this.schedule = new Schedule(this.getUid(), s.getAllEvents());
  }

  @Override
  public String getUid() {
    return this.uid;
  }

  @Override
  public ISchedule getSchedule() {
    return this.schedule;
  }

  @Override
  public void addEventToSchedule(IEvent event) {
    this.schedule.addEvent(event);
  }

  @Override
  public void removeEventFromSchedule(IEvent event) {
    this.schedule.removeEvent(event);
  }

  @Override
  public void modifyEvent(IEvent originalEvent, IEvent newEventDetails) {
    this.schedule.modifyEvent(originalEvent, newEventDetails);
  }
}