package nuplanner.view;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import nuplanner.model.IEvent;
import nuplanner.model.IUser;
import nuplanner.strategy.Strategy;
import nuplanner.controller.Features;
import nuplanner.model.ReadOnlySystems;
import nuplanner.model.User;

/**
 * The class that creates the frame for the scheduling part of the planner.
 */
public class ScheduleFrame extends JFrame implements IView {
  private final Strategy strat;
  private final ReadOnlySystems model;
  private final String selectedUser;
  private JTextField eventField;
  private JTextField locationField;
  private JTextField durationField;
  private JButton scheduleEvent;
  private final JComboBox<String> onlineDropdown;
  private JList<String> usersList;
  private DefaultListModel<String> usersListModel;
  private EventPanel panelNorth;
  private EventPanel panelSouth;
  private EventPanel panelCenter;
  private EventPanel locationPanel;
  private EventPanel durationPanel;
  private EventPanel availUsersPanel;

  /**
   * Creates the constructor for the event frame.
   * @param model the model passed into the view
   */
  public ScheduleFrame(ReadOnlySystems model, String selectedUser, Strategy strat) {
    super();
    this.model = model;
    this.selectedUser = selectedUser;
    this.strat = strat;
    this.setSize(900, 700);
    this.setMinimumSize(new Dimension(300, 300));
    this.setLayout(new BorderLayout());
    this.createPanels();
    this.layoutPanels();
    this.setPanelSizes();
    this.addPanels();
    String[] online = {"Is online", "Not online"};
    JLabel eventName = new JLabel("Event Name:");
    this.panelNorth.add(eventName);
    JLabel location = new JLabel("Location:");
    this.locationPanel.add(location);
    this.onlineDropdown = new JComboBox<>(online);
    JLabel duration = new JLabel("Duration in minutes:");
    this.durationPanel.add(duration);
    JLabel availableUsers = new JLabel("Available Users:");
    this.availUsersPanel.add(availableUsers);
    this.addComponents();
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  /**
   * Creates the panels that are going to be added to the Frame.
   */
  private void createPanels() {
    this.panelNorth = new EventPanel(this.model);
    this.panelCenter = new EventPanel(this.model);
    this.panelSouth = new EventPanel(this.model);
    this.locationPanel = new EventPanel(this.model);
    this.durationPanel = new EventPanel(this.model);
    this.availUsersPanel = new EventPanel(this.model);
  }

  /**
   * Sets the layout for the panels using the BoxLayout.
   */
  private void layoutPanels() {
    this.panelNorth.setLayout(new BoxLayout(this.panelNorth, BoxLayout.PAGE_AXIS));
    this.panelSouth.setLayout(new BoxLayout(this.panelSouth, BoxLayout.LINE_AXIS));
    this.panelCenter.setLayout(new BoxLayout(this.panelCenter, BoxLayout.PAGE_AXIS));
    this.locationPanel.setLayout(new BoxLayout(this.locationPanel, BoxLayout.LINE_AXIS));
    this.durationPanel.setLayout(new BoxLayout(this.durationPanel, BoxLayout.LINE_AXIS));
    this.availUsersPanel.setLayout(new BoxLayout(this.availUsersPanel, BoxLayout.PAGE_AXIS));
  }

  /**
   * Sets the sizes of the panels to be displayed on the Frame.
   */
  private void setPanelSizes() {
    this.panelNorth.setPreferredSize(new Dimension(this.getWidth(),
            this.getHeight() / 10));
    this.panelSouth.setPreferredSize(new Dimension(this.getWidth(),
            this.getHeight() / 10));
    this.panelCenter.setPreferredSize(new Dimension(this.getWidth(),
            (this.getHeight() - (
                    2 * (this.getHeight() / 10))) / 2));
    this.locationPanel.setPreferredSize(new Dimension(this.getWidth(),
            (this.getHeight() - (
                    2 * (this.getHeight() / 10))) / 10));
    this.durationPanel.setPreferredSize(new Dimension(this.getWidth(),
            (this.getHeight() - (
                    2 * (this.getHeight() / 10))) / 10));
  }

  /**
   * Adds the specific panels in the specific location.
   */
  private void addPanels() {
    this.add(this.panelNorth, BorderLayout.NORTH);
    this.add(this.panelCenter, BorderLayout.CENTER);
    this.add(this.panelSouth, BorderLayout.SOUTH);
    this.panelCenter.add(this.locationPanel);
    this.panelCenter.add(this.durationPanel);
    this.panelCenter.add(this.availUsersPanel);
  }

  /**
   * Adds the components that were created and customizes them.
   */
  private void addComponents() {
    this.eventField = new JTextField();
    this.panelNorth.add(this.eventField);
    this.locationPanel.add(this.onlineDropdown);
    this.locationField = new JTextField();
    this.locationPanel.add(this.locationField);
    this.durationField = new JTextField();
    this.durationPanel.add(this.durationField);
    this.usersListModel = new DefaultListModel<>();
    this.usersList = new JList<>(this.usersListModel);
    this.usersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    JScrollPane usersScrollPane = new JScrollPane(this.usersList);
    this.availUsersPanel.add(usersScrollPane);
    this.populateUsersList();
    this.scheduleEvent = new JButton("Schedule Event");
    this.panelSouth.add(this.scheduleEvent);

  }

  /**
   * The method that populates the user list on the frame.
   */
  private void populateUsersList() {
    this.usersListModel.clear();
    for (IUser user : this.model.getAllUsers().values()) {
      this.usersListModel.addElement(user.getUid());
    }
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(this,
            error, "Notification", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void setListener(Features controller) {
    if (this.scheduleEvent != null) {
      this.scheduleEvent.addActionListener(e -> {
        this.printScheduleEvent();
        IEvent event = this.strat.createEventWithDuration(this.eventField.getText(),
                this.convertBoolean(Objects.requireNonNull(
                        this.onlineDropdown.getSelectedItem()).toString()),
                this.locationField.getText(),
                this.durationField.getText(),
                this.selectedUser,
                this.getInvitedUsers());
        if (event == null) {
          this.showErrorMessage("Event cannot be created.");
        }
        controller.onCreateEvent(this.selectedUser, event);
      });

    }
  }

  /**
   * Convert the string to the boolean type in order to compose an event class object.
   * @param online a string that tells if the event is online or not.
   * @return boolean
   */
  private boolean convertBoolean(String online) {
    if (online.equals("Is online")) {
      return true;
    } else if (online.equals("Not online")) {
      return false;
    } else {
      throw new IllegalArgumentException("Not a valid string");
    }
  }

  /**
   * Prints the schedule of the event.
   */
  private void printScheduleEvent() {
    if (this.validateInput()) {
      System.out.println("Create event:");
      this.printEventDetailsForDebug();
    } else {
      this.showErrorMessage("Please complete all fields.");
    }
  }

  /**
   * Prints the event details.
   */
  private void printEventDetailsForDebug() {
    System.out.println("Event name: " + this.eventField.getText());
    System.out.println("Location: " + this.locationField.getText() + " Is online: "
            + onlineDropdown.getSelectedItem());
    System.out.println("Duration: " + this.durationField.getText());
    User hostUser = new User(this.selectedUser);
    System.out.println("Host User: " + hostUser.getUid());
    List<IUser> invitedUsers = this.getInvitedUsers();
    String invitedUsersStr = invitedUsers.stream()
            .map(IUser::getUid)
            .collect(Collectors.joining(", "));

    System.out.println("Invited Users: " + invitedUsersStr);
  }

  /**
   * Gets the invited users.
   * @return a list of the invited users.
   */
  private List<IUser> getInvitedUsers() {
    List<String> selectedUserIds = usersList.getSelectedValuesList();
    List<IUser> invitedUsers = new ArrayList<>();
    for (String userId : selectedUserIds) {
      IUser user = model.getUser(userId);
      if (user != null && !user.getUid().equals(selectedUser)) {
        invitedUsers.add(user);
      }
    }

    return invitedUsers;
  }

  /**
   * Checks if the input is valid.
   * @return a boolean for if the input is valid
   */
  private boolean validateInput() {
    return !this.eventField.getText().trim().isEmpty()
            && !this.locationField.getText().trim().isEmpty()
            && !this.durationField.getText().trim().isEmpty()
            && isDurationValid();
  }

  /**
   * This ensures that an event does not span more than one week in duration, as per the
   * rules set out for the NUPlanner system.
   * @return true if the duration is valid
   */
  private boolean isDurationValid() {
    final long maxDurationMinutes = 6 * 24 * 60 + 23 * 60 + 59;
    try {
      long duration = Long.parseLong(this.durationField.getText().trim());
      return duration >= 0 && duration <= maxDurationMinutes;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
