package nuplanner.model;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import nuplanner.utils.XMLParser;

/**
 * The Central System is the model for the NUPlanner. The model keeps track of all the users
 * and their user ID, and it is represented by HashMap. The model changes the events and the
 * schedule for the user.
 * The model also makes sure when changing events that it is consistent for the invited
 * users as well.
 */
public class CentralSystem implements MutableSystems {
  private final Map<String, IUser> users;

  /**
   * This creates a NUPlanner system that does not have any existing user.
   */
  public CentralSystem() {
    this.users = new HashMap<>();
  }

  /**
   * This creates a NUPlanner system with users that are being given.
   * @param users the list of users
   */
  public CentralSystem(Map<String, IUser> users) {
    this.users = Objects.requireNonNull(users);
  }

  @Override
  public void uploadXMLFileToSystem(String filePath) {
    XMLParser.readScheduleFromFile(this, filePath, this.users);
  }

  @Override
  public void saveXMLFileFromSystem(String directoryPath) {
    for (Map.Entry<String, IUser> entry : this.users.entrySet()) {
      String userId = entry.getKey();
      IUser user = entry.getValue();
      String filePath = directoryPath + "/" + userId + "_schedule.xml";
      Document scheduleDoc = user.getSchedule().toDocument();
      XMLParser.writeDocumentToFile(filePath, scheduleDoc);
    }
  }

  @Override
  public Map<String, IUser> getAllUsers() {
    return this.users;
  }

  @Override
  public IUser getUser(String uid) {
    return this.users.get(uid);
  }

  @Override
  public ISchedule getUserSchedule(String uid) {
    IUser user = users.get(uid);
    if (user != null) {
      return user.getSchedule();
    }
    throw new IllegalArgumentException("Cannot get the schedule of a null user");
  }

  @Override
  public boolean isEventConflicting(IEvent event) {
    for (IUser invitedUser : event.getInvitedUsers()) {
      ISchedule userSchedule = invitedUser.getSchedule();
      if (userSchedule.isOverlapping(event)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void addUser(IUser user) {
    if (!this.users.containsValue(user)) {
      this.users.put(user.getUid(), user);
    }
    else {
      throw new IllegalArgumentException("Already has this user in it.");
    }
  }

  @Override
  public void removeUser(IUser user) {
    this.users.remove(user.getUid(), user);
  }


  @Override
  public void createEvent(String uid, IEvent event) {
    IUser user = users.get(uid);
    if (user == null) {
      this.addUser(new User(uid));
      user = users.get(uid);
    }
    user.addEventToSchedule(event);
    for (IUser invitedUser : event.getInvitedUsers()) {
      if (users.containsValue(invitedUser)) {
        invitedUser.addEventToSchedule(event);
      }
      else {
        this.users.put(invitedUser.getUid(), invitedUser);
        invitedUser.addEventToSchedule(event);
      }
    }
  }

  @Override
  public void modifyEvent(String uid, IEvent event, IEvent modifiedEvent) {
    IUser user = users.get(uid);
    if (user == null) {
      this.addUser(new User(uid));
      user = users.get(uid);
    }
    user.modifyEvent(event, modifiedEvent);
    for (IUser invitedUser : modifiedEvent.getInvitedUsers()) {
      if (users.containsValue(invitedUser)) {
        invitedUser.modifyEvent(event, modifiedEvent);
      }
      else {
        this.users.put(invitedUser.getUid(), invitedUser);
        invitedUser.modifyEvent(event, modifiedEvent);
      }
    }
  }

  @Override
  public void deleteEvent(IEvent event) {
    for (IUser user : this.users.values()) {
      user.removeEventFromSchedule(event);
    }
  }
}