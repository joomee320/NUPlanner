package nuplanner.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.JComboBox;

import nuplanner.model.IEvent;
import nuplanner.model.ISchedule;
import nuplanner.model.IUser;
import nuplanner.model.ReadOnlySystems;

/**
 * The class that paints the lines and the event boxes onto the panel.
 */
public class PaintComponent {
  private final ReadOnlySystems model;
  private final JComboBox<String> users;
  private List<IEvent> events;
  private boolean isSaturday;
  private final int height;
  private final int width;
  private Color color;

  /**
   * The constructor for the paint component class.
   * @param model the model it takes information from
   * @param users the list of users
   * @param events the list of events
   * @param isSaturday if the saturday schedule is being used or not
   * @param height the height of the panel
   * @param width the width of the panel
   * @param color the color being painted on
   */
  public PaintComponent(ReadOnlySystems model, JComboBox<String> users,
                        List<IEvent> events, boolean isSaturday, int height,
                        int width, Color color) {
    this.model = model;
    this.users = users;
    this.events = events;
    this.isSaturday = isSaturday;
    this.height = height;
    this.width = width;
    this.color = color;
  }

  /**
   * The method that paints the lines and the boxes onto the panel.
   * @param g the graphics being used to draw things
   */
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    int x = this.width / 7;
    int first = this.height / 24;
    int y = (this.height - (2 * first)) / 24;
    for (int xCoord = 1; xCoord <= 7; xCoord++) {
      g2d.drawLine(x * xCoord, y, x * xCoord, y * 25);
    }
    for (int yCoord = 1; yCoord <= 24; yCoord++) {
      g2d.drawLine(0, y * yCoord, this.width, y * yCoord);
    }
    this.drawRed(g2d);
  }

  /**
   * The method that goes through and draws the red boxes.
   * @param g2d the graphics parameter passed in to draw it
   */
  private void drawRed(Graphics2D g2d) {
    Map<String, IUser> users = this.model.getAllUsers();
    for (IUser u : users.values()) {
      if (Objects.equals(this.users.getSelectedItem(), u.getUid())) {
        ISchedule schedule = this.model.getUserSchedule(u.getUid());
        List<IEvent> events = schedule.getAllEvents();
        for (IEvent e : events) {
          if (e.getHostUser().getUid().equals(this.users.getSelectedItem()) && this.isSaturday) {
            this.color = Color.BLUE;
          }
          else {
            this.color = Color.RED;
          }
          LocalTime start = e.getStartTime();
          LocalTime end = e.getEndTime();
          String startDay = e.getStartDay().toString();
          String endDay = e.getEndDay().toString();
          this.drawBoxes(g2d, startDay, endDay, start, end);
          this.events.add(e);
        }
      }
    }
  }

  /**
   * Draws the boxes onto the panel.
   * @param g2d the graphics parameter to draw onto
   * @param startDay the first day in the event
   * @param endDay the last day in the event
   * @param start the start time in the event
   * @param end the end time in the event
   */
  private void drawBoxes(Graphics2D g2d, String startDay,
                         String endDay, LocalTime start, LocalTime end) {
    int s;
    int e;
    if (this.isSaturday) {
      s = this.drawDaySaturday(startDay);
      e = this.drawDaySaturday(endDay);
    }
    else {
      s = this.drawDay(startDay);
      e = this.drawDay(endDay);
    }
    int st = this.drawTime(start);
    int et = this.drawTime(end);
    g2d.setColor(this.color);
    while (s <= e) {
      g2d.fillRect(s, st, this.width / 7,
              (this.height - (this.height / 12)) / 24);
      st += (this.height - (2 * (this.height / 24))) / 24;
      if (st >= et && s == e) {
        s = this.width;
      }
      if (s < e && st >= this.height) {
        s += this.width / 7;
        st = 0;
      }
      if (st >= et) {
        st = et;
      }
    }
    g2d.setColor(Color.BLACK);
  }

  /**
   * The method that gives the x coordinate for the given day.
   * @param day the parameter represented as a day that's passed in
   * @return the x coordinate representing the day
   */
  private int drawDay(String day) {
    int cutoff = this.width / 7;
    switch (day) {
      case "Sunday":
        return 0;
      case "Monday":
        return cutoff;
      case "Tuesday":
        return cutoff * 2;
      case "Wednesday":
        return cutoff * 3;
      case "Thursday":
        return cutoff * 4;
      case "Friday":
        return cutoff * 5;
      case "Saturday":
        return cutoff * 6;
      default:
        throw new IllegalArgumentException("Not a valid day");
    }
  }

  /**
   * The method that gives the x coordinate for the given day.
   * @param day the parameter represented as a day that's passed in
   * @return the x coordinate representing the day
   */
  private int drawDaySaturday(String day) {
    int cutoff = this.width / 7;
    switch (day) {
      case "Saturday":
        return 0;
      case "Sunday":
        return cutoff;
      case "Monday":
        return cutoff * 2;
      case "Tuesday":
        return cutoff * 3;
      case "Wednesday":
        return cutoff * 4;
      case "Thursday":
        return cutoff * 5;
      case "Friday":
        return cutoff * 6;
      default:
        throw new IllegalArgumentException("Not a valid day");
    }
  }

  /**
   * Creates the methods that returns the y coordinate corresponding to the
   * passed in time.
   * @param t the time parameter represented as a local time
   * @return the integer of the y coordinate
   */
  private int drawTime(LocalTime t) {
    int first = this.height / 24;
    int y = (this.height - (2 * first)) / 24;
    return (y * (t.getHour() + 1)) + (int) (((double) y / (double) 60) * t.getMinute());
  }

  /**
   * Returns the list of the events.
   * @return the list of events
   */
  public List<IEvent> getEvents() {
    return this.events;
  }
}
