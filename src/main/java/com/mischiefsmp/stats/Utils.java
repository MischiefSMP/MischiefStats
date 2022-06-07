package com.mischiefsmp.stats;

import java.util.List;

public class Utils {
    public static boolean containsStringIgnoreCase(List<String> list, String string) {
        for(String str : list) {
            if(str.equalsIgnoreCase(string))
                return true;
        }
        return false;
    }
}
