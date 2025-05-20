package nuplanner.decorator.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import nuplanner.model.IEvent;
import nuplanner.model.ISchedule;
import nuplanner.model.IUser;
import nuplanner.model.Schedule;
import nuplanner.model.User;

/**
 * The class that creates the User based off of the new Saturday schedule.
 */
public class SaturdayUser implements IUser {
  private final User delegate;
  private final String uid;
  private ISchedule schedule;

  /**
   * The constructor for the Saturday User with a delegate and the user ID.
   * @param delegate delegate of the User
   * @param uid string user ID
   */
  public SaturdayUser(User delegate, String uid) {
    this.delegate = delegate;
    this.uid = uid;
    this.schedule = new Schedule(this.uid);
  }

  /**
   * The second constructor of the user.
   * @param delegate the User delegate
   * @param uid the string user ID
   * @param schedule the passed in schedule
   */
  public SaturdayUser(User delegate, String uid, ISchedule schedule) {
    this.delegate = delegate;
    this.uid = uid;
    this.schedule = schedule;
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
    SaturdayDay startDay = null;
    LocalTime startTime = null;
    SaturdayDay endDay = null;
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
    return new SaturdayEvent(name, startDay, startTime, endDay, endTime,
            online, place, user, invitedUsers);
  }

  /**
   * Converts the given string to a day.
   * @param day the string of the day
   * @return the Day enum
   */
  private SaturdayDay convert(String day) {
    switch (day) {
      case "Sunday":
        return SaturdayDay.SUNDAY;
      case "Monday":
        return SaturdayDay.MONDAY;
      case "Tuesday":
        return SaturdayDay.TUESDAY;
      case "Wednesday":
        return SaturdayDay.WEDNESDAY;
      case "Thursday":
        return SaturdayDay.THURSDAY;
      case "Friday":
        return SaturdayDay.FRIDAY;
      case "Saturday":
        return SaturdayDay.SATURDAY;
      default:
        throw new IllegalStateException("Not a valid day: " + day);
    }
  }

  @Override
  public void setSchedule(ISchedule s) {
    this.delegate.setSchedule(s);
  }

  @Override
  public String getUid() {
    return this.delegate.getUid();
  }

  @Override
  public ISchedule getSchedule() {
    return this.delegate.getSchedule();
  }

  @Override
  public void addEventToSchedule(IEvent event) {
    this.delegate.addEventToSchedule(event);
  }

  @Override
  public void removeEventFromSchedule(IEvent event) {
    this.delegate.removeEventFromSchedule(event);
  }

  @Override
  public void modifyEvent(IEvent originalEvent, IEvent newEventDetails) {
    this.delegate.modifyEvent(originalEvent, newEventDetails);
  }
}
