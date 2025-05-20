package nuplanner.model;

/**
 * Creates the enumeration for the seven days of the week.
 */
public enum Day implements DayOfWeek {
  SUNDAY,
  MONDAY,
  TUESDAY,
  WEDNESDAY,
  THURSDAY,
  FRIDAY,
  SATURDAY;

  @Override
  public String toString() {
    switch (this) {
      case SUNDAY:
        return "Sunday";
      case MONDAY:
        return "Monday";
      case TUESDAY:
        return "Tuesday";
      case WEDNESDAY:
        return "Wednesday";
      case THURSDAY:
        return "Thursday";
      case FRIDAY:
        return "Friday";
      case SATURDAY:
        return "Saturday";
      default:
        throw new IllegalStateException("Not a valid day:" + this);
    }
  }

  /**
   * Converts the given string to the day.
   * @param s the string s
   * @return the converted day
   */
  public static Day toDay(String s) {
    switch (s) {
      case "Sunday":
        return SUNDAY;
      case "Monday":
        return MONDAY;
      case "Tuesday":
        return TUESDAY;
      case "Wednesday":
        return WEDNESDAY;
      case "Thursday":
        return THURSDAY;
      case "Friday":
        return FRIDAY;
      case "Saturday":
        return SATURDAY;
      default:
        throw new IllegalArgumentException("Not a valid string");
    }
  }

  @Override
  public int num() {
    return this.ordinal();
  }
}
