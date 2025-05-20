package nuplanner.view;

import nuplanner.controller.Features;

/**
 * Creates the interface for the view of the planner.
 */
public interface IView {
  /**
   * Make the view visible. This is usually called
   * after the view is constructed.
   */
  void makeVisible();

  /**
   * Transmit an error message to the view, in case
   * the command could not be processed correctly.
   * @param error the error message
   */
  void showErrorMessage(String error);

  /**
   * Signal the view to draw itself.
   */
  void refresh();

  /**
   * Sets the action listener to the buttons.
   * @param plannerController the controller that is used to update the model
   */
  void setListener(Features plannerController);
}
