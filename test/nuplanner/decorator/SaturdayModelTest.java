package nuplanner.decorator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nuplanner.decorator.model.SaturdayCentralSystem;
import nuplanner.decorator.model.SaturdayDay;
import nuplanner.decorator.model.SaturdayEvent;
import nuplanner.decorator.model.SaturdayUser;
import nuplanner.model.Schedule;
import nuplanner.view.ScheduleTextView;
import nuplanner.model.MutableSystems;
import nuplanner.model.IUser;
import nuplanner.model.IEvent;
import nuplanner.model.CentralSystem;
import nuplanner.model.User;

import static org.junit.Assert.assertEquals;

/**
 * The tests for the saturday central system.
 */
public class SaturdayModelTest {
  MutableSystems saturdayCentralSystem;
  IUser user1;
  IUser user2;
  IUser user3;
  IEvent morningLecture;
  IEvent afternoonLecture;
  IEvent sleep;
  IEvent overlap;
  IEvent party;
  String filePath1;
  String filePath2;

  @Before
  public void init() {
    this.saturdayCentralSystem = new SaturdayCentralSystem(new CentralSystem());
    this.user1 = new SaturdayUser(new User("Alex"), "Alex");
    this.user2 = new SaturdayUser(new User("Joomee"), "Joomee");
    this.user3 = new SaturdayUser(new User("Prof. Lucia"), "Prof. Lucia");
    this.morningLecture = new SaturdayEvent("CS3500 Morning Lecture",
            SaturdayDay.TUESDAY, LocalTime.of(9, 50), SaturdayDay.TUESDAY,
            LocalTime.of(11, 30), false, "Churchill Hall 101",
            this.user1, List.of(this.user2, this.user3));
    this.afternoonLecture = new SaturdayEvent("CS3500 Afternoon Lecture",
            SaturdayDay.TUESDAY, LocalTime.of(13, 35), SaturdayDay.TUESDAY,
            LocalTime.of(15, 50), false, "Churchill Hall 101",
            this.user3, List.of(this.user1));
    this.sleep = new SaturdayEvent("Sleep", SaturdayDay.FRIDAY, LocalTime.of(20, 0),
            SaturdayDay.SUNDAY,
            LocalTime.of(9, 30), false, "Home", this.user1, List.of());
    this.overlap = new SaturdayEvent("Sleep", SaturdayDay.FRIDAY, LocalTime.of(20, 0),
            SaturdayDay.SUNDAY,
            LocalTime.of(8, 30), false, "Home", this.user1, List.of());
    this.party = new SaturdayEvent("Party", SaturdayDay.THURSDAY, LocalTime.of(21, 30),
            SaturdayDay.THURSDAY,
            LocalTime.of(23, 59), false, "Home", this.user3, List.of());
    filePath1 = "C:\\Users\\alexw\\OneDrive\\Desktop\\CS3500\\Homework\\HW9\\src\\prof.xml";
    filePath2 = "C:\\Users\\alexw\\OneDrive\\Desktop";
  }

  @Test
  public void testUserCreation() {
    assertEquals(0, this.saturdayCentralSystem.getAllUsers().size());
  }

  @Test
  public void testGetAllUsers() {
    this.saturdayCentralSystem.addUser(this.user1);
    this.saturdayCentralSystem.addUser(this.user2);
    this.saturdayCentralSystem.addUser(this.user3);
    Map<String, IUser> users = new HashMap<>();
    users.put("Alex", this.user1);
    users.put("Joomee", this.user2);
    users.put("Prof. Lucia", this.user3);
    assertEquals(users, this.saturdayCentralSystem.getAllUsers());
  }

  @Test
  public void testGetUsers() {
    this.saturdayCentralSystem.addUser(this.user1);
    this.saturdayCentralSystem.addUser(this.user2);
    this.saturdayCentralSystem.addUser(this.user3);
    Map<String, IUser> users = new HashMap<>();
    users.put("Alex", this.user1);
    users.put("Joomee", this.user2);
    users.put("Prof. Lucia", this.user3);
    Assert.assertEquals(user1, this.saturdayCentralSystem.getUser("Alex"));
  }



