package com.kingbase.ws.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * url 校验
 * 
 * @author ganliang
 *
 */
public class URLUtil {

	public static boolean check(String url) {
		Pattern p = Pattern.compile(
				"^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(url);    
        
        if(m.find()){  
            System.out.println(m.group());  
        } 
		return false;
	}
	
	public static void main(String[] args) {
		check("http://localhost:8081/soap/");
	}
}
