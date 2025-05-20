package nuplanner.view;

import java.awt.Graphics;
import java.util.List;

import nuplanner.controller.Features;
import nuplanner.model.IEvent;

/**
 * Creates the interface for the schedule panel view.
 */
public interface PlannerPanelView {
  /**
   * The method that paints the lines onto the panel.
   * @param g the <code>Graphics</code> object to protect
   */
  void paintComponent(Graphics g);

  /**
   * This is a method that handles when the panel is clicked.
   * @param controller the controller passed into the listener
   */
  void addClickListener(Features controller);

  /**
   * The method for the events in the planner panel.
   * @return the list of the events
   */
  List<IEvent> getEvents();
}
