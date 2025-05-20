package nuplanner.model;

/**
 * The interface for the day of the week enumerations.
 */
public interface DayOfWeek {
  /**
   * Creates the to string method that returns the passed in day of the
   * week in string form.
   * @return a string of the current day
   */
  String toString();

  /**
   * Returns the place of the day in the list.
   * @return the place of the day
   */
  int num();
}
