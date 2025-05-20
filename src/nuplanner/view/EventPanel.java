package nuplanner.view;

import javax.swing.JPanel;

import nuplanner.model.ReadOnlySystems;

/**
 * EventPanel class extends JPanel and implements EventPanelView to create a custom panel
 * for displaying an Event frame.
 * This panel is specifically designed to handle an event frame where the
 * clients can create, modify and remove events. It is capable of
 * displaying event frame, indicating all the relevant information for an event.
 */
public class EventPanel extends JPanel {
  protected ReadOnlySystems model;

  /**
   * The constructor for the event panel.
   * @param model the passed in model for the view
   */
  public EventPanel(ReadOnlySystems model) {
    super();
    this.model = model;
  }
}
