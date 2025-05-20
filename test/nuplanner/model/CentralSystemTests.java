package nuplanner.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nuplanner.view.ScheduleTextView;

import static org.junit.Assert.assertEquals;

/**
 * The tests for the central system.
 */
public class CentralSystemTests {
  MutableSystems centralSystem;
  User user1;
  User user2;
  User user3;
  Event morningLecture;
  Event afternoonLecture;
  Event sleep;
  Event overlap;
  Event party;
  String filePath1;
  String filePath2;

  @Before
  public void init() {
    this.centralSystem = new CentralSystem();
    this.user1 = new User("Alex");
    this.user2 = new User("Joomee");
    this.user3 = new User("Prof. Lucia");
    this.morningLecture = new Event("CS3500 Morning Lecture",
            Day.TUESDAY, LocalTime.of(9, 50), Day.TUESDAY,
            LocalTime.of(11, 30), false, "Churchill Hall 101",
            this.user1, List.of(this.user2, this.user3));
    this.afternoonLecture = new Event("CS3500 Afternoon Lecture",
            Day.TUESDAY, LocalTime.of(13, 35), Day.TUESDAY,
            LocalTime.of(15, 50), false, "Churchill Hall 101",
            this.user3, List.of(this.user1));
    this.sleep = new Event("Sleep", Day.FRIDAY, LocalTime.of(20, 0),
            Day.SUNDAY,
            LocalTime.of(9, 30), false, "Home", this.user1, List.of());
    this.overlap = new Event("Sleep", Day.FRIDAY, LocalTime.of(20, 0),
            Day.SUNDAY,
            LocalTime.of(8, 30), false, "Home", this.user1, List.of());
    this.party = new Event("Party", Day.THURSDAY, LocalTime.of(21, 30),
            Day.THURSDAY,
            LocalTime.of(23, 59), false, "Home", this.user3, List.of());
    filePath1 = "/Users/joomeechoi/Desktop/IntelliJ Project/Homework5/src/prof.xml";
    filePath2 = "/Users/joomeechoi/Desktop/IntelliJ Project/Homework5/";
  }

  @Test
  public void testUserCreation() {
    assertEquals(0, this.centralSystem.getAllUsers().size());
  }

