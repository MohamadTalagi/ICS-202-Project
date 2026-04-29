package kfupm.clinic.matching;


public class NaiveMatcher implements StringMatcher {
    @Override
    public boolean contains(String text, String pattern) {
        if (text == null || pattern == null) {
            return false;
        }

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        if (pattern.length() == 0) {
            return true;
        }

        if (pattern.length() > text.length()) {
            return false;
        }

        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            boolean found = true;

            for (int j = 0; j < pattern.length(); j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return true;
            }
        }

        return false;
    }
}