  @Test
  public void testAddUser() {
    this.saturdayCentralSystem.addUser(this.user1);
    assertEquals(1, this.saturdayCentralSystem.getAllUsers().size());
    this.saturdayCentralSystem.addUser(this.user2);
    assertEquals(2, this.saturdayCentralSystem.getAllUsers().size());
    try {
      this.saturdayCentralSystem.addUser(this.user1);
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testRemoveUser() {
    this.saturdayCentralSystem.addUser(this.user1);
    this.saturdayCentralSystem.addUser(this.user2);
    this.saturdayCentralSystem.addUser(this.user3);
    this.saturdayCentralSystem.removeUser(this.user3);
    assertEquals(2, this.saturdayCentralSystem.getAllUsers().size());
  }

  @Test
  public void testGetUserSchedule() {
    this.saturdayCentralSystem.addUser(this.user1);
    this.saturdayCentralSystem.createEvent("Alex", this.morningLecture);
    Schedule sc1 = new Schedule("Alex");
    sc1.addEvent(this.morningLecture);
    Schedule sc2 = new Schedule("Joomee");
    sc2.addEvent(this.morningLecture);
    Schedule sc3 = new Schedule("Prof. Lucia");
    sc3.addEvent(this.morningLecture);
    assertEquals(sc1.getAllEvents(), this.saturdayCentralSystem.getUserSchedule("Alex")
            .getAllEvents());
    assertEquals(sc2.getAllEvents(), this.saturdayCentralSystem.getUserSchedule("Joomee")
            .getAllEvents());
    assertEquals(sc3.getAllEvents(), this.saturdayCentralSystem.getUserSchedule("Prof. Lucia")
            .getAllEvents());
  }

  @Test
  public void testCreateEvent() {
    this.saturdayCentralSystem.addUser(this.user1);
    this.saturdayCentralSystem.createEvent("Alex", this.morningLecture);
    this.saturdayCentralSystem.createEvent("Prof. Lucia", this.afternoonLecture);
    assertEquals(2, this.saturdayCentralSystem
            .getUserSchedule("Prof. Lucia").getAllEvents().size());
    assertEquals(2, this.saturdayCentralSystem
            .getUserSchedule("Alex").getAllEvents().size());
    assertEquals(1, this.saturdayCentralSystem
            .getUserSchedule("Joomee").getAllEvents().size());
    try {
      this.saturdayCentralSystem.createEvent("Prof. Lucia", this.morningLecture);
      Assert.fail("Should not be any overlap when adding events.");
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testModifyEvent() {
    this.saturdayCentralSystem.addUser(this.user1);
    this.saturdayCentralSystem.createEvent("Alex", this.morningLecture);
    try {
      this.saturdayCentralSystem.modifyEvent("Alex", this.morningLecture, this.afternoonLecture);
      Assert.fail("Cannot change the host of the event.");
    } catch (IllegalArgumentException ignored) {
    }
    assertEquals(1, this.saturdayCentralSystem.getUserSchedule("Alex").getAllEvents().size());
    this.saturdayCentralSystem.modifyEvent("Alex", this.morningLecture, this.sleep);
    assertEquals(this.sleep, this.saturdayCentralSystem.getUserSchedule(
            "Alex").getAllEvents().get(0));
    assertEquals(1, this.saturdayCentralSystem.getUserSchedule("Alex").getAllEvents().size());
    assertEquals(this.saturdayCentralSystem.getUserSchedule("Alex").getAllEvents(),
            new ArrayList<>(Arrays.asList(this.sleep)));
    this.saturdayCentralSystem.createEvent("Prof. Lucia", this.afternoonLecture);
    try {
      this.saturdayCentralSystem.modifyEvent("Alex", this.morningLecture, this.overlap);
      Assert.fail("Cannot modify event due to event overlap.");
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testDeleteEventByHostUserCentralSystem() {
    this.saturdayCentralSystem.addUser(this.user1); // alex
    this.saturdayCentralSystem.createEvent("Alex", this.morningLecture);
    this.saturdayCentralSystem.createEvent("Prof. Lucia", this.afternoonLecture);
    assertEquals(2, this.saturdayCentralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(2, this.saturdayCentralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(1, this.saturdayCentralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
    this.saturdayCentralSystem.deleteEvent(this.afternoonLecture);
    assertEquals(1, this.saturdayCentralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(1, this.saturdayCentralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(1, this.saturdayCentralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
  }

  // this does not work
  @Test
  public void testDeleteEventByHostUser() {
    this.saturdayCentralSystem.addUser(this.user1);
    this.saturdayCentralSystem.createEvent("Alex", this.morningLecture);
    this.saturdayCentralSystem.createEvent("Prof. Lucia", this.afternoonLecture);
    assertEquals(2, this.saturdayCentralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(2, this.saturdayCentralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(1, this.saturdayCentralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
    this.user3.removeEventFromSchedule(this.afternoonLecture);
    assertEquals(1, this.saturdayCentralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(2, this.saturdayCentralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(1, this.saturdayCentralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
  }

  @Test
  public void testDeleteEventByInvitedUser() {
    this.saturdayCentralSystem.addUser(this.user1);
    this.saturdayCentralSystem.createEvent("Alex", this.morningLecture);
    this.saturdayCentralSystem.createEvent("Prof. Lucia", this.afternoonLecture);
    assertEquals(2, this.saturdayCentralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(2, this.saturdayCentralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(1, this.saturdayCentralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
    this.user2.removeEventFromSchedule(this.morningLecture);
    assertEquals(2, this.saturdayCentralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(2, this.saturdayCentralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(0, this.saturdayCentralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
  }

  @Test
  public void testXMLWithExistingUser() {
    this.saturdayCentralSystem.addUser(this.user3);
    this.saturdayCentralSystem.uploadXMLFileToSystem(this.filePath1);
    ScheduleTextView stv = new ScheduleTextView(this.saturdayCentralSystem);
    assertEquals(stv.toString(), "user: Alex\n" +
            "Sunday:\n" +
            "Monday:\n" +
            "Tuesday:\n" +
            "\tname: \"CS3500 Morning Lecture\"\n" +
            "\ttime: Tuesday: 950 -> Tuesday: 1130\n" +
            "\tlocation: \"Churchill Hall 101\"\n" +
            "\tonline: false\n" +
            "\tinvitees: Alex\n" +
            "\t\"Student Anon\"\n" +
            "\t\"Chat\"\n" +
            "\tname: \"CS3500 Afternoon Lecture\"\n" +
            "\ttime: Tuesday: 1335 -> Tuesday: 1515\n" +
            "\tlocation: \"Churchill Hall 101\"\n" +
            "\tonline: false\n" +
            "\tinvitees: Alex\n" +
            "\t\"Chat\"\n" +
            "Wednesday:\n" +
            "Thursday:\n" +
            "Friday:\n" +
            "\tname: Sleep\n" +
            "\ttime: Friday: 180 -> Sunday: 120\n" +
            "\tlocation: Home\n" +
            "\tonline: true\n" +
            "\tinvitees: Alex\n" +
            "Saturday:\n");
  }

  @Test
  public void testXMLWithAddedEvent() {
    this.saturdayCentralSystem.addUser(this.user3);
    this.saturdayCentralSystem.createEvent("Prof. Lucia", this.party);
    this.saturdayCentralSystem.uploadXMLFileToSystem(this.filePath1);
    ScheduleTextView stv = new ScheduleTextView(this.saturdayCentralSystem);
    assertEquals(stv.toString(), "user: Prof. Lucia\nSunday:\nMonday:\n"
            + "Tuesday:\n\tname: \"CS3500 Morning Lecture\"\n"
            + "\ttime: Tuesday: 950 -> Tuesday: 1130\n"
            + "\tlocation: \"Churchill Hall 101\"\n"
            + "\tonline: false\n\tinvitees: Prof. Lucia\n"
            + "\t\"Student Anon\"\n\t\"Chat\"\n"
            + "\tname: \"CS3500 Afternoon Lecture\"\n"
            + "\ttime: Tuesday: 1335 -> Tuesday: 1515\n"
            + "\tlocation: \"Churchill Hall 101\"\n"
            + "\tonline: false\n\tinvitees: Prof. Lucia\n"
            + "\t\"Chat\"\nWednesday:\nThursday:\n"
            + "\tname: Party\n\ttime: Thursday: 2130 -> Thursday: 2359\n"
            + "\tlocation: Home\n\tonline: false\n"
            + "\tinvitees: Prof. Lucia\nFriday:\n"
            + "\tname: Sleep\n\ttime: Friday: 180 -> Sunday: 120\n"
            + "\tlocation: Home\n\tonline: true\n"
            + "\tinvitees: Prof. Lucia\nSaturday:\n");
  }

  @Test
  public void testXMLWithNonExistentUser() {
    this.saturdayCentralSystem.uploadXMLFileToSystem(this.filePath1);
    ScheduleTextView stv = new ScheduleTextView(this.saturdayCentralSystem);
    assertEquals(stv.toString(), "user: Prof. Lucia\nSunday:\nMonday:\n"
            + "Tuesday:\n"
            + "\tname: \"CS3500 Morning Lecture\"\n"
            + "\ttime: Tuesday: 950 -> Tuesday: 1130\n"
            + "\tlocation: \"Churchill Hall 101\"\n"
            + "\tonline: false\n"
            + "\tinvitees: Prof. Lucia\n"
            + "\t\"Student Anon\"\n"
            + "\t\"Chat\"\n"
            + "\tname: \"CS3500 Afternoon Lecture\"\n"
            + "\ttime: Tuesday: 1335 -> Tuesday: 1515\n"
            + "\tlocation: \"Churchill Hall 101\"\n"
            + "\tonline: false\n"
            + "\tinvitees: Prof. Lucia\n"
            + "\t\"Chat\"\n"
            + "Wednesday:\n"
            + "Thursday:\n"
            + "Friday:\n"
            + "\tname: Sleep\n"
            + "\ttime: Friday: 180 -> Sunday: 120\n"
            + "\tlocation: Home\n"
            + "\tonline: true\n"
            + "\tinvitees: Prof. Lucia\n"
            + "Saturday:\n");
  }

  @Test
  public void testOverlappingEvents() {
    this.saturdayCentralSystem.addUser(this.user3);
    try {
      this.saturdayCentralSystem.createEvent("Prof. Lucia", this.morningLecture);
      this.saturdayCentralSystem.uploadXMLFileToSystem(this.filePath1);
      Assert.fail("The events should be overlapping.");
    } catch (IllegalArgumentException ignored) {
    }
  }
}