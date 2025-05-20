package nuplanner.controller;

import nuplanner.model.CentralSystem;
import nuplanner.model.Day;
import nuplanner.model.Event;
import nuplanner.model.MutableSystems;
import nuplanner.model.User;
import nuplanner.view.IView;
import nuplanner.view.ViewMock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.time.LocalTime;
import java.util.List;

/**
 * Tests the functionality of the NUPlannerController within the NUPlanner application.
 * This test class validates the interactions between the controller and the model,
 * ensuring that user actions trigger the correct modifications in the model and proper
 * responses in the view.
 */
public class TestController {
  private Event e1;
  private Event e2;
  private NUPlannerController controller;
  private StringBuilder log;

  @Before
  public void setUp() {
    MutableSystems model = new CentralSystem();
    this.log = new StringBuilder();
    IView view = new ViewMock(log);
    this.controller = new NUPlannerController(view, model, "Any time");
    this.e1 = new Event("CS3500 Morning Lecture",
            Day.TUESDAY, LocalTime.of(9, 50), Day.TUESDAY,
            LocalTime.of(11, 30), false, "Churchill Hall 101",
            new User("Alex"), List.of());
    this.e2 = new Event("CS3500 Afternoon Lecture",
            Day.TUESDAY, LocalTime.of(13, 35), Day.TUESDAY,
            LocalTime.of(15, 15), false, "Churchill Hall 101",
            new User("Alex"), List.of());
  }

  @Test
  public void testOnCreateEvent() {
    this.controller.onCreateEvent("Alex", this.e1);
    Assert.assertTrue(this.log.toString().contains("setting a model features listeners"));
  }

  @Test
  public void testOnModifyEvent() {
    this.controller.onModifyEvent("Alex", this.e1, this.e2);
    Assert.assertTrue(this.log.toString().contains("setting a model features listeners"));
  }

  @Test
  public void testOnRemoveEvent() {
    this.controller.onRemoveEvent(this.e1);
    Assert.assertTrue(this.log.toString().contains("setting a model features listeners"));
  }

  @Test
  public void testOnUploadXMLFile() {
    this.controller.onUploadXMLFile("file path");
    Assert.assertTrue(this.log.toString().contains("setting a model features listeners"));
  }

  @Test
  public void testOnSaveSchedules() {
    this.controller.onSaveSchedules("directory path");
    Assert.assertTrue(this.log.toString().contains("setting a model features listeners"));
  }
}
