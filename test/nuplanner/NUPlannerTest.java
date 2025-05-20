package nuplanner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nuplanner.model.CentralSystem;
import nuplanner.model.Day;
import nuplanner.model.Event;
import nuplanner.model.MutableSystems;
import nuplanner.model.User;
import nuplanner.view.ScheduleTextView;

import static org.junit.Assert.assertEquals;

/**
 * Creates the class for the NUPlanner test to see what the client can access.
 */
public class NUPlannerTest {
  String profFilePath;
  String directoryFilePath;
  Event earlyLecture;
  Event freeTime;
  Event lectureWithTwoStudents;
  Event lecture;
  MutableSystems centralSystem;
  User user1;
  User user2;
  User user3;


  @Before
  public void setUp() {
    profFilePath = "/Users/joomeechoi/Desktop/IntelliJ Project/Homework5/src/prof.xml";
    directoryFilePath = "/Users/joomeechoi/Desktop/IntelliJ Project/Homework5/";
    centralSystem = new CentralSystem();
    user1 = new User("Alex");
    user2 = new User("Joomee");
    user3 = new User("Prof. Lucia");
    earlyLecture = new Event("CS3500 Early Lecture",
            Day.TUESDAY, LocalTime.of(7, 50), Day.TUESDAY,
            LocalTime.of(9, 30), false, "Churchill Hall 101",
            this.user3, List.of());
    freeTime = new Event("Free Time",
            Day.TUESDAY, LocalTime.of(9, 50), Day.TUESDAY,
            LocalTime.of(11, 30), false, "Home",
            this.user3, List.of());
    lectureWithTwoStudents = new Event("CS3500 Lecture with Joomee and Alex",
            Day.TUESDAY, LocalTime.of(13, 35), Day.TUESDAY,
            LocalTime.of(15, 10), false, "Churchill Hall 101",
            this.user3,
            List.of(new User("Student Joomee"), this.user1));
    lecture = new Event("CS3500 Early Lecture",
            Day.TUESDAY, LocalTime.of(7, 50), Day.TUESDAY,
            LocalTime.of(9, 30), false, "Churchill Hall 101",
            this.user3, List.of(user1));
  }

  @Test
  public void testCreateEvent() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.createEvent("Alex", this.earlyLecture);
    this.centralSystem.createEvent("Prof. Lucia", this.freeTime);
    assertEquals(1, this.centralSystem.getUserSchedule("Prof. Lucia").getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Alex").getAllEvents().size());
    try {
      this.centralSystem.createEvent("Prof. Lucia", this.freeTime);
      Assert.fail("Should not be any overlap when adding events.");
    } catch (IllegalArgumentException ignored) {
    }
  }

