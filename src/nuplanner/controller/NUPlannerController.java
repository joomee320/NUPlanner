package nuplanner.controller;

import nuplanner.model.IEvent;
import nuplanner.strategy.AnyTime;
import nuplanner.strategy.Strategy;
import nuplanner.strategy.WorkHours;
import nuplanner.model.MutableSystems;
import nuplanner.view.IView;
import nuplanner.view.EventFrame;

/**
 * Creates the class for the controller of the planner.
 * Has not been implemented yet.
 */
public class NUPlannerController implements Features {
  private final MutableSystems model;
  private final IView view;
  private final String strat;

  /**
   * The constructor for the controller.
   * @param view the view it takes in
   * @param model the model it updates
   */
  public NUPlannerController(IView view, MutableSystems model, String strat) {
    this.view = view;
    this.model = model;
    this.strat = strat;
    this.view.setListener(this);
  }

  @Override
  public void onUploadXMLFile(String filePath) {
    try {
      this.model.uploadXMLFileToSystem(filePath);
      this.view.refresh();
    } catch (Exception e) {
      this.view.showErrorMessage("Error uploading XML file: " + e.getMessage());
    }
  }

  @Override
  public void onSaveSchedules(String directoryPath) {
    try {
      this.model.saveXMLFileFromSystem(directoryPath);
      this.view.refresh();
    } catch (Exception e) {
      this.view.showErrorMessage("Error saving XML file: " + e.getMessage());
    }
  }

  @Override
  public void onCreateEvent(String uid, IEvent event) {
    try {
      this.model.createEvent(uid, event);
      this.view.refresh();
    } catch (IllegalArgumentException e) {
      this.view.showErrorMessage("Failed to create event: " + e.getMessage());
    }
  }

  @Override
  public void onModifyEvent(String uid, IEvent originalEvent, IEvent newEvent) {
    try {
      this.model.modifyEvent(uid, originalEvent, newEvent);
      this.view.refresh();
    } catch (Exception e) {
      this.view.showErrorMessage("Failed to modify event: " + e.getMessage());
    }
  }

  @Override
  public void onRemoveEvent(IEvent event) {
    try {
      this.model.deleteEvent(event);
      this.view.refresh();
    } catch (Exception e) {
      this.view.showErrorMessage("Failed to remove event: " + e.getMessage());
    }
  }

  @Override
  public Strategy onCreateEventFrame() {
    if (this.strat.equals("Any time")) {
      return new AnyTime(this.model);
    }
    else if (this.strat.equals("Work hours")) {
      return new WorkHours(this.model);
    }
    throw new IllegalArgumentException("Not a valid strategy.");
  }

  @Override
  public void onSwitchUser(String userId) {
    this.view.refresh();
  }

  @Override
  public void handleClick(IEvent e) {
    EventFrame ef = new EventFrame(this.model, false, e.getHostUser().getUid());
    ef.setEverything(e);
  }
}
