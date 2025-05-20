package nuplanner.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The Schedule is the class that holds the list of all the events and the user that
 * has all the events. The schedule is identified as unique user ID since each user owns
 * only one schedule.
 */
public class Schedule implements ISchedule {
  private final String uid;
  private final List<IEvent> events;

  /**
   * This is the constructor for a schedule.
   */
  public Schedule(String uid) {
    this.uid = uid;
    this.events = new ArrayList<>();
  }

  /**
   * Creates a schedule where you can initialize the list of events.
   * @param uid the string user id
   * @param events the list of events
   */
  public Schedule(String uid, List<IEvent> events) {
    this.uid = Objects.requireNonNull(uid);
    this.events = Objects.requireNonNull(events);
    for (IEvent e: this.events) {
      if (this.isOverlapping(e)) {
        throw new IllegalArgumentException("Not a valid schedule");
      }
    }
  }

  @Override
  public void addEvent(IEvent event) {
    if (!this.isOverlapping(event)) {
      this.events.add(event);
    }
    else {
      throw new IllegalArgumentException("The events are overlapping.");
    }
  }

  @Override
  public void modifyEvent(IEvent event, IEvent newEventDetails) {
    for (int i = 0; i < this.events.size(); i++) {
      IEvent events = this.events.get(i);
      if (events.equals(event) && !this.isOverlapping(newEventDetails, events)
              && events.getHostUser().equals(newEventDetails.getHostUser())) {
        this.events.set(i, newEventDetails);
      }
      else if (this.isOverlapping(newEventDetails, events)
              || !events.getHostUser().equals(newEventDetails.getHostUser())) {
        throw new IllegalArgumentException("The event is overlapping or cannot change the host.");
      }
    }
  }

  @Override
  public void removeEvent(IEvent event) {
    this.events.removeIf(events -> events.equals(event));
  }

  @Override
  public void merge(IEvent event) {
    this.addEvent(event);
  }

  @Override
  public boolean isOverlapping(IEvent newEvent, IEvent... exclude) {
    for (IEvent existingEvent : this.events) {
      if (exclude.length > 0 && existingEvent.equals(exclude[0])) {
        continue;
      }
      boolean sameOrOverlappingDays = newEvent.getStartDay().num()
              <= existingEvent.getEndDay().num()
              && newEvent.getEndDay().num() >= existingEvent.getStartDay().num();
      if (!sameOrOverlappingDays) {
        continue;
      }
      int newEventStartDayOrdinal = newEvent.getStartDay().num();
      int newEventEndDayOrdinal = newEvent.getEndDay().num();
      int existingEventStartDayOrdinal = existingEvent.getStartDay().num();
      int existingEventEndDayOrdinal = existingEvent.getEndDay().num();
      if (newEventEndDayOrdinal < newEventStartDayOrdinal) {
        newEventEndDayOrdinal += 7;
      }
      if (existingEventEndDayOrdinal < existingEventStartDayOrdinal) {
        existingEventEndDayOrdinal += 7;
      }
      if ((newEvent.getEndTime().isAfter(existingEvent.getStartTime())
              || newEvent.getEndTime().equals(existingEvent.getStartTime())) &&
              (newEvent.getStartTime().isBefore(existingEvent.getEndTime())
                      || newEvent.getStartTime().equals(existingEvent.getEndTime()))) {
        return true;
      }
    }
    return false;
  }

  @Override
  public List<IEvent> getAllEvents() {
    return new ArrayList<>(events);
  }

  @Override
  public Document toDocument() {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();
      Element rootElement = doc.createElement("schedule");
      rootElement.setAttribute("id", this.uid);
      doc.appendChild(rootElement);
      for (IEvent event : this.getAllEvents()) {
        convertEventContentToFile(event, doc, rootElement);
      }
      return doc;
    } catch (Exception e) {
      throw new RuntimeException("Error creating document from schedule", e);
    }
  }

  /**
   * This method converts the contents of an event to a file.
   * @param event The passed in event
   * @param doc The doc to convert it to
   * @param rootElement what the event is added to
   */
  private static void convertEventContentToFile(IEvent event, Document doc, Element rootElement) {
    Element eventElement = doc.createElement("event");
    rootElement.appendChild(eventElement);
    Element name = doc.createElement("name");
    name.appendChild(doc.createTextNode(event.getName()));
    eventElement.appendChild(name);
    Element time = doc.createElement("time");
    eventElement.appendChild(time);
    Element startDay = doc.createElement("start-day");
    startDay.appendChild(doc.createTextNode(event.getStartDay().toString()));
    time.appendChild(startDay);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
    Element start = doc.createElement("start");
    start.appendChild(doc.createTextNode(event.getStartTime().format(formatter)));
    time.appendChild(start);
    Element endDay = doc.createElement("end-day");
    endDay.appendChild(doc.createTextNode(event.getEndDay().toString()));
    time.appendChild(endDay);
    Element end = doc.createElement("end");
    end.appendChild(doc.createTextNode(event.getEndTime().format(formatter)));
    time.appendChild(end);
    Element location = doc.createElement("location");
    eventElement.appendChild(location);
    Element online = doc.createElement("online");
    online.appendChild(doc.createTextNode(String.valueOf(event.isOnline())));
    location.appendChild(online);
    Element place = doc.createElement("place");
    place.appendChild(doc.createTextNode(event.getPlace()));
    location.appendChild(place);
    Element users = doc.createElement("users");
    eventElement.appendChild(users);
    Element hostUid = doc.createElement("uid");
    hostUid.appendChild(doc.createTextNode(event.getHostUser().getUid()));
    users.appendChild(hostUid);
    for (IUser user : event.getInvitedUsers()) {
      if (!user.getUid().equals(event.getHostUser().getUid())) {
        Element uidElement = doc.createElement("uid");
        uidElement.appendChild(doc.createTextNode(user.getUid()));
        users.appendChild(uidElement);
      }
    }
  }
}