package cn.baiweigang.qtaf.dispatch.run;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestContext;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import cn.baiweigang.qtaf.dispatch.DispatchConf;
import cn.baiweigang.qtaf.dispatch.report.ExportReportHtml;
import cn.baiweigang.qtaf.dispatch.report.TestReport;
import cn.baiweigang.qtaf.dispatch.report.TngCount;
import cn.baiweigang.qtaf.dispatch.testcase.ICase;
import cn.baiweigang.qtaf.dispatch.util.DispatchComm;
import cn.baiweigang.qtaf.dispatch.util.DisPatchFile;




/**
 * 执行testng，
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/> 2012-10-29
 *
 */
public class TestngRun {
	
	private TestNG tng;//运行TestNG
	private TestListenerAdapter listener;//运行的监听器

	private TestRunInfo runInfo;
	private List<String> xmlFileList;
	
	private TestReport testReport;
	
	/*
	 * 构造函数 初始化
	 */
	public  TestngRun() {
		tng=new TestNG();
		listener=new TestListenerAdapter();//定义监听器类型
		tng.addListener(listener);
		xmlFileList=new ArrayList<>();//记录测试使用的xml文件路径列表
		testReport=new TestReport();//记录测试报告测试报告信息
		runInfo=new TestRunInfo();
	}
	
	public void setRunInfo(TestRunInfo runInfo) {
		this.runInfo=runInfo;
		for (ICase icase : this.runInfo.getCaseList()) {
			addXmlFileList(icase.getCaseList());
		}
	}
	
	/**
	 * 执行用例
	 * @return
	 */
	public boolean run() {
		if (getXmlFileList().size()<1) {
			return false;
		}
		//运行相关参数配置
		tng.setOutputDirectory(getTestNgOut());
		
		try {
			tng.setTestSuites(getXmlFileList());
			tng.run();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		//记录测试报告摘要
		createTestReport();
		//输出html测试报告
		
		if (!createHtmlReport()) {
			this.testReport.setResNo(1);
			this.testReport.setResMsg("任务执行成功，转换Html格式报告出错");
		}else{
			this.testReport.setResNo(0);
			this.testReport.setResMsg("任务执行成功");
		}
		
		return true;
	}

	/**
	 * 获取测试报告
	 * @return
	 */
	public TestReport getTestReport() {
		return this.testReport;
	}
	
	//私有方法
	private String getTestNgOut(){
		String out= this.runInfo.getTestng_OutPut();
		if (out == null || out.length()<1) {
			return DispatchConf.TestNgOutPath;//默认的TestNG输出目录
		}
		if (!out.endsWith("/")) {
			out=out+"/";
		}
		return out;
	}
	
	private String getTaskName() {
		if (this.runInfo.getTaskName() == null || this.runInfo.getTaskName().length()<1) {
			return "未命名测试任务"+DispatchComm.getRandomStr(5);//默认的TestNG输出目录
		}
		return this.runInfo.getTaskName();
	}
	
	private String getHtmlReportOut(){
		if (this.runInfo.getHtmlReportOutPath() == null || this.runInfo.getHtmlReportOutPath().length()<1) {
			return DispatchConf.HtmlReportOutPath;//默认的html报告输出目录
		}
		return this.runInfo.getHtmlReportOutPath();
	}
	
	private String getHtmlReportTitle(){
		if (null == this.runInfo.getHtmlReportTitle() || this.runInfo.getHtmlReportTitle().length()<1) {
			return  DispatchConf.HtmlReportTitle;
		}
		return this.runInfo.getHtmlReportTitle();
	}
	
	private List<String> getXmlFileList() {
		return xmlFileList;
	}

	private void addXmlFileList(List<String> xmlFileList) {
		if (null==xmlFileList) return;
		for (String xmlFile : xmlFileList) {
			addXmlFile(xmlFile);
		}
	}
	
	private boolean addXmlFile(String xmlPathName){
		if (null==xmlPathName){
			return false;
		}
		if (!DisPatchFile.getExtensionName(xmlPathName).equals("xml")) xmlPathName=xmlPathName+".xml";
		
		if (DisPatchFile.isEmeyxist(xmlPathName)) {
			this.xmlFileList.add(xmlPathName);
			return true;
		}else{
			return false;
		}
	}


	private boolean createHtmlReport() {
		boolean res=false;
		res = ExportReportHtml.createHtmlReport(getTestNgOut()+"testng-results.xml", getHtmlReportOut(),getHtmlReportTitle());
		if (res) this.testReport .setHtmlReport(getHtmlReportOut());
		return res;
	}
	
	/**
	 * 从监听器中获取需要的报告信息
	 * @param listener
	 */
	private void createTestReport() {
		ArrayList<TngCount> testCountList=new ArrayList<TngCount>();;
		TngCount tngCount=new TngCount();
		tngCount.setName(getTaskName());
		tngCount.setFailed(this.listener.getFailedTests().size());
		tngCount.setPassed(this.listener.getPassedTests().size());
		tngCount.setSkipped(this.listener.getSkippedTests().size());
		this.testReport.setTngSuiteCount(tngCount);
		List<ITestContext> testContextList=this.listener.getTestContexts();
		for (int i = 0; i < testContextList.size(); i++) {
			tngCount=new TngCount();
			tngCount.setName(testContextList.get(i).getName());
			tngCount.setSuiteName(testContextList.get(i).getSuite().getName());
			tngCount.setFailed(testContextList.get(i).getFailedTests().size());
			tngCount.setPassed(testContextList.get(i).getPassedTests().size());
			tngCount.setSkipped(testContextList.get(i).getSkippedTests().size());
			
			testCountList.add(tngCount);
		}
		this.testReport .setTaskName(getTaskName());
		this.testReport .setTngTestCountList(testCountList);
	}

	
}
