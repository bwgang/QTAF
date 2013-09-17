package cn.baiweigang.qtaf.dispatch.report;

import java.util.ArrayList;

/**
 * 测试任务执行完毕后输出的测试报告摘要信息<br/>
 * 	
 *	@author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 */
public class TestReport {

	private String taskId;//任务ID
	private String taskName;//任务名称
	private String htmlReport;//html报告存储位置
	private String hostsFilePath;//使用的host文件存储位置
	private Long  sumTime;//测试任务执行时长，单位：秒
	
	private TngCount tngSuiteCount;//测试套结果信息
	private ArrayList<TngCount> tngTestCountList;//测试套中所有测试集结果信息
	
	private int resNo;//任务运行结果返回码
	private String resMsg;//任务运行结果返回信息
	
	public String getHtmlReport() {
		return htmlReport;
	}
	public void setHtmlReport(String htmlReport) {
		this.htmlReport = htmlReport;
	}
	public String getHostsFilePath() {
		return hostsFilePath;
	}
	public void setHostsFilePath(String hostsFilePath) {
		this.hostsFilePath = hostsFilePath;
	}
	public TngCount getTngSuiteCount() {
		return tngSuiteCount;
	}
	public void setTngSuiteCount(TngCount tngSuiteCount) {
		this.tngSuiteCount = tngSuiteCount;
	}
	public ArrayList<TngCount> getTngTestCountList() {
		return tngTestCountList;
	}
	public void setTngTestCountList(ArrayList<TngCount> tngTestCountList) {
		this.tngTestCountList = tngTestCountList;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Long getSumTime() {
		return sumTime;
	}
	public void setSumTime(Long sumTime) {
		this.sumTime = sumTime;
	}
	public int getResNo() {
		return resNo;
	}
	public void setResNo(int resNo) {
		this.resNo = resNo;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	
}
