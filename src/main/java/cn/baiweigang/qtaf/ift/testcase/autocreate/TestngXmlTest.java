package cn.baiweigang.qtaf.ift.testcase.autocreate;

import java.util.Arrays;

import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

import cn.baiweigang.qtaf.ift.IftConf;
import cn.baiweigang.qtaf.toolkit.file.CompilerUtil;
import cn.baiweigang.qtaf.toolkit.log.Tklogger;

/**
 * 说明：封装TestNG的XmlTest，包括名称、执行线程数等参数设置
 * 			  
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/> 2012-11-14
 *
 */
public class TestngXmlTest {
		
	private static Tklogger log=Tklogger.getLogger(TestngXmlTest.class);//日志记录
	//默认测试集名称
	private final static String XMLTEST_NAME="未命名_"+System.currentTimeMillis();

	//XmlTest名称设置
	private String xmltestname=XMLTEST_NAME;
	
	//测试集，一个TestNG运行实例中可以有多个，目前只使用1个的情况
	private  XmlTest  xmltest;
	
	//测试类的集合，一个测试类对应一个XmlTest，目前只使用1个TestCaseSet情况
	private TestCaseSet testcaseset;


	/**
	 * 更新测试集的信息，更新前会清空原XmlTest信息，并同时更新对应用例集的信息，创建新的.java文件
	 * @return 更新成功返回true，失败返回false
	 */
	public boolean updateTestToXmlTest() {
		
		//设置测试集所在测试套的位置
		this.xmltest=new XmlTest();

		try {
			//先更新用例集信息到TestCaseSet
			updateTestCaseSet();
			
			//属性值设置，线程数等
			this.xmltest.setName(xmltestname);
			this.xmltest.setPreserveOrder("true");
			this.xmltest.setThreadCount(1);
			this.xmltest.setVerbose(2);
			this.xmltest.setPreserveOrder("true");

			//添加包含的Xmlclasses
			XmlClass classe=new XmlClass
					(getTestcaseset().getPackagename()+"."+getTestcaseset().getCasename());
			this.xmltest.setXmlClasses(Arrays.asList(classe));
			log.info("设置测试集："+getXmltestname()+" 信息成功");
			return true;
		} catch (Exception e) {
			log.error("设置测试集："+getXmltestname()+" 信息失败");
			log.error(e.getMessage());
			return false;
		}
	}
	
	
	/**
	 * 设置TestCaseSet
	 * @return 
	 */
	public boolean setTestCaseSet(TestCaseSet testcaseset) {
		if (null==testcaseset) {
			log.error("设置用例集失败，请检查");
			return false;
		}		
		this.testcaseset=testcaseset;
		return true;
	}
	/**
	 * 返回用例集
	 * @return the testcaseset 
	 * 
	 */
	public TestCaseSet getTestcaseset() {
		return testcaseset;
	}
	
	/**
	 * 返回TestNG的Test测试集
	 * @return the xmltest
	 */
	public XmlTest getXmlTest() {		
		return xmltest;		
	}
	/**
	 * @return the xmltestname
	 */
	public String getXmltestname() {
		return xmltestname;
	}

	/**
	 * @param xmltestname the xmltestname to set
	 */
	public boolean setXmltestname(String xmltestname) {
		if (null!=xmltestname && xmltestname.length()>0) {
			this.xmltestname = xmltestname;
			return true;
		}else {
			return false;
		}
	}

	//私有方法
	/**
	 * 更新设置后的分组和依赖信息到用例集,重新生成java文件
	 * @return
	 */
	private boolean updateTestCaseSet() {
		boolean flag=false;
		//创建java文件
		flag=getTestcaseset().creatJavaSrcFile();
		if (flag) {
//			log.info("创建"+getTestcaseset().getCasename()+"对应的.java源文件成功");
		}else{
//			log.info("创建"+getTestcaseset().getCasename()+"对应的.java源文件失败");
			return flag;
		}
		//编译java文件为class文件
		flag=CompilerUtil.dynamicCompiler(getTestcaseset().getJavasavepath()+getTestcaseset().getCasename()+".java", 
				IftConf.DistPath, IftConf.LibPath,IftConf.JarFile);
		if (flag) {
//			log.info("编译"+getTestcaseset().getCasename()+"成功");
		}else{
//			log.info("编译"+getTestcaseset().getCasename()+"失败");
			return flag;
		}
		return flag;
	}
}
