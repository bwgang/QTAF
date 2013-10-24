package cn.baiweigang.qtaf.toolkit;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


/**
 * 配置文件
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a> (bwgang@163.com)<br/>
 */
public class TkConf  {
	
	/**
	 * 根目录
	 */
	public static String RootPath = getRootPath();
	/**
	 * log4j配置文件目录
	 */
	public static String Log4jConf = RootPath+"qtaf/toolkit/log4j.properties";

	/**
	 * 如果配置文件不存在，写入
	 * @return boolean
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

	/**
	 * 获取项目根目录，如果是在Tomcat中运行，则返回部署根目录
	 * @return String
	 */
	private   static String getRootPath() {
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
	
}

