package cn.baiweigang.qtaf.toolkit;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


/**
 * 配置文件
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class TkConf  {
	
	public static String RootPath = getRootPath();//根目录
	public static String DistPath = getDistPath();//编译输出目录
	public static String LibPath = getLibPath();//lib文件目录
	
	public static String Log4jConf = RootPath+"qtaf/toolkit/log4j.properties";
	
	/**
	 * 获取项目根目录，如果是在Tomcat中运行，则返回部署根目录
	 * @return
	 */
	protected   static String getRootPath() {
		String path="";
		if (TkConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF")!=-1) {
			path=TkConf.class.getClassLoader().getResource("").toString().substring(6, TkConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF"));
			path=path+"/";
		}else {
			String temp=System.getProperty("user.dir");
			temp=temp.replace("\\","/");
			path=temp+"/";
		}
		return path;
	}
	
	/**
	 * 获取编译输出目录，如果是在Tomcat中运行，则返回部署目录下的WEB-INF/classes/  否则返回bin/
	 * @return
	 */
	protected   static String getDistPath() {
		String path="";
		if (TkConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF")!=-1) {
			path=TkConf.class.getClassLoader().getResource("").toString().substring(6, TkConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF"));
			path=path+"/WEB-INF/classes/";
		}else {
//			path = GlobalSettings.class.getClassLoader().getResource("").toString();
			String temp=System.getProperty("user.dir");
			temp=temp.replace("\\","/");
			path=temp+"/bin/";
		}
		return path;
	}
	
	/**
	 * 获取jar包存放的lib目录，如果是在Tomcat中运行，则返回部署目录下的WEB-INF/lib/  否则返回lib/
	 * @return
	 */
	protected   static String getLibPath() {
		String path="";
		if (TkConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF")!=-1) {
			path=TkConf.class.getClassLoader().getResource("").toString().substring(6, TkConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF"));
			path=path+"/WEB-INF/lib/";
		}else {
//			path = GlobalSettings.class.getClassLoader().getResource("").toString();
			String temp=System.getProperty("user.dir");
			temp=temp.replace("\\","/");
			path=temp+"/lib/";
		}
		return path;
	}

	/**
	 * 如果配置文件不存在，写入
	 * @return
	 */
	public static boolean writeConf() {
		//相应配置文件如果不存在，则创建
		try {
			if (!new File(TkConf.Log4jConf).exists()) {
				FileUtils.copyFile(new File(TkConf.class.getResource("log4j.properties").getFile()), 
						new File(TkConf.Log4jConf));
			}
			return true;
		} catch (IOException e) {
//			e.printStackTrace();
			return false;
		}
	}
}

