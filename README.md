# NUPlanner: Java-based Calendar & Event Planning Application

## Overview

NUPlanner is a modular, object-oriented scheduling and calendar application written in Java using the MVC (Model-View-Controller) architecture. It enables multiple users to manage personal schedules, create and modify events, and visualize their calendars using both textual and GUI interfaces.

This project was developed with core principles of software engineering: abstraction, modularity, separation of concerns, and testability.

## Features

✅ User schedule creation and event management

✅ Support for overlapping validation and duration constraints

✅ Textual and Swing-based GUI visualizations

✅ File upload/download using XML input/output

✅ Supports command-line arguments for strategy heuristics ("Any Time" vs "Work Hours")

✅ Implements interface-based abstraction (IUser, IEvent, ISchedule) for loosely coupled design

✅ Adapter and integration layer for 3rd-party view compatibility
 
✅ GUI components are resizable and responsive

## Technologies & Concepts

### 🛠 Languages & Frameworks:

Java SE 11+
Java Swing
XML (JAXP)
JUnit (if tested)

### 🎯 Design Patterns:

Model-View-Controller (MVC)
Strategy Pattern
Adapter Pattern
Interface-based abstraction
Architecture

## Model

CentralSystem — Main backend manager handling creation, update, and deletion of users and events. Acts as the logic core.
Event — Encapsulates individual event details (name, time range, location, participants).
Schedule — Maintains per-user event collections and prevents overlapping.
User — Stores unique UID and personal schedule.
XMLParser — Provides reading/writing of schedules to XML format.
Interfaces:

IEvent, IUser, ISchedule — Maintain logic abstraction and decouple implementations from external users.
View

TextualView — Outputs user schedules in a simple terminal-friendly format.
GUI (Swing) — Allows interaction via Event Frames and Planner Panels. Supports visual scheduling, event creation/modification through clicks and form inputs.
Enhanced GUI includes color toggles, resizing, and graphical timeline rendering (via paintComponent).
Controller

Converts user inputs (button clicks, command-line inputs) into model side effects.
Accepts strategy arguments (e.g., Work Hours preference) and applies them through decision logic.
Communicates only through defined interfaces (Features), enhancing loose coupling.
Usage

## Quick Start

// Initialize model
CentralSystem model = new CentralSystem();

// Add new users, create/modify events using model methods

// Text View
ScheduleTextView view = new ScheduleTextView(model);
view.display(); // prints schedule to console

// GUI
Main class parses command-line args and decides the view launch strategy

Run from CLI:

java -jar nuplanner.jar "Work hours"
java -jar nuplanner.jar "Any time"
java -jar nuplanner.jar "provider" // to launch 3rd party's view

Command-line Options

"Any time" — Default view.
"Work hours" — Strategy applies work-week preferences for scheduling.
"provider" — Uses integrated 3rd-party view (some features unsupported due to controller/view coupling).

## Project Structure

src/
└── main/
├── java/
│   └── nuplanner/
│       ├── model/
│       │   ├── CentralSystem.java
│       │   ├── User.java
│       │   ├── Event.java
│       │   ├── Schedule.java
│       │   └── XMLParser.java
│       ├── view/
│       │   ├── ScheduleTextView.java
│       │   ├── NUEventFrame.java
│       │   └── PlannerPanel.java
│       ├── controller/
│       │   └── NUPlannerController.java
└── test/
└── (unit and integration tests if present)

## Design Decisions & Enhancements

- Refactored tightly coupled code via interface injection.
- Adopted interface-based architecture to enforce abstraction.
- Introduced command-line strategy injection for more flexible scheduling.
- Developed adapters for integrating external provider views.
- Resolved Day enum alignment issues for alternate week-start (Saturday-first schedules).
- Integrated exception handling for invalid XML and user input edge cases.

## Known Limitations

Provider’s GUI framework could not be controlled due to tight coupling with their controller.
paintComponent render logic intended for dynamic calendar drawing encountered limitations and is partially implemented but not fully functional.
