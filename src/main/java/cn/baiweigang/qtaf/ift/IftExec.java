/**
 * 
 */
package cn.baiweigang.qtaf.ift;

import java.util.ArrayList;
import java.util.List;

import cn.baiweigang.qtaf.dispatch.DispatchConf;
import cn.baiweigang.qtaf.dispatch.ExecTask;
import cn.baiweigang.qtaf.dispatch.report.TestReport;
import cn.baiweigang.qtaf.dispatch.run.TestRunInfo;
import cn.baiweigang.qtaf.dispatch.testcase.ICase;
import cn.baiweigang.qtaf.ift.testcase.autocreate.IftDataFileCase;

/**
 * 接口测试任务执行类
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a> (bwgang@163.com)<br/>
 *
 */
public class IftExec {

	private ExecTask exec;
	private TestRunInfo runInfo;
	private List<ICase> caseList;
	private IftDataFileCase dataCase;
	public IftExec(){
		//清空临时目录
		DispatchConf.delTmpPath();
		IftConf.delTmpPath();
		
		//检查相关配置，不存在则创建
		IftConf.checkConf();
		 exec = new ExecTask();
		 runInfo = new TestRunInfo();
		 caseList = new ArrayList<>();//用例列表
		 dataCase = new IftDataFileCase();
		 dataCase.setIftTaskName("接口测试");		
	}
	
	/**
	 * 添加用例
	 * @param casePath 用例路径 必填
	 * @param sheetName Excel的sheet表名 可选
	 * @param caseName  用例名称 必填
	 * @param cls		执行用例的类 必填
	 * @param method    类中的方法 必填
	 */
	public void addCase(String casePath, String sheetName, String caseName,
			Class<?> cls,String method){
		dataCase.addCase(casePath, sheetName, caseName, cls, method);
	}
	
	/**
	 * 任务执行
	 * @return TestReport
	 */
	public TestReport run(){
		dataCase.updateXmlFileList();
		caseList.add(dataCase);
		//设置运行配置信息
		runInfo.setTaskName(dataCase.getTaskName());//任务名称
		runInfo.setCaseList(caseList);//用例
		runInfo.setHtmlReportOutPath(dataCase.getHtmlReportPath());//设置测试报告输出目录，
				
		//可选运行参数设置
//		runInfo.setTestng_OutPut(IftConf.IftPath+"testng-out/");//设置TestNG输出目录，--可选
//		runInfo.setHtmlReportOutPath(IftConf.IftPath+"report/");//设置测试报告输出目录，---可选
//		runInfo.setHtmlReportTitle("设置测试报告标题-可选");//设置测试报告标题 ---可选
//		TestngLog.setOutputTestNGLog(false);//不记录TestNG日志，--可选
		
		//执行
		exec.setRunInfo(runInfo);
		return exec.Exec();
	}

	/**
	 * 返回Excel报告的路径
	 * @return String
	 */
	public String getExcelReportPath() {
		return dataCase.getExcelReportPath();
	}
}
