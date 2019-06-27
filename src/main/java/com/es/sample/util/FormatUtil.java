package com.es.sample.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {

	/**
	 * iso8601형태의 값으로 현재시각을 반환한다.
	 * @return [현재시각 => yyyy-MM-dd'T'HH:mm:ss.SS 형태로 반환]
	 */
	public static String isoTypeNowDate() {
		Date date = new Date(System.currentTimeMillis());
		// Conversion
		SimpleDateFormat sdf;
		sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
		return sdf.format(date);
		
	}
	
}
