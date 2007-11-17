package org.google.code.translate;

/**
 * This class represents (key;value) pair.
 *
 * @author Alexander Shvets
 * @version 1.0 04/07/2007
 */
public class KeyValuePair {
  private String key;
  private String value;

  /**
   * Creates new pair.
   *
   * @param key   the key
   * @param value the value
   */
  public KeyValuePair(String key, String value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Getter for property 'key'.
   *
   * @return Value for property 'key'.
   */
  public String getKey() {
    return key;
  }

  /**
   * Setter for property 'key'.
   *
   * @param key Value to set for property 'key'.
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * Getter for property 'value'.
   *
   * @return Value for property 'value'.
   */
  public String getValue() {
    return value;
  }

  /**
   * Setter for property 'value'.
   *
   * @param value Value to set for property 'value'.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    return "KeyValuePair (" + key + ", " + value + ")";
  }

}
