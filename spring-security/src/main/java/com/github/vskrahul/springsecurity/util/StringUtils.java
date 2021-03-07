package com.github.vskrahul.springsecurity.util;

import java.util.Objects;

public class StringUtils {

	public static boolean isEmpty(String s) {
		return Objects.isNull(s) || "".equals(s.trim());
	}
}
