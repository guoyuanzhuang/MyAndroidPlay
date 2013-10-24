package com.gyz.megaeyefamily.util;

public class RandomUtil {

	public static String getRandomNum3bit() {
		String randomStr = "000";
		int randomNum = (int) (Math.random() * 1000 % 1000);
		if (9 < randomNum && randomNum < 100) {
			randomStr = "0" + randomNum;
		} else if (-1 < randomNum && randomNum < 10) {
			randomStr = "00" + randomNum;

		} else {
			randomStr = "" + randomNum;
		}
		return randomStr;
	}

}
