package nuplanner.view;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.time.LocalTime;
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

import nuplanner.controller.Features;
import nuplanner.model.Event;
import nuplanner.model.IEvent;
import nuplanner.model.IUser;
import nuplanner.model.ReadOnlySystems;
import nuplanner.model.User;
import nuplanner.model.Day;

/**
 * The class that creates the frame for the event window of NUPlanner.
 * This class extends the JFrame and implements the EventFrameView and all
 * its classes.
 */
public class EventFrame extends JFrame implements IView {
  private final ReadOnlySystems model;
  private JTextField eventField;
  private JTextField locationField;
  private JTextField startTimeField;
  private JTextField endTimeField;
  private final JButton removeEventButton;
  private JButton createEventButton;
  private final JButton modifyEventButton;
  private final JComboBox<String> onlineDropdown;
  private final JComboBox<String> startDayDropdown;
  private final JComboBox<String> endDayDropdown;
  private JList<String> usersList;
  private DefaultListModel<String> usersListModel;
  private EventPanel panelNorth;
  private EventPanel panelSouth;
  private EventPanel panelCenter;
  private EventPanel locationPanel;
  private EventPanel startDayPanel;
  private EventPanel startTimePanel;
  private EventPanel endDayPanel;
  private EventPanel endTimePanel;
  private EventPanel availUsersPanel;
  private final String selectedUser;
  private IEvent selectedEvent;

