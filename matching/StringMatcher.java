package kfupm.clinic.matching;

/** Returns true if pattern occurs in text. */
public interface StringMatcher {
    boolean contains(String text, String pattern);
}
