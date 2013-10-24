package cn.baiweigang.qtaf.ift.testcase.autocreate;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.xml.XmlSuite;

import cn.baiweigang.qtaf.toolkit.util.FileUtil;
import cn.baiweigang.qtaf.toolkit.util.LogUtil;

/**
 * 封装TestNg的XmlSuite
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a>(bwgang@163.com)<br/>
 *
 */
public class TestngXmlSuite {

	private static LogUtil log = LogUtil.getLogger(TestngXmlSuite.class);//日志记录
	//默认测试套名称
	private final static String XMLSUITE_XML_FILE_NAME="alltest.xml";
	private  XmlSuite  xmlsuite;
	//测试集，一个TestNG测试套中可以有多个测试集
	private  List<TestngXmlTest> testngxmltestlist;
	private String xmlfilepath="";
	private String xmlname=XMLSUITE_XML_FILE_NAME;
	
	/**
	 * 更新测试集信息
	 * @return boolean
	 */
	public boolean updateTestToXmlSuite() {
		try {
			//清空测试套
			this.xmlsuite=new XmlSuite();
			//属性值设置，线程数等
			this.xmlsuite.setName(xmlname);
			this.xmlsuite.setPreserveOrder("true");
			this.xmlsuite.setThreadCount(1);
			this.xmlsuite.setVerbose(2);
			XmlSuite.DEFAULT_PRESERVE_ORDER="true";			
			
			//添加包含的XmlTest
			for (int i = 0; i < this.testngxmltestlist.size(); i++) {
				this.testngxmltestlist.get(i).updateTestToXmlTest();//更新生成xml文件
				this.xmlsuite.addTest(this.testngxmltestlist.get(i).getXmlTest());
			}
			
			//其它设置
			
			log.info("设置测试套："+xmlname+" 信息成功");
			return  true;
		} catch (Exception e) {
			
			log.error("设置测试套："+xmlname+" 信息失败");
			log.error(e.getMessage());
			return false;
		}
		
	}
	/**
	 * 添加测试集
	 * @param testngxmltest
	 * @return boolean
	 */
	public boolean addXmlTest(TestngXmlTest testngxmltest) {
		if (null == testngxmltest) {
			log.info("添加的测试集不能为null");
			return false;
		}
		
		if (null==this.testngxmltestlist) this.testngxmltestlist=new ArrayList<TestngXmlTest>();
			
		this.testngxmltestlist.add(testngxmltest);
//		log.info("添加测试集信息："+testngxmltest.getXmltestname()+" 成功");
		return true;
	}
	
	/**
	 * @param testngxmltestlist the xmltest to set
	 * @return boolean 设置成功返回true，失败返回false
	 */
	public boolean setXmlTestList(List<TestngXmlTest> testngxmltestlist) {
		if (null!=testngxmltestlist) {
			this.testngxmltestlist = testngxmltestlist;
			return true;
		}else {
			return false;
		}
		
	}
	
	/**
	 * 创建测试套对应的xml配置文件
	 * @return  boolean 创建成功返回true，失败返回false
	 */
	public boolean createXmlFile() {		
		try {

			String xml=this.xmlsuite.toXml();
			String xx[]=xml.split("\n");
			ArrayList<String> arr=new ArrayList<String>();
			for (int i = 0; i < xx.length; i++) {
				if (xx[i].indexOf("verbose")!=-1) {
					String temp=xx[i].trim();
					xx[i]=temp.substring(0, temp.indexOf("verbose")+12)+" preserve-order=\"true\" "
					+temp.substring(temp.indexOf("verbose")+12, temp.length());
				}
				arr.add(xx[i]);
			}	
			boolean res = FileUtil.writeString(arr,this.xmlfilepath+"/"+this.xmlname+".xml", "UTF-8");
//			if (res) log.info("创建测试套："+getXmlName()+" 对应的xml文件 "+this.xmlfilepath+"/"+this.xmlname+".xml 成功");
			return res;
			
		} catch (Exception e) {
			log.error("创建测试套："+getXmlName()+" 对应的xml文件 "+this.xmlfilepath+"/"+this.xmlname+".xml 失败");
			log.error(e.getMessage());
			return false;
		}
	}


	/**
	 * 设置xml文件路径
	 * @param xmlfilepath
	 * @return boolean 设置成功返回true
	 */
	public boolean setXmlFilePath(String xmlfilepath) {
		File filepath=new File(xmlfilepath);
		try {
			if (!filepath.exists()) {
				filepath.mkdirs();
			}
			this.xmlfilepath = xmlfilepath;
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}

		
	}

	/**
	 * 设置xml名称
	 * @param xmlname
	 * @return boolean 设置成功返回true
	 */
	public boolean setXmlName(String xmlname) {
		try {
			if (xmlname.length()>0) {
				this.xmlname=xmlname;				
			}
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}
	
	public String getXmlFilePath() {
		return this.xmlfilepath;
	}
	
	public String getXmlName() {
		return this.xmlname;
	}
	
	public XmlSuite getXmlSuite() {
		return this.xmlsuite;
	}
}
