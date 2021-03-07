package com.github.vskrahul.springsecurity.util;

import javax.servlet.http.HttpServletRequest;



public class IpAddressUtil {

	public static String retrieveIpAddress(HttpServletRequest request) {
        String ipAddressArrString = request.getHeader("X-Forwarded-For");
        String ipAddress = retrieveFirstIpAddress(ipAddressArrString);

        if(StringUtils.isEmpty(ipAddress)){
            ipAddress = retrieveFirstIpAddress(request.getRemoteAddr());
        }
        return ipAddress;
    }

    private static String retrieveFirstIpAddress(String ipAddressArrString){
        if (ipAddressArrString != null) {
            ipAddressArrString = ipAddressArrString.replaceAll("\\s+", "");
            String[] ipAddressArr = ipAddressArrString.split(",");
            if(ipAddressArr.length > 0){
                return ipAddressArr[0];
            }
        }

        return null;
    }
}