  /**
   * Creates the constructor for the event frame that shows up when "create event" button on the
   * planner frame is being clicked.
   *
   * @param model the model passed into the view
   */
  public EventFrame(ReadOnlySystems model, boolean showCreateEventButton,
                    String selectedUser) {
    super();
    this.model = model;
    this.selectedUser = selectedUser;
    this.setSize(900, 700);
    this.setMinimumSize(new Dimension(300, 300));
    this.setLayout(new BorderLayout());
    this.createPanels();
    this.layoutPanels();
    this.setPanelSizes();
    this.addPanels();
    String[] online = {"Is online", "Not online"};
    String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
                        "Friday", "Saturday"};
    JLabel eventName = new JLabel("Event Name:");
    this.panelNorth.add(eventName);
    JLabel location = new JLabel("Location:");
    this.locationPanel.add(location);
    this.onlineDropdown = new JComboBox<>(online);
    JLabel startingDay = new JLabel("Starting Day:");
    this.startDayPanel.add(startingDay);
    this.startDayDropdown = new JComboBox<>(days);
    JLabel startingTime = new JLabel("Starting Time:");
    this.startTimePanel.add(startingTime);
    JLabel endingDay = new JLabel("Ending Day:");
    this.endDayPanel.add(endingDay);
    this.endDayDropdown = new JComboBox<>(days);
    JLabel endingTime = new JLabel("Ending Time:");
    this.endTimePanel.add(endingTime);
    JLabel availableUsers = new JLabel("Available Users:");
    this.availUsersPanel.add(availableUsers);
    this.modifyEventButton = new JButton("Modify event");
    this.panelSouth.add(modifyEventButton);
    this.removeEventButton = new JButton("Remove event");
    this.panelSouth.add(removeEventButton);
    if (showCreateEventButton) {
      this.createEventButton = new JButton("Create event");
      panelSouth.add(this.createEventButton);
    }
    this.addComponents();
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  /**
   * Creates the constructor for the event frame that shows up when the red boxes that represent
   * the event of the user are being clicked.
   *
   * @param model the model passed into the view
   */
  public EventFrame(ReadOnlySystems model, boolean showCreateEventButton,
                    String selectedUser, Event selectedEvent) {
    super();
    this.model = model;
    this.selectedUser = selectedUser;
    this.selectedEvent = selectedEvent;
    this.setSize(900, 700);
    this.setMinimumSize(new Dimension(300, 300));
    this.setLayout(new BorderLayout());
    this.createPanels();
    this.layoutPanels();
    this.setPanelSizes();
    this.addPanels();
    String[] online = {"Is online", "Not online"};
    String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
                        "Friday", "Saturday"};
    JLabel eventName = new JLabel("Event Name:");
    this.panelNorth.add(eventName);
    JLabel location = new JLabel("Location:");
    this.locationPanel.add(location);
    this.onlineDropdown = new JComboBox<>(online);
    JLabel startingDay = new JLabel("Starting Day:");
    this.startDayPanel.add(startingDay);
    this.startDayDropdown = new JComboBox<>(days);
    JLabel startingTime = new JLabel("Starting Time:");
    this.startTimePanel.add(startingTime);
    JLabel endingDay = new JLabel("Ending Day:");
    this.endDayPanel.add(endingDay);
    this.endDayDropdown = new JComboBox<>(days);
    JLabel endingTime = new JLabel("Ending Time:");
    this.endTimePanel.add(endingTime);
    JLabel availableUsers = new JLabel("Available Users:");
    this.availUsersPanel.add(availableUsers);
    this.modifyEventButton = new JButton("Modify event");
    this.panelSouth.add(modifyEventButton);
    this.removeEventButton = new JButton("Remove event");
    this.panelSouth.add(removeEventButton);
    if (showCreateEventButton) {
      this.createEventButton = new JButton("Create event");
      panelSouth.add(this.createEventButton);
    }
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
    this.startDayPanel = new EventPanel(this.model);
    this.startTimePanel = new EventPanel(this.model);
    this.endDayPanel = new EventPanel(this.model);
    this.endTimePanel = new EventPanel(this.model);
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
    this.startDayPanel.setLayout(new BoxLayout(this.startDayPanel, BoxLayout.LINE_AXIS));
    this.startTimePanel.setLayout(new BoxLayout(this.startTimePanel, BoxLayout.LINE_AXIS));
    this.endDayPanel.setLayout(new BoxLayout(this.endDayPanel, BoxLayout.LINE_AXIS));
    this.endTimePanel.setLayout(new BoxLayout(this.endTimePanel, BoxLayout.LINE_AXIS));
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
    this.startDayPanel.setPreferredSize(new Dimension(new Dimension(
            this.getWidth(), (this.getHeight() - (
            2 * (this.getHeight() / 10))) / 10)));
    this.startTimePanel.setPreferredSize(new Dimension(this.getWidth(),
            (this.getHeight() - (
                    2 * (this.getHeight() / 10))) / 10));
    this.endDayPanel.setPreferredSize(new Dimension(new Dimension(
            this.getWidth(), (this.getHeight() - (
            2 * (this.getHeight() / 10))) / 10)));
    this.endTimePanel.setPreferredSize(new Dimension(new Dimension(
            this.getWidth(), (this.getHeight() - (
            2 * (this.getHeight() / 10))) / 10)));
  }

  /**
   * Adds the specific panels in the specific location.
   */
  private void addPanels() {
    this.add(this.panelNorth, BorderLayout.NORTH);
    this.add(this.panelCenter, BorderLayout.CENTER);
    this.add(this.panelSouth, BorderLayout.SOUTH);
    this.panelCenter.add(this.locationPanel);
    this.panelCenter.add(this.startDayPanel);
    this.panelCenter.add(this.startTimePanel);
    this.panelCenter.add(this.endDayPanel);
    this.panelCenter.add(this.endTimePanel);
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
    this.startDayPanel.add(this.startDayDropdown);
    this.startTimeField = new JTextField();
    this.startTimePanel.add(this.startTimeField);
    this.endDayPanel.add(this.endDayDropdown);
    this.endTimeField = new JTextField();
    this.endTimePanel.add(this.endTimeField);
    this.usersListModel = new DefaultListModel<>();
    this.usersList = new JList<>(this.usersListModel);
    this.usersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    JScrollPane usersScrollPane = new JScrollPane(this.usersList);
    this.availUsersPanel.add(usersScrollPane);
    this.populateUsersList();
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

  /**
   * Sets up the action listeners for the buttons.
   */
  @Override
  public void setListener(Features controller) {
    if (this.createEventButton != null) {
      this.createEventButton.addActionListener(e -> {
        printCreateEvent();
        IEvent event = this.getEventDetails();
        controller.onCreateEvent(this.selectedUser, event);
      });
    }
    if (this.removeEventButton != null) {
      this.removeEventButton.addActionListener(e -> {
        printRemoveEvent();
        IEvent event = this.getEventDetails();
        controller.onRemoveEvent(event);
      });
    }
    if (this.modifyEventButton != null) {
      this.modifyEventButton.addActionListener(e -> {
        printModifyEvent();
        IEvent newEvent = this.getEventDetails();
        controller.onModifyEvent(this.selectedUser, this.selectedEvent, newEvent);
      });
    }
  }


  private IEvent getEventDetails() {
    if (!this.validateInput()) {
      this.showErrorMessage("Please complete all fields.");
    }
    String eventName = this.eventField.getText();
    Day startDay = Day.toDay(Objects.requireNonNull(
            this.startDayDropdown.getSelectedItem()).toString());
    LocalTime startTime = this.convertTime(this.startTimeField.getText());
    Day endDay = Day.toDay(Objects.requireNonNull(
            this.endDayDropdown.getSelectedItem()).toString());
    LocalTime endTime = this.convertTime(this.endTimeField.getText());
    boolean isOnline = this.convertBoolean(Objects.requireNonNull(
            this.onlineDropdown.getSelectedItem()).toString());
    String place = this.locationField.getText();
    IUser selectedUser = new User(this.selectedUser);
    List<IUser> invitedUsers = this.getInvitedUsers();

    return new Event(eventName, startDay, startTime, endDay, endTime, isOnline, place,
            selectedUser, invitedUsers);
  }

  /**
   * Convert the string to the local time java class in order to compose an event class object.
   * @param time a string that tells when the event starts and ends.
   * @return LocalTime
   */
  private LocalTime convertTime(String time) {
    int hour;
    int minute;
    if (time.length() >= 4) {
      String[] parts = time.split(":");
      hour = Integer.parseInt(parts[0]);
      minute = Integer.parseInt(parts[1]);
    } else {
      throw new IllegalArgumentException("Time format is not valid. " +
              "Please use HHMM or H:MM format.");
    }
    if (hour == 24 && minute == 0) {
      hour = 0;
    }
    return LocalTime.of(hour, minute);
  }

  /**
   * Convert the string to the boolean type in order to compose an event class object.
   * @param online a string that tells if the event is online or not.
   * @return boolean of the online version
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
   * Checks if the input is valid.
   * @return a boolean for if the input is valid
   */
  private boolean validateInput() {
    return !this.eventField.getText().trim().isEmpty()
            && !this.locationField.getText().trim().isEmpty()
            && !this.startTimeField.getText().trim().isEmpty()
            && !this.endTimeField.getText().trim().isEmpty();
  }

  private void printCreateEvent() {
    if (validateInput()) {
      System.out.println("Create event:");
      printEventDetailsForDebug();
    } else {
      System.out.println("Error: Missing information. Please complete all fields.");
    }
  }

  /**
   * Prints the event being removed.
   */
  private void printRemoveEvent() {
    if (validateInput()) {
      System.out.println("Remove event:");
      printEventDetailsForDebug();
    } else {
      System.out.println("Error: Missing information. Please complete all fields.");
    }
  }

  /**
   * Prints the event being modified.
   */
  private void printModifyEvent() {
    if (validateInput()) {
      System.out.println("Modify event:");
      printEventDetailsForDebug();
    } else {
      System.out.println("Error: Missing information. Please complete all fields.");
    }
  }

  /**
   * Prints the event details for debugging.
   */
  private void printEventDetailsForDebug() {
    System.out.println("Event name: " + eventField.getText());
    System.out.println("Location: " + locationField.getText() + " Is online: "
            + onlineDropdown.getSelectedItem());
    System.out.println("Start Day: " + startDayDropdown.getSelectedItem()
            + " Start Time: " + startTimeField.getText());
    System.out.println("End Day: " + endDayDropdown.getSelectedItem()
            + " End Time: " + endTimeField.getText());
    User hostUser = new User(this.selectedUser);
    System.out.println("Host User: " + hostUser.getUid());
    List<IUser> invitedUsers = this.getInvitedUsers();
    String invitedUsersStr = invitedUsers.stream()
            .map(IUser::getUid)
            .collect(Collectors.joining(", "));
    System.out.println("Invited Users: " + invitedUsersStr);
  }

  /**
   * The list of the invited users.
   * @return the list of users
   */
  private List<IUser> getInvitedUsers() {
    List<String> selectedUserIds = this.usersList.getSelectedValuesList();
    List<IUser> invitedUsers = new ArrayList<>();
    for (String userId : selectedUserIds) {
      IUser user = this.model.getUser(userId);
      if (user != null && !user.getUid().equals(selectedUser)) {
        invitedUsers.add(user);
      }
    }

    return invitedUsers;
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
    this.revalidate();
  }

  /**
   * Sets the values of the event frame to the passed in event.
   * @param e the event passed in
   */
  public void setEverything(IEvent e) {
    this.eventField.setText(e.getName());
    this.locationField.setText(e.getPlace());
    this.startTimeField.setText(e.getStartTime().toString());
    this.endTimeField.setText(e.getEndTime().toString());
    if (e.isOnline()) {
      this.onlineDropdown.setSelectedItem("Is online");
    } else {
      this.onlineDropdown.setSelectedItem("Not online");
    }
  }
}

