package cn.baiweigang.qtaf.dispatch.testcase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import cn.baiweigang.qtaf.dispatch.DispatchConf;
import cn.baiweigang.qtaf.dispatch.util.DispatchComm;
import cn.baiweigang.qtaf.dispatch.util.DisPatchFile;


/**
 * .java文件类型的测试用例
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class JavaFileCase extends SuperCase{
	private final String XMLPATHNAME=DispatchConf.SuitsXmlPath;
	private XmlSuite suite;
	private String suiteName;
	public JavaFileCase(){
		super();
		suite=new XmlSuite();
		setSuiteName("未命名测试任务"+DispatchComm.getRandomStr(5));
	}
	public List<String> getCaseList(){
		if (this.suite.getTests().size()<1) return null;
		return Arrays.asList(createXmlFile());
	}
	public void addCase(Class<?> cls){
		if (null==cls) return ;
		String caseName=cls.getSimpleName();
		addCase(caseName, cls);
	}
	public void addCase(String caseName,Class<?> cls) {
		if (null==cls ) return;
		if (null==caseName || caseName.length()<1) {
			String nameTmp="未命名测试用例"+DispatchComm.getRandomStr(5);
			addClassToXmlTest(cls.getName(), nameTmp);
			return;
		}
		addClassToXmlTest(cls.getName(), caseName);
	}
	private void addTest(XmlTest test){
		if (null==test) return;
		this.suite.addTest(test);
	}

	public void setSuiteName(String suiteName) {
		if (null!=suiteName && suiteName.length()>0) this.suiteName = suiteName;
		this.suite.setName(this.suiteName);
	}
	private void addClassToXmlTest(String pkgAndClsName,String testName) {
		if (null==pkgAndClsName || null==testName) return;
		XmlTest xmltest=new XmlTest();
		XmlClass classe=new XmlClass(pkgAndClsName);
		xmltest.setName(testName);
		xmltest.setClasses(Arrays.asList(classe));
		addTest(xmltest);
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
		if (DisPatchFile.writeString(arr, this.XMLPATHNAME+this.suiteName+".xml", "UTF-8")){
			return this.XMLPATHNAME+this.suiteName+".xml";
		}else {
			return "";
		}
	}
}
