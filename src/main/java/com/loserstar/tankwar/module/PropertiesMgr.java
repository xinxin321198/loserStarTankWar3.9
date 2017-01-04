package com.loserstar.tankwar.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//配置文件类
/**获取的是class的根路径下的文件 
 * 优点是：可以在非Web应用中读取配置资源信息，可以读取任意的资源文件信息 
 * 缺点：只能加载类classes下面的资源文件。 
 * 如果要加上路径的话：com/test/servlet/jdbc_connection.properties 
 */  
public class PropertiesMgr {
	static Properties properties = new Properties();
	
	static{
		try {
			ClassLoader cl = PropertiesMgr.class.getClassLoader();
			InputStream inputStream = cl.getResourceAsStream("tankWar.properties");
		properties.load(inputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key){
		return properties.getProperty(key);
	}
}
