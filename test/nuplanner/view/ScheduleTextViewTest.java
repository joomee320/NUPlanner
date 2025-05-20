package nuplanner.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.List;

import nuplanner.model.CentralSystem;
import nuplanner.model.Day;
import nuplanner.model.Event;
import nuplanner.model.MutableSystems;
import nuplanner.model.User;

import static org.junit.Assert.assertEquals;

/**
 * This class creates the tests to test the text view version of the created planner.
 */
public class ScheduleTextViewTest {
  ScheduleTextView stv1;
  MutableSystems centralSystem;
  Event luciaLecture;
  User alex;
  User joomee;

  @Before
  public void init() {
    this.centralSystem = new CentralSystem();
    this.stv1 = new ScheduleTextView(this.centralSystem);
    this.luciaLecture = new Event("CS3500 Morning Lecture",
            Day.TUESDAY, LocalTime.of(9, 50), Day.TUESDAY,
            LocalTime.of(11, 30), false, "Churchill Hall 101",
            new User("Prof. Lucia"), List.of(new User("Anon"), new User("Chat")));
    this.alex = new User("Alex");
    this.joomee = new User("Joomee");
  }

  @Test
  public void testWithNoUsers() {
    assertEquals(this.stv1.toString(), "");
  }

  @Test
  public void testWithAddedEvents() {
    this.centralSystem.createEvent("Prof. Lucia", luciaLecture);
    Assert.assertEquals(this.stv1.toString(), "user: Anon\nSunday:\nMonday:\nTuesday:\n"
            + "\tname: CS3500 Morning Lecture\n\ttime: Tuesday: 950 -> Tuesday: 1130\n"
            + "\tlocation: Churchill Hall 101\n\tonline: false\n\tinvitees: Prof. Lucia\n"
            + "\tAnon\n\tChat\nWednesday:\nThursday:\nFriday:\nSaturday:\n"
            + "user: Prof. Lucia\nSunday:\nMonday:\nTuesday:\n"
            + "\tname: CS3500 Morning Lecture\n\ttime: Tuesday: 950 -> Tuesday: 1130\n"
            + "\tlocation: Churchill Hall 101\n\tonline: false\n"
            + "\tinvitees: Prof. Lucia\n\tAnon\n\tChat\nWednesday:\nThursday:\nFriday:\nSaturday:\n"
            + "user: Chat\nSunday:\nMonday:\nTuesday:\n"
            + "\tname: CS3500 Morning Lecture\n\ttime: Tuesday: 950 -> Tuesday: 1130\n"
            + "\tlocation: Churchill Hall 101\n\tonline: false\n"
            + "\tinvitees: Prof. Lucia\n\tAnon\n\tChat\nWednesday:"
            + "\nThursday:\nFriday:\nSaturday:\n");
  }

  // a new test made in April 8th by Joomee
  @Test
  public void testCreatEvent() {
    this.centralSystem.addUser(this.alex);
    this.centralSystem.addUser(this.joomee);
    Event morningLecture = new Event("CS3500 Morning Lecture",
            Day.TUESDAY, LocalTime.of(9, 50), Day.TUESDAY,
            LocalTime.of(11, 30), false, "Churchill Hall 101",
            this.alex, List.of(this.joomee));
    centralSystem.createEvent("Alex", morningLecture);
    Event afternoonLecture = new Event("CS3500 Afternoon Lecture",
            Day.THURSDAY, LocalTime.of(13, 35), Day.THURSDAY,
            LocalTime.of(15, 15), false, "Churchill Hall 101",
            this.alex, List.of(this.joomee));
    centralSystem.createEvent("Alex", afternoonLecture);
    Assert.assertEquals(this.stv1.toString(), "user: Alex\n" +
            "Sunday:\n" +
            "Monday:\n" +
            "Tuesday:\n" +
            "\tname: CS3500 Morning Lecture\n" +
            "\ttime: Tuesday: 950 -> Tuesday: 1130\n" +
            "\tlocation: Churchill Hall 101\n" +
            "\tonline: false\n" +
            "\tinvitees: Alex\n" +
            "\tJoomee\n" +
            "Wednesday:\n" +
            "Thursday:\n" +
            "\tname: CS3500 Afternoon Lecture\n" +
            "\ttime: Thursday: 1335 -> Thursday: 1515\n" +
            "\tlocation: Churchill Hall 101\n" +
            "\tonline: false\n" +
            "\tinvitees: Alex\n" +
            "\tJoomee\n" +
            "Friday:\n" +
            "Saturday:\n" +
            "user: Joomee\n" +
            "Sunday:\n" +
            "Monday:\n" +
            "Tuesday:\n" +
            "\tname: CS3500 Morning Lecture\n" +
            "\ttime: Tuesday: 950 -> Tuesday: 1130\n" +
            "\tlocation: Churchill Hall 101\n" +
            "\tonline: false\n" +
            "\tinvitees: Alex\n" +
            "\tJoomee\n" +
            "Wednesday:\n" +
            "Thursday:\n" +
            "\tname: CS3500 Afternoon Lecture\n" +
            "\ttime: Thursday: 1335 -> Thursday: 1515\n" +
            "\tlocation: Churchill Hall 101\n" +
            "\tonline: false\n" +
            "\tinvitees: Alex\n" +
            "\tJoomee\n" +
            "Friday:\n" +
            "Saturday:\n");

  }
}
