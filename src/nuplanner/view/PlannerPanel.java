package nuplanner.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import nuplanner.controller.Features;
import nuplanner.model.Day;
import nuplanner.model.IEvent;
import nuplanner.model.ISchedule;
import nuplanner.model.IUser;
import nuplanner.model.ReadOnlySystems;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Creates the class for the planner panel. This class extends the JPanel class
 * and implements the PlannerPanelView.
 */
public class PlannerPanel extends JPanel implements PlannerPanelView {
  private final ReadOnlySystems model;
  private final JComboBox<String> users;
  private List<IEvent> events;
  private final PaintComponent pc;

  /**
   * Makes the constructor for the planner panel.
   * @param model the model it takes in
   * @param users the list of users in the JComboBox
   */
  public PlannerPanel(ReadOnlySystems model, JComboBox<String> users) {
    super();
    this.model = Objects.requireNonNull(model);
    this.users = Objects.requireNonNull(users);
    this.events = new ArrayList<>();
    this.pc = new PaintComponent(this.model, this.users,
            this.events, false, this.getHeight(), this.getWidth(), Color.RED);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    int x = this.getWidth() / 7;
    int first = this.getHeight() / 24;
    int y = (this.getHeight() - (2 * first)) / 24;
    for (int xCoord = 1; xCoord <= 7; xCoord++) {
      g2d.drawLine(x * xCoord, y, x * xCoord, y * 25);
    }
    for (int yCoord = 1; yCoord <= 24; yCoord++) {
      g2d.drawLine(0, y * yCoord, this.getWidth(), y * yCoord);
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
    int s = this.drawDay(startDay);
    int e = this.drawDay(endDay);
    int st = this.drawTime(start);
    int et = this.drawTime(end);
    int opacity = 160;
    Color red = new Color(255, 0, 0, opacity);
    g2d.setColor(red);
    while (s <= e) {
      g2d.fillRect(s, st, this.getWidth() / 7,
              (this.getHeight() - (this.getHeight() / 12)) / 24);
      st += (this.getHeight() - (2 * (this.getHeight() / 24))) / 24;
      if (st >= et && s == e) {
        s = this.getWidth();
      }
      if (s < e && st >= this.getHeight()) {
        s += this.getWidth() / 7;
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
    int cutoff = this.getWidth() / 7;
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
   * Creates the methods that returns the y coordinate corresponding to the
   * passed in time.
   * @param t the time parameter represented as a local time
   * @return the integer of the y coordinate
   */
  private int drawTime(LocalTime t) {
    int first = this.getHeight() / 24;
    int y = (this.getHeight() - (2 * first)) / 24;
    return (y * (t.getHour() + 1)) + (int) (((double) y / (double) 60) * t.getMinute());
  }

  @Override
  public List<IEvent> getEvents() {
    return this.pc.getEvents();
  }

  /**
   * The color for the point on the panel.
   * @param p the point clicked
   * @return the color it is on
   */
  private Color getColor(Point p) {
    Day startDay = this.reverseDay(p);
    LocalTime startTime = this.reverseTime(p);
    for (IEvent e : this.getEvents()) {
      if (e.getStartDay().equals(startDay)
              && e.getStartTime().getMinute() <= startTime.getMinute()
              && e.getStartTime().getHour() <= startTime.getHour()) {
        return Color.RED;
      }
    }
    return Color.WHITE;
  }

  /**
   * Creates the method that reverses the calculations for the time.
   * @param p the point the event is at
   * @return the time that is returned
   */
  private LocalTime reverseTime(Point p) {
    int cutoff = this.getHeight() / 24;
    int y = (this.getHeight() - (2 * cutoff)) / 24;
    double div = (double) 60 / y;
    int hour = ((p.y - y) / y) - 2;
    int minute = 0;
    if (y > 0) {
      minute = (int) ((p.y - (y * hour)) / div);
    }
    return LocalTime.of(hour, minute);
  }

  /**
   * Reverse the calculations to get the day.
   * @param p the point of the event
   * @return the day it is at
   */
  private Day reverseDay(Point p) {
    int cutoff = this.getWidth() / 7;
    int x = p.x;
    if (x < cutoff) {
      return Day.SUNDAY;
    }
    else if (x < cutoff * 2) {
      return Day.MONDAY;
    }
    else if (x < cutoff * 3) {
      return Day.TUESDAY;
    }
    else if (x < cutoff * 4) {
      return Day.WEDNESDAY;
    }
    else if (x < cutoff * 5) {
      return Day.THURSDAY;
    }
    else if (x < cutoff * 6) {
      return Day.FRIDAY;
    }
    else if (x < this.getWidth()) {
      return Day.SATURDAY;
    }
    else {
      throw new IllegalArgumentException("Not a valid point.");
    }
  }

  /**
   * Gets the event at the point.
   * @param p the point it is at
   * @return the event created
   */
  private IEvent getEvent(Point p) {
    Day startDay = this.reverseDay(p);
    LocalTime startTime = this.reverseTime(p);
    for (IEvent e : this.getEvents()) {
      if (e.getStartDay().equals(startDay)
              && e.getStartTime().getMinute() <= startTime.getMinute()
              && e.getStartTime().getHour() <= startTime.getHour()) {
        return e;
      }
    }
    throw new IllegalStateException("No event located");
  }

  @Override
  public void addClickListener(Features controller) {
    this.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        PlannerPanel panel = PlannerPanel.this;
        Point clicked = e.getPoint();
        Color color = panel.getColor(clicked);
        if (color.equals(Color.RED)) {
          IEvent event = panel.getEvent(clicked);
          controller.handleClick(event);
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
        // do not need to implement
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        // do not need to implement
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        // do not need to implement
      }

      @Override
      public void mouseExited(MouseEvent e) {
        // do not need to implement
      }
    });
  }
}
