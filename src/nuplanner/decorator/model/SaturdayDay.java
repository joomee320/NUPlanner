package nuplanner.decorator.model;

import nuplanner.model.DayOfWeek;

/**
 * The enumeration for a day, where the first day is Saturday instead of Sunday.
 */
public enum SaturdayDay implements DayOfWeek {
  SATURDAY,
  SUNDAY,
  MONDAY,
  TUESDAY,
  WEDNESDAY,
  THURSDAY,
  FRIDAY;

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
   * The method that takes a string and converts it to a day.
   * @param s the string s
   * @return the Saturday Day it returns
   */
  public static SaturdayDay toDay(String s) {
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
