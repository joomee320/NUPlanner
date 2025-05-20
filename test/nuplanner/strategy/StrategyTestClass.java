package nuplanner.strategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;

import nuplanner.controller.NUPlannerController;
import nuplanner.model.CentralSystem;
import nuplanner.model.Day;
import nuplanner.model.IEvent;
import nuplanner.model.MutableSystems;
import nuplanner.model.User;
import nuplanner.view.ViewMock;

/**
 * This class is designed to test the functionality of two specific scheduling strategies:
 * "Any Time" and "Work Hours." The class sets up a testing environment with a CentralSystem model
 * and mock view, alongside initializing the strategies for testing.
 */
public class StrategyTestClass {
  protected NUPlannerController controller;
  protected MutableSystems model;
  protected Strategy workHours;
  protected Strategy anyTime;

  @Before
  public void init() {
    this.model = new CentralSystem();
    this.workHours = new WorkHours(this.model);
    this.anyTime = new AnyTime(this.model);
  }

  @Test
  public void testAnyTime() {
    this.controller = new NUPlannerController(new ViewMock(new StringBuilder()),
            this.model, "Any time");
    IEvent created = this.anyTime.createEventWithDuration("A", true, "home",
            "100", "Alex", new ArrayList<>());
    this.controller.onCreateEvent("Alex", created);
    Assert.assertEquals("A", created.getName());
    Assert.assertTrue(created.isOnline());
    Assert.assertEquals("home", created.getPlace());
    Assert.assertEquals(Day.SUNDAY, created.getStartDay());
    Assert.assertEquals(LocalTime.of(0, 0), created.getStartTime());
    Assert.assertEquals(Day.SUNDAY, created.getEndDay());
    Assert.assertEquals(LocalTime.of(1, 40), created.getEndTime());
    Assert.assertEquals("Alex", created.getHostUser().getUid());
    Assert.assertTrue(created.getInvitedUsers().isEmpty());
    this.model.addUser(new User("Alex"));
    Assert.assertTrue(this.model.getUserSchedule("Alex").getAllEvents().contains(created));
  }

  @Test
  public void testWorkHours() {
    this.controller = new NUPlannerController(new ViewMock(new StringBuilder()),
            this.model, "Work hours");
    IEvent created = this.workHours.createEventWithDuration("A", true, "home",
            "100", "Alex", new ArrayList<>());
    this.controller.onCreateEvent("Alex", created);
    Assert.assertEquals("A", created.getName());
    Assert.assertTrue(created.isOnline());
    Assert.assertEquals("home", created.getPlace());
    Assert.assertEquals(Day.MONDAY, created.getStartDay());
    Assert.assertEquals(LocalTime.of(9, 0), created.getStartTime());
    Assert.assertEquals(Day.MONDAY, created.getEndDay());
    Assert.assertEquals(LocalTime.of(10, 40), created.getEndTime());
    Assert.assertEquals("Alex", created.getHostUser().getUid());
    Assert.assertTrue(created.getInvitedUsers().isEmpty());
    Assert.assertTrue(this.model.getUserSchedule("Alex").getAllEvents().contains(created));
  }
}
