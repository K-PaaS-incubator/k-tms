package com.kglory.tms.web.util;

public class PolicyUtils {	
	
	public static long getLResponse(boolean response, boolean block) {
		if (response == true && block == true) {
            return 268566530L;
        } else if (response == true && block == false) {
        	return 131074L;
        } else if (response == false && block == true) {
        	return 268566529L;
        }
		return 131073L;
	}
}
