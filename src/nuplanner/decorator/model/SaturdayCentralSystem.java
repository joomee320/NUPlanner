package nuplanner.decorator.model;

import java.util.Map;

import nuplanner.model.CentralSystem;
import nuplanner.model.IEvent;
import nuplanner.model.ISchedule;
import nuplanner.model.IUser;
import nuplanner.model.MutableSystems;

/**
 * The class for the central system starting on Saturday instead of on Sunday.
 */
public class SaturdayCentralSystem implements MutableSystems {
  private final CentralSystem delegate;

  public SaturdayCentralSystem(CentralSystem delegate) {
    this.delegate = delegate;
  }

  @Override
  public void uploadXMLFileToSystem(String filePath) {
    this.delegate.uploadXMLFileToSystem(filePath);
  }

  @Override
  public void saveXMLFileFromSystem(String filePath) {
    this.delegate.saveXMLFileFromSystem(filePath);
  }

  @Override
  public void addUser(IUser user) {
    this.delegate.addUser(user);
  }

  @Override
  public void removeUser(IUser user) {
    this.delegate.removeUser(user);
  }

  @Override
  public void createEvent(String uid, IEvent event) {
    this.delegate.createEvent(uid, event);
  }

  @Override
  public void modifyEvent(String uid, IEvent event, IEvent modifiedEvent) {
    this.delegate.modifyEvent(uid, event, modifiedEvent);
  }

  @Override
  public void deleteEvent(IEvent event) {
    this.delegate.deleteEvent(event);
  }

  @Override
  public Map<String, IUser> getAllUsers() {
    return this.delegate.getAllUsers();
  }

  @Override
  public IUser getUser(String uid) {
    return this.delegate.getUser(uid);
  }

  @Override
  public ISchedule getUserSchedule(String uid) {
    return this.delegate.getUserSchedule(uid);
  }

  @Override
  public boolean isEventConflicting(IEvent event) {
    return this.delegate.isEventConflicting(event);
  }
}
