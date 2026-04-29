package kfupm.clinic.matching;

public class KMPMatcher implements StringMatcher {
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



        int[] next = computeNextArray(pattern);
            int n = text.length();
            int m = pattern.length();
            int i = 0;  // text index
            int j = 0;  // pattern index

            while (i < n && j < m) {
                if (j == -1 || text.charAt(i) == pattern.charAt(j)) {
                    i++;
                    j++;
                } else {
                    j = next[j];
                }
            }

            return j == m;
        }


    public static String getBorder(String text) {
        String border = "";
        for(int i = 0; i<text.length();i++){
            String pf = text.substring(0,i);
            String sf = text.substring(text.length()-i);
            if(pf.equals(sf) && pf.length()> border.length()){
                border = pf;
            }


        }


        return border;
    }



    public static int[] computeNextArray(String pattern){
        int[] next = new int[pattern.length()];
        // Complete code here
        for(int i = 0; i<pattern.length();i++){
            int l = getBorder(pattern.substring(0,i)).length();
            if(l == 0 && i == 0){
                next[i]=-1;
                continue;
            }
            next[i] = l;
        }



        return next;
    }
}
