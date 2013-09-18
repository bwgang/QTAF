package cn.baiweigang.qtaf.dispatch;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import cn.baiweigang.qtaf.toolkit.util.CommUtils;
import cn.baiweigang.qtaf.toolkit.util.FileUtil;


/**
 * 配置文件
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class DispatchConf {
	
	/**
	 * 类文件输出路径
	 */
	public static String ClsPath = getClassPath();
	/**
	 * 项目根目录
	 */
	public static String RootPath = getRootPath();
	/**
	 * suites默认存储目录
	 */
	public static String SuitsXmlPath = RootPath+"qtaf/dispatch/suites/";//getPropValue("SuitsXmlPath", "tba/suites/");
	/**
	 * testng默认输出目录
	 */
	public static String TestNgOutPath = RootPath+"qtaf/dispatch/testng-out/";
	/**
	 * html测试报告默认输出目录
	 */
	public static String HtmlReportOutPath = RootPath+"qtaf/dispatch/report/"+CommUtils.getStrRandNum(5)+"/";
	/**
	 * html测试报告默认标题
	 */
	public static String HtmlReportTitle = "测试报告";
	/**
	 * testNGXslt插件配置文件路径
	 */
	public static String TestNGXsltFile = RootPath+"qtaf/dispatch/testng-results.xsl";
	
	/**
	 * 写配置文件，如果不存在则创建
	 * @return boolean 已存在或创建失败时返回false  创建成功返回true
	 */
	public static boolean writeConf() {
		if (!FileUtil.isEmeyxist(DispatchConf.TestNGXsltFile)) {
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
	
	/**
	 * 删除默认临时目录
	 */
	public static void delTmpPath() {
		// 清空xml文件生成目录
		FileUtil.delAllFile(DispatchConf.SuitsXmlPath);
		// TestNG输出目录
		FileUtil.delAllFile(DispatchConf.TestNgOutPath);
	}
	
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
	
	@SuppressWarnings("unused")
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
	
	
}
