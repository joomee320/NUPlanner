package nuplanner.view;

import java.time.LocalTime;
import java.util.List;

import nuplanner.controller.Features;
import nuplanner.model.Day;
import nuplanner.model.Event;
import nuplanner.model.User;

/**
 * The mock implementation of the view for testing the controller.
 */
public class ViewMock implements IView {
  private final StringBuilder log;


  /**
   * Constructor with a log and a mutable model to create an instance of the mock view.
   */
  public ViewMock(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void makeVisible() {
    // no necessary to implement for the mock
  }

  @Override
  public void showErrorMessage(String error) {
    log.append(" New message: \"").append(error);
  }

  @Override
  public void refresh() {
    // no necessary to implement for the mock
  }

  @Override
  public void setListener(Features plannerController) {
    this.log.append("setting a model features listeners\n");
    plannerController.onSaveSchedules("mock directory");
    plannerController.onUploadXMLFile("mock filepath");
    plannerController.onCreateEvent("mock userid", new Event("mock event name",
            Day.TUESDAY, LocalTime.of(0, 0), Day.TUESDAY,
            LocalTime.of(1, 1), false, "mock location",
            new User("mock user"), List.of()));
    plannerController.onRemoveEvent(new Event("mock event name",
            Day.TUESDAY, LocalTime.of(0, 0), Day.TUESDAY,
            LocalTime.of(1, 1), false, "mock location",
            new User("mock user"), List.of()));
    plannerController.onModifyEvent("mock user", new Event("mock event name",
            Day.TUESDAY, LocalTime.of(0, 0), Day.TUESDAY,
            LocalTime.of(1, 1), false, "mock location",
            new User("mock user"), List.of()), new Event("mock event name",
            Day.TUESDAY, LocalTime.of(0, 0), Day.TUESDAY,
            LocalTime.of(1, 1), false, "mock location",
            new User("mock user"), List.of()));
    plannerController.onCreateEventFrame();
    plannerController.onSwitchUser("mock user");
    plannerController.handleClick(new Event("mock event name",
            Day.TUESDAY, LocalTime.of(0, 0), Day.TUESDAY,
            LocalTime.of(1, 1), false, "mock location",
            new User("mock user"), List.of()));
  }
}
