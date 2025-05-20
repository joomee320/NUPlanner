package nuplanner.decorator.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

import nuplanner.controller.Features;
import nuplanner.model.CentralSystem;
import nuplanner.view.IView;
import nuplanner.view.PlannerFrame;
import nuplanner.view.PlannerPanel;

/**
 * The class that is the planner frame but with the new Button implemented.
 */
public class DecoratorPlannerFrame extends JFrame implements IView {
  private final PlannerFrame delegate;
  private final JButton toggleColor;
  private boolean blueColor;
  private Color color;

  /**
   * The constructor for the planner frame.
   * @param delegate the planner frame delegate
   */
  public DecoratorPlannerFrame(PlannerFrame delegate) {
    super();
    this.delegate = delegate;
    CentralSystem cs = new CentralSystem();
    DecoratorPlannerPanel panel = new DecoratorPlannerPanel(new PlannerPanel(cs,
            this.delegate.usersList()), cs, this.delegate.usersList());
    panel.setLayout(new BorderLayout());
    this.add(panel);
    this.toggleColor = new JButton("Toggle host color");
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(this.toggleColor);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    this.delegate.add(panel, BorderLayout.SOUTH);
    this.blueColor = false;
    this.color = Color.RED;
  }

  @Override
  public void makeVisible() {
    this.delegate.makeVisible();
  }

  @Override
  public void showErrorMessage(String error) {
    this.delegate.showErrorMessage(error);
  }

  @Override
  public void refresh() {
    this.delegate.refresh();
    this.revalidate();
    this.repaint();
  }

  @Override
  public void setListener(Features plannerController) {
    this.delegate.setListener(plannerController);
    this.toggleColor.addActionListener(e -> {
      if (this.blueColor) {
        this.blueColor = false;
        this.color = Color.RED;
      }
      else {
        this.blueColor = true;
        this.color = Color.BLUE;
      }
      plannerController.onSwitchUser(this.delegate.getUser());
    });
  }
}
