package nuplanner.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.List;


/**
 * This class is created to test whether protected methods in Event work properly.
 */
public class EventTest {

  User lucia;
  User anon;
  User chat;
  Event e1;
  Event e2;
  Event e3;
  User joomee;
  User alex;

  @Before
  public void setUp() {
    this.lucia = new User("Prof. Lucia");
    this.anon = new User("Student Anon");
    this.chat = new User("Chat");
    this.joomee = new User("Student Joomee");
    this.alex = new User("Student Alex");
    this.e1 = new Event("CS3500 Morning Lecture",
            Day.TUESDAY, LocalTime.of(9, 50), Day.TUESDAY,
            LocalTime.of(11, 30), false, "Churchill Hall 101",
            lucia, List.of(anon, chat));

    this.e2 = new Event("CS3500 Afternoon Lecture",
            Day.TUESDAY, LocalTime.of(13, 35), Day.TUESDAY,
            LocalTime.of(15, 15), false, "Churchill Hall 101",
            lucia, List.of(chat));
    this.e3 = new Event("Sleep", Day.FRIDAY, LocalTime.of(18, 0),
            Day.SUNDAY, LocalTime.of(12, 0), true, "Home",
            lucia, List.of());
    lucia.addEventToSchedule(e1);
    lucia.addEventToSchedule(e2);
    lucia.addEventToSchedule(e3);
  }


  /**
   * This test the method calculateEventDurationInMinutes.
   */
  @Test
  public void testLongestEventDurationCalculation() {
    Event longestEvent = new Event("1 Week of Travel",
            Day.WEDNESDAY, LocalTime.of(16, 0), Day.WEDNESDAY,
            LocalTime.of(15, 59), false, "Florida", joomee, List.of());
    Assert.assertEquals(10079, longestEvent.calculateEventDurationInMinutes(Day.WEDNESDAY,
            LocalTime.of(16, 0), Day.WEDNESDAY,
            LocalTime.of(15, 59)));
  }

  /**
   * This test the method calculateEventDurationInMinutes.
   */
  @Test
  public void testEventDurationCalculationOvernight() {
    Event trip = new Event("trip for 5 nights and 6 days",
            Day.TUESDAY, LocalTime.of(11, 0), Day.SUNDAY,
            LocalTime.of(14, 0), false, "Florida", joomee, List.of());
    Assert.assertEquals(7380,
            trip.calculateEventDurationInMinutes(
                    Day.TUESDAY, LocalTime.of(11, 0), Day.SUNDAY,
            LocalTime.of(14, 0)));
  }

  /**
   * This test the method calculateEventDurationInMinutes.
   */
  @Test
  public void testEventDurationCalculationLessThan24hours() {
    Event twoHourEvent = new Event("1 Week of Travel",
            Day.WEDNESDAY, LocalTime.of(10, 0), Day.WEDNESDAY,
            LocalTime.of(12, 0), false, "Florida", joomee, List.of());
    Assert.assertEquals(120, twoHourEvent.calculateEventDurationInMinutes(Day.WEDNESDAY,
            LocalTime.of(10, 0), Day.WEDNESDAY,
            LocalTime.of(12, 0)));
  }

  /**
   * This checks if an Event that has the same day, same time throws the error.
   */
  @Test
  public void testEventSpansForMoreThanOneWeekThrowsError() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new Event("Invalid Event",
            Day.WEDNESDAY, LocalTime.of(16, 0), Day.WEDNESDAY,
            LocalTime.of(16, 0), false, "Florida", joomee, List.of()));
  }

  /**
   * This checks if an Event that has a null value for necessary element throws an error.
   */
  @Test
  public void testEventWithNullElementThrowsError() {
    Assert.assertThrows(IllegalStateException.class, () -> new Event("Null Event",
            Day.WEDNESDAY, LocalTime.of(16, 0), Day.WEDNESDAY,
            LocalTime.of(16, 0), false, "Florida", joomee, null));
  }
}