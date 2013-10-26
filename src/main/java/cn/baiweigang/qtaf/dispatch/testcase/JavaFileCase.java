package cn.baiweigang.qtaf.dispatch.testcase;

import java.util.Arrays;
import java.util.List;

import cn.baiweigang.qtaf.toolkit.util.CommUtils;

/**
 * .java文件类型的测试用例
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a>(bwgang@163.com)<br/>
 *
 */
public class JavaFileCase extends SuperCase{
	private CreateXmlFile createXmlFile;
	/**
	 * 构造函数
	 */
	public JavaFileCase(){
		super();
		createXmlFile = new CreateXmlFile();
	}
	/**
	 * 获取用例列表，返回的是xml文件路径信息
	 * @return List<String>
	 */
	public List<String> getCaseList(){
		String xmlFilePath = createXmlFile.getXmlFilePath();
		if (null!=xmlFilePath){
			return Arrays.asList(xmlFilePath);
		}else{
			return null;
		}
		
	}
	/**
	 * 添加用例
	 * @param cls
	 */
	public void addCase(Class<?> cls){
		if (null==cls) return ;
		String caseName=cls.getSimpleName();
		addCase(caseName, cls);
	}
	/**
	 * 添加用例
	 * @param caseName
	 * @param cls
	 */
	public void addCase(String caseName,Class<?> cls) {
		if (null==cls ) return;
		if (null==caseName || caseName.length()<1) {
			caseName="未命名测试用例"+CommUtils.getRandomStr(5);
		}
		createXmlFile.addJavaCase(caseName, cls);
	}

	/**
	 * 设置测试套名称 未设置使用默认名称 "未命名测试用例"+5随机字符
	 * @param suiteName
	 */
	public void setSuiteName(String suiteName) {
		createXmlFile.setSuiteName(suiteName);
	}
	
	/**
	 * 设置生成的xml文件存放文件夹  未设置使用默认 qtaf/dispatch/suites/
	 * @param suiteName
	 */
	public void setXmlFileFolder(String xmlFileFolder) {
		createXmlFile.setXmlFileFolder(xmlFileFolder);
	}
	
}
