package cn.baiweigang.qtaf.ift;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import cn.baiweigang.qtaf.dispatch.DispatchConf;
import cn.baiweigang.qtaf.toolkit.TkConf;
import cn.baiweigang.qtaf.toolkit.util.FileUtil;


/**
 * 接口全局配置文件
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a> (bwgang@163.com)<br/>
 *
 */
public class IftConf{
	/**
	 * 项目根目录	
	 */
	public static final String RootPath = getRootPath();
	/**
	 * 接口ift配置等文件默认保存目录
	 */
	public static  String IftPath = getRootPath()+"qtaf/ift/";
	/**
	 * 默认编译输出目录
	 */
	public static  String DistPath = getDistPath();
	/**
	 * 默认lib目录
	 */
	public static  String LibPath = getLibPath();
	/**
	 * jar文件目录  maven
	 */
	public static String JarFile = "";
	/**
	 * 默认生成的java文件存放目录
	 */
	public static String JavaPath = IftPath+"javaCase/";
	/**
	 * 默认的模板文件
	 */
	public static String TemplatePath = IftPath +"template/Template.ftl";	
	/**
	 * 默认的配置文件
	 */
	public static  String ConfFile = IftPath + "config/IftConf.properties";
	
	//代理相关配置信息
		/** 
	     * 是否启用代理配置 
	     */  	
		public static String ProxyEnable = getPropValue("ProxyEnable","Y");
		/**
		 * 代理IP
		 */
		public static String ProxyIp = getPropValue("ProxyIp","127.0.0.1");
		/**
		 * 代理端口
		 */
		public static int PROXY_PORT = Integer.parseInt(getPropValue("ProxyPort","8888"));
        
    //测试用例Excel文件读取 相关配置信息
		/**
		 * url所在行数
		 */
	    public static final int urlRow = 0;
	    /**
	     * url所在列数
	     */
		public static final int urlCol = 1;
		/**
		 * httpMethod所在行数
		 */
		public static final int methodRow = 1;
		/**
		 * httpMethod所在列数
		 */
		public static final int methodCol = 1;
		/**
		 * 全局cookie所在行数
		 */
		public static final int cookieRow = 2;
		/**
		 * 全局cookie所在列数
		 */
		public static final int cookieCol = 1;
		/**
		 * 参数签名计算参数个数所在行数
		 */
		public static final int argCountRow = 3;
		/**
		 * 参数签名计算参数个数所在列数
		 */
		public static final int argCountCol = 1; 
		/**
		 * 标题所在行
		 */
		public static final int typeRow = 4;
		/**
		 * 用例数据开始行数
		 */
		public static final int paramStartRow = 5;
		/**
		 * 用例数据开始列表
		 */
		public static final int paramStartCol = 3;
		/**
		 * caseId所在列数
		 */
		public static final int caseIdCol = 1;
		/**
		 * 是否执行属性所在列数
		 */
		public static final int isRunCol = 1;
		/**
		 * secondUrl所在列数
		 */
		public static final int secondUrlCol = 2;
	
	/**
	 * json默认解析方式 单层解析
	 */
	public static final int parseJson = 1; 
	
	
	/**
	 * 获取项目根目录，如果是在Tomcat中运行，则返回部署根目录
	 * @return String
	 */
	private   static String getRootPath() {
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
	 * @return String
	 */
	private   static String getDistPath() {
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
	 * @return String
	 */
	private   static String getLibPath() {
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
			prop.load(new InputStreamReader(file,"UTF-8"));
			file.close();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return prop;
	}
	
	/**
	 * 如果配置文件不存在，写入
	 * @return boolean
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
			
			return true;
		} catch (IOException e) {
			System.out.print(e.getMessage());
			return false;
		}
	}

	/**
	 * 设置依赖的jar文件路径信息 maven
	 * @param args
	 * @return boolean 设置成功返回true
	 */
	public static boolean updateJarFile(String[] args) {
		
		if (IftConf.JarFile.length()>0) {
			return true;//JarFile已设置
		}
		
		if (null!=args && args.length>0) {//设置JarFile，同时写入文件
			IftConf.JarFile=args[0];
			FileUtil.writeString(args[0], IftConf.IftPath+"JarFile", "UTF-8");
		}else{
			if (FileUtil.isEmeyxist(IftConf.IftPath+"JarFile")) {//已存在则读取
				IftConf.JarFile=FileUtil.readToString(IftConf.IftPath+"JarFile", "UTF-8");
			}else{
				System.out.print("在eclipse中，第一次需要以maven方式执行");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查相关配置，不存在则创建
	 */
	public static void checkConf() {
		TkConf.writeConf();
		DispatchConf.writeConf();
		IftConf.writeConf();
	}
	
}
