package cn.baiweigang.qtaf.dispatch;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import cn.baiweigang.qtaf.dispatch.util.DisPatchFile;
import cn.baiweigang.qtaf.dispatch.util.DispatchComm;


/**
 * 配置文件
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class DispatchConf {
	
	public static String ClsPath = getClassPath();
	public static String RootPath = getRootPath();
	
	public static String SuitsXmlPath = RootPath+"qtaf/dispatch/suites/";//getPropValue("SuitsXmlPath", "tba/suites/");
	public static String TestNgOutPath = RootPath+"qtaf/dispatch/testng-out/";
	public static String HtmlReportOutPath = RootPath+"qtaf/dispatch/report/"+DispatchComm.getStrRandNum(5)+"/";
	public static String HtmlReportTitle = "测试报告";
	
	public static String TestNGXsltFile = RootPath+"qtaf/dispatch/testng-results.xsl";
	
	private static String getClassPath() {
		return DispatchConf.class.getClassLoader().getResource("").toString();
	}
	private   static String getRootPath() {
		String path="";
		if (DispatchConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF")!=-1) {
			path=DispatchConf.class.getClassLoader().getResource("").toString().substring(6, DispatchConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF"));
			path=path+"/";
		}else {
//			path = GlobalSettings.class.getClassLoader().getResource("").toString();
			String temp=System.getProperty("user.dir");
			temp=temp.replace("\\","/");
			path=temp+"/";
		}
		return path;
	}
	
	private static String getPropValue(String key, String defaultValue) {
		return getProperties().getProperty(key, defaultValue);
	}
	
	private static Properties getProperties() {
		Properties prop = new Properties();
		try {
			FileInputStream file = new FileInputStream("conf.properties");
			prop.load(file);
			file.close();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return prop;
	}
	
	public static boolean writeConf() {
		if (!DisPatchFile.isEmeyxist(DispatchConf.TestNGXsltFile)) {
			try {
				FileUtils.copyFile(new File(DispatchConf.class.getResource("testng-results.xsl").getFile()), new File(DispatchConf.TestNGXsltFile));
				return true;
			} catch (IOException e) {
//				e.printStackTrace();
				System.out.print(e.getMessage());
				return false;
			}
		}
		return false;
	}
}
