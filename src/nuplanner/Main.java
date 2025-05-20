package nuplanner;

import java.time.LocalTime;
import java.util.List;

import nuplanner.controller.Features;
import nuplanner.controller.NUPlannerController;
import nuplanner.decorator.view.DecoratorPlannerFrame;
import nuplanner.model.CentralSystem;
import nuplanner.model.Day;
import nuplanner.model.Event;
import nuplanner.model.MutableSystems;
import nuplanner.model.User;
import nuplanner.view.IView;
import nuplanner.view.PlannerFrame;

/**
 * The class that starts the program, so you can see the planner.
 */
public class Main {
  /**
   * The function that starts the program.
   * @param args the string array of the arguments
   */
  public static void main(String[] args) {
    MutableSystems model = new CentralSystem();
    User lucia = new User("Prof. Lucia");
    User alex = new User("Student Alex");
    User joomee = new User("Student Joomee");
    Event lecture = new Event("CS3500 Thursday Lecture", Day.SATURDAY,
            LocalTime.of(9,50), Day.SATURDAY, LocalTime.of(11,30),
            false,"Churchill Hall 101", lucia, List.of(alex, joomee));
    model.addUser(lucia);
    model.addUser(alex);
    model.addUser(joomee);
    model.createEvent("Prof. Lucia", lecture);
    PlannerFrame view = new PlannerFrame(model);
    IView dec = new DecoratorPlannerFrame(view);
    String strat = "";
    // gets the arguments from the command line as a strategy
    if (args.length > 0) {
      strat = args[0] + " " + args[1];
      Features controller = new NUPlannerController(dec, model, strat);
      dec.makeVisible();
    }
    else {
      strat = "Any time";
      Features controller = new NUPlannerController(dec, model, strat);
      dec.makeVisible();
    }
  }
}
