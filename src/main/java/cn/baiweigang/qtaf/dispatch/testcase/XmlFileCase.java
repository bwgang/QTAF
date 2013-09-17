package cn.baiweigang.qtaf.dispatch.testcase;

import java.util.ArrayList;
import java.util.List;

/**
 * XML文件类型的测试用例
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class XmlFileCase extends SuperCase{

	public  XmlFileCase() {
		super();
		xmlPathNameList=new ArrayList<>();
	}
	public  XmlFileCase(List<String> xmlPathNameList) {
		super();
		xmlPathNameList=new ArrayList<>();
		this.xmlPathNameList=xmlPathNameList;
	}
	public  XmlFileCase(String xmlPathName) {
		super();
		xmlPathNameList=new ArrayList<>();
		if (null==xmlPathName)return;
		this.xmlPathNameList.add(xmlPathName);
	}

	public void addXmlCase(List<String> xmlPathNameList) {
		this.xmlPathNameList.addAll(xmlPathNameList);
	}
	public void addXmlCase(String xmlPathName) {
		if (null==xmlPathName)return;
		this.xmlPathNameList.add(xmlPathName);
	}
	
}
