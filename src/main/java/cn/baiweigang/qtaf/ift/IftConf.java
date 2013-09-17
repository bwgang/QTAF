package cn.baiweigang.qtaf.ift;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;


/**
 * 接口全局配置文件
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class IftConf{
		
	public static String RootPath = getRootPath();
	public static String IftPath = getRootPath()+"qtaf/ift/";
	public static String DistPath = getDistPath();
	public static String LibPath = getLibPath();
	public static String JarFile = "";
	
	public static String JavaPath = IftPath+"javaCase/";
//	public static String TestCasePath = IftPath+"excelCase/";
	public static String TemplatePath = IftPath +"template/Template.ftl";	
//	public static String TestngPath = LibPath +"testng-6.8.jar";
	
	public static  String ConfFile = IftPath + "config/IftConf.properties";
	
	/** 
     * 配置代理信息 
     */  	
	public static String ProxyEnable = getPropValue("ProxyEnable","Y");
	public static String ProxyIp = getPropValue("ProxyIp","127.0.0.1");
	public static int PROXY_PORT = Integer.parseInt(getPropValue("ProxyPort","8888"));
        
    //测试用例Excel文件读取 相关配置信息
    public static final int urlRow = 0;
	public static final int urlCol = 1;
	public static final int methodRow = 1;
	public static final int methodCol = 1;
	public static final int cookieRow = 2;
	public static final int cookieCol = 1;
	public static final int argCountRow = 3;
	public static final int argCountCol = 1; 
	public static final int typeRow = 4;
	public static final int paramStartRow = 5;
	public static final int paramStartCol = 3;
	public static final int caseIdCol = 1;
	public static final int isRunCol = 1;
	public static final int secondUrlCol = 2;
	
	//结果比对json串解析方式常量
	public static final int SINGLE_JSON = 1; //单层方式解析json串
	public static final int ALL_JSON = 2;//多层方式解析json串
	
	
	/**
	 * 获取项目根目录，如果是在Tomcat中运行，则返回部署根目录
	 * @return
	 */
	protected   static String getRootPath() {
		String path="";
		if (IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF")!=-1) {
			path=IftConf.class.getClassLoader().getResource("").toString().substring(6, IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF"));
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
		if (IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF")!=-1) {
			path=IftConf.class.getClassLoader().getResource("").toString().substring(6, IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF"));
			path=path+"/WEB-INF/classes/";
		}else {
//			path = GlobalSettings.class.getClassLoader().getResource("").toString();
			String temp=System.getProperty("user.dir");
			temp=temp.replace("\\","/");
			path=temp+"/target/classes/";
		}
		return path;
	}
	
	/**
	 * 获取jar包存放的lib目录，如果是在Tomcat中运行，则返回部署目录下的WEB-INF/lib/  否则返回lib/
	 * @return
	 */
	protected   static String getLibPath() {
		String path="";
		if (IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF")!=-1) {
			path=IftConf.class.getClassLoader().getResource("").toString().substring(6, IftConf.class.getClassLoader().getResource("").toString().indexOf("WEB-INF"));
			path=path+"/WEB-INF/lib/";
		}else {
//			path = GlobalSettings.class.getClassLoader().getResource("").toString();
			String temp=System.getProperty("user.dir");
			temp=temp.replace("\\","/");
			path=temp+"/lib/";
		}
		return path;
	}
	
	private static String getPropValue(String key, String defaultValue) {
		return getProperties().getProperty(key, defaultValue);
	}
	
	private static Properties getProperties() {
		Properties prop = new Properties();
		try {
			//配置文件不存在则创建
			if (!new File(ConfFile).exists() || !new File(TemplatePath).exists()
					)IftConf.writeConf();
			FileInputStream file = new FileInputStream(ConfFile);
			prop.load(file);
			file.close();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return prop;
	}
	
	/**
	 * 如果配置文件不存在，写入
	 * @return
	 */
	public static boolean writeConf() {
		//相应配置文件如果不存在，则创建
		try {
			if (!new File(IftConf.ConfFile).exists()) {
				FileUtils.copyFile(new File(IftConf.class.getResource("IftConf.properties").getFile()), 
						new File(IftConf.ConfFile));
			}
			if (!new File(IftConf.TemplatePath).exists()) {
				FileUtils.copyFile(new File(IftConf.class.getResource("Template.ftl").getFile()), 
					new File(IftConf.TemplatePath));
			}
//			if (!new File(IftConf.TestngPath).exists()) {
//				FileUtils.copyFile(new File(IftConf.class.getResource("testng-6.8.jar").getFile()), 
//					new File(IftConf.TestngPath));
//			}
			
			return true;
		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.print(e.getMessage());
			return false;
		}
	}
}