  @Test
  public void testGetAllUsers() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.addUser(this.user2);
    this.centralSystem.addUser(this.user3);
    Map<String, User> users = new HashMap<>();
    users.put("Alex", this.user1);
    users.put("Joomee", this.user2);
    users.put("Prof. Lucia", this.user3);
    assertEquals(users, this.centralSystem.getAllUsers());
  }

  @Test
  public void testGetUsers() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.addUser(this.user2);
    this.centralSystem.addUser(this.user3);
    Map<String, User> users = new HashMap<>();
    users.put("Alex", this.user1);
    users.put("Joomee", this.user2);
    users.put("Prof. Lucia", this.user3);
    Assert.assertEquals(user1, this.centralSystem.getUser("Alex"));
  }



  @Test
  public void testAddUser() {
    this.centralSystem.addUser(this.user1);
    assertEquals(1, this.centralSystem.getAllUsers().size());
    this.centralSystem.addUser(this.user2);
    assertEquals(2, this.centralSystem.getAllUsers().size());
    try {
      this.centralSystem.addUser(this.user1);
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testRemoveUser() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.addUser(this.user2);
    this.centralSystem.addUser(this.user3);
    this.centralSystem.removeUser(this.user3);
    assertEquals(2, this.centralSystem.getAllUsers().size());
  }

  @Test
  public void testGetUserSchedule() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.createEvent("Alex", this.morningLecture);
    Schedule sc1 = new Schedule("Alex");
    sc1.addEvent(this.morningLecture);
    Schedule sc2 = new Schedule("Joomee");
    sc2.addEvent(this.morningLecture);
    Schedule sc3 = new Schedule("Prof. Lucia");
    sc3.addEvent(this.morningLecture);
    assertEquals(sc1.getAllEvents(), this.centralSystem.getUserSchedule("Alex")
                    .getAllEvents());
    assertEquals(sc2.getAllEvents(), this.centralSystem.getUserSchedule("Joomee")
                    .getAllEvents());
    assertEquals(sc3.getAllEvents(), this.centralSystem.getUserSchedule("Prof. Lucia")
                    .getAllEvents());
  }

  @Test
  public void testCreateEvent() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.createEvent("Alex", this.morningLecture);
    this.centralSystem.createEvent("Prof. Lucia", this.afternoonLecture);
    assertEquals(2, this.centralSystem.getUserSchedule("Prof. Lucia").getAllEvents().size());
    assertEquals(2, this.centralSystem.getUserSchedule("Alex").getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Joomee").getAllEvents().size());
    try {
      this.centralSystem.createEvent("Prof. Lucia", this.morningLecture);
      Assert.fail("Should not be any overlap when adding events.");
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testModifyEvent() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.createEvent("Alex", this.morningLecture);
    try {
      this.centralSystem.modifyEvent("Alex", this.morningLecture, this.afternoonLecture);
      Assert.fail("Cannot change the host of the event.");
    } catch (IllegalArgumentException ignored) {
    }
    assertEquals(1, this.centralSystem.getUserSchedule("Alex").getAllEvents().size());
    this.centralSystem.modifyEvent("Alex", this.morningLecture, this.sleep);
    assertEquals(this.sleep, this.centralSystem.getUserSchedule("Alex").getAllEvents().get(0));
    assertEquals(1, this.centralSystem.getUserSchedule("Alex").getAllEvents().size());
    assertEquals(this.centralSystem.getUserSchedule("Alex").getAllEvents(),
            new ArrayList<>(Arrays.asList(this.sleep)));
    this.centralSystem.createEvent("Prof. Lucia", this.afternoonLecture);
    try {
      this.centralSystem.modifyEvent("Alex", this.morningLecture, this.overlap);
      Assert.fail("Cannot modify event due to event overlap.");
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testDeleteEventByHostUserCentralSystem() {
    this.centralSystem.addUser(this.user1); // alex
    this.centralSystem.createEvent("Alex", this.morningLecture);
    // alex is the host, joomee and prof invited
    this.centralSystem.createEvent("Prof. Lucia", this.afternoonLecture);
    // prof is the host, alex is invited
    assertEquals(2, this.centralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(2, this.centralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
    this.centralSystem.deleteEvent(this.afternoonLecture);
    assertEquals(1, this.centralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
  }

  // this does not work
  @Test
  public void testDeleteEventByHostUser() {
    this.centralSystem.addUser(this.user1); // alex
    this.centralSystem.createEvent("Alex", this.morningLecture);
    // alex is the host, joomee and prof invited
    this.centralSystem.createEvent("Prof. Lucia", this.afternoonLecture);
    // prof is the host, alex is invited
    assertEquals(2, this.centralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(2, this.centralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
    this.user3.removeEventFromSchedule(this.afternoonLecture);
    assertEquals(1, this.centralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
  }

  @Test
  public void testDeleteEventByInvitedUser() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.createEvent("Alex", this.morningLecture);
    // alex is the host, joomee and prof invited
    this.centralSystem.createEvent("Prof. Lucia", this.afternoonLecture);
    // prof is the host, alex is invited
    assertEquals(2, this.centralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(2, this.centralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
    this.user2.removeEventFromSchedule(this.morningLecture); //Joomee deleting the event
    assertEquals(2, this.centralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(2, this.centralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    assertEquals(0, this.centralSystem.getUserSchedule("Joomee").
            getAllEvents().size());
  }

  @Test
  public void testXMLWithExistingUser() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.uploadXMLFileToSystem(this.filePath1);
    ScheduleTextView stv = new ScheduleTextView(this.centralSystem);
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
    this.centralSystem.addUser(this.user3);
    this.centralSystem.createEvent("Prof. Lucia", this.party);
    this.centralSystem.uploadXMLFileToSystem(this.filePath1);
    ScheduleTextView stv = new ScheduleTextView(this.centralSystem);
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
    this.centralSystem.uploadXMLFileToSystem(this.filePath1);
    ScheduleTextView stv = new ScheduleTextView(this.centralSystem);
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
    this.centralSystem.addUser(this.user3);
    try {
      this.centralSystem.createEvent("Prof. Lucia", this.morningLecture);
      this.centralSystem.uploadXMLFileToSystem(this.filePath1);
      Assert.fail("The events should be overlapping.");
    } catch (IllegalArgumentException ignored) {
    }
  }
}