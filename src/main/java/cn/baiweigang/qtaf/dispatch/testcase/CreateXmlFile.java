package cn.baiweigang.qtaf.dispatch.testcase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import cn.baiweigang.qtaf.dispatch.DispatchConf;
import cn.baiweigang.qtaf.toolkit.util.CommUtils;
import cn.baiweigang.qtaf.toolkit.util.FileUtil;
import cn.baiweigang.qtaf.toolkit.util.LogUtil;


/**
 * 根据java文件输出xml文件
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a>(bwgang@163.com)<br/>
 *
 */
public class CreateXmlFile {
	private static LogUtil log=LogUtil.getLogger(CreateXmlFile.class);//日志记录
	
	private XmlSuite suite;
	private String xmlFileFolder;
	private String suiteName;

	/**
	 * 构造函数
	 */
	public CreateXmlFile(){
		suite=new XmlSuite();
		setSuiteName("测试套件"+CommUtils.getRandomStr(5));
		setXmlFileFolder(DispatchConf.SuitsXmlPath);
	}
	
	public String getXmlFilePath(){
		if (suite.getTests().size()<1) {
			log.error("未添加任何测试集，未生成xml文件");
			return null;
		}else{
			return createXmlFile();
		}
	}
	
	/**
	 * 添加java用例
	 * @param cls
	 */
	public void addJavaCase(Class<?> cls){
		if (null==cls) return ;
		String caseName=cls.getSimpleName();
		addJavaCase(caseName, cls);
	}
	/**
	 * 添加java用例
	 * @param caseName 用例名称
	 * @param cls 类
	 */
	public void addJavaCase(String caseName,Class<?> cls) {
		if (null==cls ) return;
		if (null==caseName || caseName.length()<1) {
			String nameTmp="未命名测试集"+CommUtils.getRandomStr(5);
			addClassToXmlTest(cls.getName(), nameTmp);
			return;
		}
		addClassToXmlTest(cls.getName(), caseName);
	}

	/**
	 * 设置测试套名称
	 * @param suiteName
	 */
	public void setSuiteName(String suiteName) {
		if (null!=suiteName && suiteName.length()>0) this.suiteName = suiteName;
		suite.setName(this.suiteName);
	}
	
	public void setXmlFileFolder(String xmlFileFolder) {
		this.xmlFileFolder = xmlFileFolder;
	}
	private void addClassToXmlTest(String pkgAndClsName,String testName) {
		if (null==pkgAndClsName || null==testName) return;
		XmlTest xmltest=new XmlTest();
		XmlClass classe=new XmlClass(pkgAndClsName);
		xmltest.setName(testName);
		xmltest.setClasses(Arrays.asList(classe));
		addTest(xmltest);
	}

	private void addTest(XmlTest test){
		if (null==test) return;
		suite.addTest(test);
	}
	
	private String createXmlFile(){
		List<String> arr=new ArrayList<String>();
		String xml=this.suite.toXml();
		String xx[]=xml.split("\n");
		
		for (int i = 0; i < xx.length; i++) {
			if (xx[i].indexOf("verbose")!=-1) {
				String temp=xx[i].trim();
				xx[i]=temp.substring(0, temp.indexOf("verbose")+12)+" preserve-order=\"true\" "
				+temp.substring(temp.indexOf("verbose")+12, temp.length());
			}
			arr.add(xx[i]);
		}
		if (FileUtil.writeString(arr, getXmlFileFolder()+getSuiteName()+".xml", "UTF-8")){
			return getXmlFileFolder()+getSuiteName()+".xml";
		}else {
			return "";
		}
	}

	private String getSuiteName(){
		return this.suiteName;
	}
	
	private String getXmlFileFolder() {
		return xmlFileFolder;
	}
}
