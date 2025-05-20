package nuplanner.decorator.view;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import nuplanner.controller.Features;
import nuplanner.model.IEvent;
import nuplanner.model.ReadOnlySystems;
import nuplanner.view.PaintComponent;
import nuplanner.view.PlannerPanel;
import nuplanner.view.PlannerPanelView;

/**
 * The class for the planner panel that starts at the Saturday schedule and turns the boxes blue.
 */
public class DecoratorPlannerPanel extends JPanel implements PlannerPanelView {
  private final PlannerPanel delegate;
  private final PaintComponent pc;

  /**
   * The constructor for the decorator planner panel.
   * @param delegate the delegate of the Planner Panel
   * @param model the read only model
   * @param users the list of users
   */
  public DecoratorPlannerPanel(PlannerPanel delegate, ReadOnlySystems model,
                               JComboBox<String> users) {
    this.delegate = delegate;
    Color color = Color.RED;
    this.pc = new PaintComponent(model, users, this.getEvents(),
            true, this.getHeight(), this.getWidth(), color);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    pc.paintComponent((Graphics2D) g);
  }

  @Override
  public void addClickListener(Features controller) {
    this.delegate.addClickListener(controller);
  }

  @Override
  public List<IEvent> getEvents() {
    return this.delegate.getEvents();
  }

  public void refresh() {
    this.repaint();
  }
}