  //This also does not work
  @Test
  public void testModifyEvent() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.createEvent("Alex", this.earlyLecture);
    try {
      this.centralSystem.modifyEvent("Alex", this.earlyLecture, this.lectureWithTwoStudents);
      Assert.fail("Cannot change the host of the event.");
    } catch (IllegalArgumentException ignored) {
    }
    assertEquals(1, this.centralSystem.getUserSchedule("Alex").getAllEvents().size());
    this.centralSystem.modifyEvent("Prof. Lucia", this.earlyLecture,
            this.lectureWithTwoStudents);
    assertEquals(this.earlyLecture, this.centralSystem.getUserSchedule(
            "Alex").getAllEvents().get(0));
    assertEquals(1, this.centralSystem.getUserSchedule(
            "Alex").getAllEvents().size());
    assertEquals(this.centralSystem.getUserSchedule("Alex").getAllEvents(),
            new ArrayList<>(Arrays.asList(this.earlyLecture)));
    this.centralSystem.createEvent("Prof. Lucia", this.lectureWithTwoStudents);
    try {
      this.centralSystem.modifyEvent("Alex",
              this.lectureWithTwoStudents, this.lectureWithTwoStudents);
      Assert.fail("Cannot modify event due to event overlap.");
    } catch (IllegalArgumentException ignored) {
    }
  }

  // This does not work
  @Test
  public void testDeleteEventByHostUser() {
    this.centralSystem.addUser(this.user1); // alex
    this.centralSystem.addUser(this.user3); // prof
    this.centralSystem.createEvent("Prof. Lucia", this.lecture); // alex is invited
    assertEquals(1, this.centralSystem.getUserSchedule("Prof. Lucia").getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Alex").getAllEvents().size());
    this.user3.removeEventFromSchedule(this.lecture);
    assertEquals(0, this.centralSystem.getUserSchedule("Prof. Lucia").getAllEvents().size());
    assertEquals(0, this.centralSystem.getUserSchedule("Alex").getAllEvents().size());
  }

  @Test
  public void testXMLWithExistingUserWithEmptySchedule() {
    centralSystem.addUser(new User("Prof. Lucia"));
    centralSystem.uploadXMLFileToSystem(profFilePath);
    ScheduleTextView stv = new ScheduleTextView(centralSystem);
    System.out.print(stv);
    Assert.assertEquals(stv.toString(),
            "user: Prof. Lucia\n" +
                    "Sunday:\n" +
                    "Monday:\n" +
                    "Tuesday:\n" +
                    "\tname: \"CS3500 Morning Lecture\"\n" +
                    "\ttime: Tuesday: 950 -> Tuesday: 1130\n" +
                    "\tlocation: \"Churchill Hall 101\"\n" +
                    "\tonline: false\n" +
                    "\tinvitees: Prof. Lucia\n" +
                    "\t\"Student Anon\"\n" +
                    "\t\"Chat\"\n" +
                    "\tname: \"CS3500 Afternoon Lecture\"\n" +
                    "\ttime: Tuesday: 1335 -> Tuesday: 1515\n" +
                    "\tlocation: \"Churchill Hall 101\"\n" +
                    "\tonline: false\n" +
                    "\tinvitees: Prof. Lucia\n" +
                    "\t\"Chat\"\n" +
                    "Wednesday:\n" +
                    "Thursday:\n" +
                    "Friday:\n" +
                    "\tname: Sleep\n" +
                    "\ttime: Friday: 180 -> Sunday: 120\n" +
                    "\tlocation: Home\n" +
                    "\tonline: true\n" +
                    "\tinvitees: Prof. Lucia\n" +
                    "Saturday:\n");
  }

  @Test
  public void testXMLWithoutExistingUser() {
    centralSystem.uploadXMLFileToSystem(profFilePath);
    ScheduleTextView stv = new ScheduleTextView(centralSystem);
    System.out.println(stv);
    Assert.assertEquals(stv.toString(), "user: Prof. Lucia\n" +
            "Sunday:\n" +
            "Monday:\n" +
            "Tuesday:\n" +
            "\tname: \"CS3500 Morning Lecture\"\n" +
            "\ttime: Tuesday: 950 -> Tuesday: 1130\n" +
            "\tlocation: \"Churchill Hall 101\"\n" +
            "\tonline: false\n" +
            "\tinvitees: Prof. Lucia\n" +
            "\t\"Student Anon\"\n" +
            "\t\"Chat\"\n" +
            "\tname: \"CS3500 Afternoon Lecture\"\n" +
            "\ttime: Tuesday: 1335 -> Tuesday: 1515\n" +
            "\tlocation: \"Churchill Hall 101\"\n" +
            "\tonline: false\n" +
            "\tinvitees: Prof. Lucia\n" +
            "\t\"Chat\"\n" +
            "Wednesday:\n" +
            "Thursday:\n" +
            "Friday:\n" +
            "\tname: Sleep\n" +
            "\ttime: Friday: 180 -> Sunday: 120\n" +
            "\tlocation: Home\n" +
            "\tonline: true\n" +
            "\tinvitees: Prof. Lucia\n" +
            "Saturday:\n");
  }

  @Test
  public void testXMLWithExistingUserWithSchedule() {
    centralSystem.addUser(new User("Prof. Lucia"));
    centralSystem.createEvent("Prof. Lucia", earlyLecture);
    centralSystem.uploadXMLFileToSystem(profFilePath);
    ScheduleTextView stv = new ScheduleTextView(centralSystem);
    Assert.assertEquals(stv.toString(), "user: Prof. Lucia\n" +
            "Sunday:\n" +
            "Monday:\n" +
            "Tuesday:\n" +
            "\tname: CS3500 Early Lecture\n" +
            "\ttime: Tuesday: 750 -> Tuesday: 930\n" +
            "\tlocation: Churchill Hall 101\n" +
            "\tonline: false\n" +
            "\tinvitees: Prof. Lucia\n" +
            "\tname: \"CS3500 Morning Lecture\"\n" +
            "\ttime: Tuesday: 950 -> Tuesday: 1130\n" +
            "\tlocation: \"Churchill Hall 101\"\n" +
            "\tonline: false\n" +
            "\tinvitees: Prof. Lucia\n" +
            "\t\"Student Anon\"\n" +
            "\t\"Chat\"\n" +
            "\tname: \"CS3500 Afternoon Lecture\"\n" +
            "\ttime: Tuesday: 1335 -> Tuesday: 1515\n" +
            "\tlocation: \"Churchill Hall 101\"\n" +
            "\tonline: false\n" +
            "\tinvitees: Prof. Lucia\n" +
            "\t\"Chat\"\n" +
            "Wednesday:\n" +
            "Thursday:\n" +
            "Friday:\n" +
            "\tname: Sleep\n" +
            "\ttime: Friday: 180 -> Sunday: 120\n" +
            "\tlocation: Home\n" +
            "\tonline: true\n" +
            "\tinvitees: Prof. Lucia\n" +
            "Saturday:\n");
  }

  @Test
  public void testUploadXMLThrowsErrorWhenOverlappingEventsExist() {
    CentralSystem cs = new CentralSystem();
    cs.addUser(new User("Prof. Lucia"));
    cs.createEvent("Prof. Lucia", freeTime);
    //Ensure this creates a setup that will cause an overlap
    Assert.assertThrows(IllegalArgumentException.class, () ->
            cs.uploadXMLFileToSystem(profFilePath));
  }

  @Test
  public void writeXMLUploadingNewUser() {
    CentralSystem cs = new CentralSystem();
    cs.createEvent("Prof. Lucia", earlyLecture);
    cs.saveXMLFileFromSystem(directoryFilePath);
    Assert.assertEquals(1, cs.getAllUsers().size());
  }

  // This test also verifies getUserSchedule(display method) inside Central System.
  @Test
  public void writeXMLCorrectlyUploadsScheduleToExistingUser() {
    CentralSystem cs = new CentralSystem();
    User lucia = new User("Prof. Lucia");
    cs.addUser(lucia);
    cs.createEvent("Prof. Lucia", earlyLecture);
    cs.saveXMLFileFromSystem(directoryFilePath);
    assertEquals(1,
            cs.getUserSchedule("Prof. Lucia").getAllEvents().size());
  }


  @Test
  public void writeXMLWithInvitedUser() {
    CentralSystem cs = new CentralSystem();
    cs.addUser(new User("Prof. Lucia"));
    cs.createEvent("Prof. Lucia", earlyLecture);
    cs.createEvent("Prof. Lucia", lectureWithTwoStudents);
    cs.saveXMLFileFromSystem(directoryFilePath);
    // adding XML file schedule with non-existing users as invited user will also create
    // users inside the central system.
    Assert.assertEquals(3,cs.getAllUsers().size());
  }

  // This tests whether user deleting the event works properly.
  @Test
  public void testInvitedUserDeletesEvent() {
    this.centralSystem.addUser(this.user1);
    this.centralSystem.createEvent("Prof. Lucia", this.lectureWithTwoStudents);
    assertEquals(1, this.centralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(1, this.centralSystem.getUserSchedule("Alex").
            getAllEvents().size());
    this.user1.removeEventFromSchedule(this.lectureWithTwoStudents); //Joomee deleting the event
    assertEquals(1, this.centralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    assertEquals(0, this.centralSystem.getUserSchedule("Alex").
            getAllEvents().size());
  }

  // This test is created to debug uploadXML file for plannerFrame on 4/8 by joomee.
  @Test
  public void testUploadXMLFile() {
    User lucia = new User("Prof. Lucia");
    User alex = new User("Student Alex");
    User joomee = new User("Student Joomee");
    Event lecture = new Event("CS3500 Thursday Lecture", Day.THURSDAY,
            LocalTime.of(9,50), Day.THURSDAY, LocalTime.of(11,30),
            false,"Churchill Hall 101", lucia, List.of(alex, joomee));
    centralSystem.addUser(lucia);
    centralSystem.addUser(alex);
    centralSystem.addUser(joomee);
    centralSystem.createEvent("Prof. Lucia", lecture);
    centralSystem.uploadXMLFileToSystem(profFilePath); // has events on Tue and Fri
    assertEquals(4, this.centralSystem.getUserSchedule("Prof. Lucia").
            getAllEvents().size());
    //check if there are total of 5 users
    assertEquals(1, this.centralSystem.getUserSchedule("Student Alex").
            getAllEvents().size());
  }
}
