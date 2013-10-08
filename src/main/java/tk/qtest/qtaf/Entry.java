package tk.qtest.qtaf;


import java.util.ArrayList;
import java.util.List;

import tk.qtest.qtaf.demo.DemoCasesUtils;

import cn.baiweigang.qtaf.dispatch.ExecTask;
import cn.baiweigang.qtaf.dispatch.report.TestReport;
import cn.baiweigang.qtaf.dispatch.run.TestRunInfo;
import cn.baiweigang.qtaf.dispatch.testcase.ICase;
import cn.baiweigang.qtaf.ift.IftConf;
import cn.baiweigang.qtaf.ift.testcase.autocreate.IftDataFileCase;


public class Entry {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//检查相关配置，不存在则创建
		IftConf.checkConf();
		
		
		//依赖的jar文件路径信息必须设置 否则不执行
		if (!IftConf.updateJarFile(args)) return;
		
		//其它设置  
		IftConf.ProxyEnable="N";//不启用代理，默认为启用
		
		ExecTask exec = new ExecTask();
		TestRunInfo runInfo = new TestRunInfo();
		List<ICase> caseList = new ArrayList<>();//用例列表
		
		//接口数据格式用例			
		IftDataFileCase dataCase = new IftDataFileCase();
		dataCase.setIftTaskName("接口测试Demo");
		//Excel文件  sheet表名 执行用例的类  类中的方法  
		dataCase.addCase(IftConf.RootPath+"demo.xlsx","Post","PayDemo",DemoCasesUtils.class,"DemoMethod1");
		dataCase.addCase(IftConf.RootPath+"demo.xlsx","Get","PayDemo2",DemoCasesUtils.class,"DemoMethod2");
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
		TestReport report=exec.Exec();
		
		//输出执行结果
		System.out.print("任务执行结果："+report.getResMsg()+"\n");
		if (report.getResNo()>-1) {
			System.out.print("任务名称："+runInfo.getTaskName()+"\n");
			System.out.print("任务执行时间："+report.getSumTime()+"毫秒\n");
			System.out.print("Html报告："+report.getHtmlReport()+"/index.html\n");
			System.out.print("Excel报告："+dataCase.getExcelReportPath());
		}

	}

}
