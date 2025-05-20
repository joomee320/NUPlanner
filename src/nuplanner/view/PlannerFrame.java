package nuplanner.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import nuplanner.model.IUser;
import nuplanner.strategy.Strategy;
import nuplanner.controller.Features;
import nuplanner.model.ReadOnlySystems;

/**
 *  Creates the class for the schedule frame.
 */
public class PlannerFrame extends JFrame implements IView {
  private final PlannerPanel panel;
  private final ReadOnlySystems model;
  private final JButton createEvent;
  private final JButton scheduleEvent;
  private final JMenuItem addCalendar;
  private final JMenuItem saveCalendars;
  private final JComboBox<String> users;
  private final List<Features> featuresList;

  /**
   * Creates the constructor for the schedule.
   * @param model the model passed in for the view
   */
  public PlannerFrame(ReadOnlySystems model) {
    super();
    this.model = model;
    this.setSize(900, 700);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.users = this.usersList();
    this.panel = new PlannerPanel(this.model, this.users);
    this.panel.setLayout(new BorderLayout());
    JPanel filePanel = new JPanel();
    filePanel.setLayout(new BorderLayout());
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    this.add(this.panel);
    this.panel.add(filePanel, BorderLayout.NORTH);
    this.panel.add(buttonPanel, BorderLayout.SOUTH);
    JMenu file = new JMenu("File");
    this.addCalendar = new JMenuItem("Add calendar");
    this.saveCalendars = new JMenuItem("Save calendars");
    file.add(this.addCalendar);
    file.add(this.saveCalendars);
    JMenuBar fileBar = new JMenuBar();
    fileBar.add(file);
    filePanel.add(fileBar, BorderLayout.WEST);
    buttonPanel.add(this.users);
    this.createEvent = new JButton("Create event");
    this.createEvent.setActionCommand("Create event");
    buttonPanel.add(this.createEvent);
    this.scheduleEvent = new JButton("Schedule event");
    this.scheduleEvent.setActionCommand("Schedule event");
    buttonPanel.add(this.scheduleEvent);
    featuresList = new ArrayList<>();
  }

  /**
   * Creates the method that adds the users from the model to the list.
   * @return the JComboBox full of the string version of the user
   */
  public JComboBox<String> usersList() {
    JComboBox<String> users = new JComboBox<>();
    users.addItem("<none>");
    for (IUser u: this.model.getAllUsers().values()) {
      users.addItem(u.getUid());
    }
    return users;
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(this.panel,
            error, "Notification", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void refresh() {
    this.revalidate();
    this.repaint();
  }

  @Override
  public void setListener(Features plannerController) {
    this.featuresList.add(plannerController);
    this.panel.addClickListener(plannerController);
    this.addCalendar.addActionListener(e -> {
      String filePath = chooseFile();
      if (filePath != null) {
        plannerController.onUploadXMLFile(filePath);
      }
    });
    this.saveCalendars.addActionListener(e -> {
      String directoryPath = chooseDirectory();
      if (directoryPath != null) {
        plannerController.onSaveSchedules(directoryPath);
      }
    });
    this.createEvent.addActionListener(e -> {
      this.createEventFrame();
      plannerController.onCreateEventFrame();
    });
    this.scheduleEvent.addActionListener(e -> {
      Strategy strat = plannerController.onCreateEventFrame();
      this.scheduleEvent(strat);
    });
    this.users.addItemListener(i -> {
      plannerController.onSwitchUser(Objects.requireNonNull(
              this.users.getSelectedItem()).toString());
    });
  }

  /**
   * The method that chooses the calendar(xml file calendar) from the computer.
   * Basically modified addCalendar method we had for HW6.
   */
  private String chooseFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("XML File", "xml"));
    int value = fileChooser.showOpenDialog(this);
    if (value == JFileChooser.APPROVE_OPTION) {
      String selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
      System.out.println("Selected file path: " + selectedFilePath); // Debugging print statement
      return selectedFilePath;
    }
    return null;
  }

  /**
   * Saves all the calendars to the computer directory selected.
   * Basically modified saveCalendars method we had for HW7.
   */
  private String chooseDirectory() {
    JFileChooser directoryChooser = new JFileChooser();
    directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int value = directoryChooser.showSaveDialog(this);
    if (value == JFileChooser.APPROVE_OPTION) {
      String selectedDirectoryPath = directoryChooser.getSelectedFile().getAbsolutePath();
      System.out.println("Selected file path: " + selectedDirectoryPath);
      return selectedDirectoryPath;
    }
    return null;
  }

  /**
   * This opens the event frame to add a new event when the user clicks create event button
   * in the schedule frame.
   */
  private void createEventFrame() {
    String selectedUserId = Objects.requireNonNull(users.getSelectedItem()).toString();
    EventFrame eventFrame = new EventFrame(this.model, true, selectedUserId);
    for (Features features: this.featuresList) {
      eventFrame.setListener(features);
    }
    eventFrame.makeVisible();
  }

  /**
   * This opens a blank event frame when the schedule event button is clicked.
   */
  private void scheduleEvent(Strategy strategy) {
    String selectedUserId = Objects.requireNonNull(users.getSelectedItem()).toString();
    ScheduleFrame scheduleFrame = new ScheduleFrame(this.model, selectedUserId, strategy);
    for (Features features: this.featuresList) {
      scheduleFrame.setListener(features);
    }
    scheduleFrame.makeVisible();
  }

  public String getUser() {
    return Objects.requireNonNull(this.users.getSelectedItem()).toString();
  }
}
