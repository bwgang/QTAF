package cn.baiweigang.qtaf.dispatch;

import cn.baiweigang.qtaf.dispatch.report.TestReport;
import cn.baiweigang.qtaf.dispatch.run.TestRunInfo;
import cn.baiweigang.qtaf.dispatch.run.TestngRunSingle;
import cn.baiweigang.qtaf.toolkit.util.CommUtils;


/**
 * 任务执行入口
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class ExecTask {

	private TestngRunSingle task;
	private TestRunInfo runInfo;
	private TestReport report;
	
	/**
	 * 构造函数
	 */
	public ExecTask() {
		DispatchConf.writeConf();//检查配置文件，不存在则写入
		task    = TestngRunSingle.getInstance();
		report  = new TestReport();
	}
	/**
	 * 任务执行
	 * @return TestReport
	 */
	public TestReport Exec() {
		setResNoAndMsg(-1,"未知错误");
		//传入参数校验
		if (runInfo.getCaseList() == null || runInfo.getCaseList().size()<1) {
			setResNoAndMsg(-100,"待执行的用例不存在");
			return report;
		}
		//执行用例之前清空用例执行相关目录(XML配置文件、TestNG输出目录)
		DispatchConf.delTmpPath();
		Long startTimeMS=System.currentTimeMillis();
		Long startTime=startTimeMS/1000;
		int sumTime=0;//记录等待时长，单位秒
		while (true) {//循环判断开始
			//获取等待的时间
			sumTime=(int) (System.currentTimeMillis()/1000-startTime);
			if (!task.getFlag()) {	//判断当前是否有任务运行		
				try {
					//运行任务 设置任务信息
					task.setRunInfo(this.runInfo);
					report= task.execTask();
					report.setSumTime(System.currentTimeMillis()-startTimeMS);
					return report;//执行完毕后返回测试报告信息
				} catch (Exception e) {
					//运行异常清空目录
					DispatchConf.delTmpPath();
					setResNoAndMsg(-202,"后台运行任务出错，请检查日志信息");
					return report;
				}
			}
			if (sumTime>600) {//设置等待超时时间600秒
//				log.info("已等待"+sumTime+"秒，超时退出");
				setResNoAndMsg(-203,"任务等待超时，已等待"+sumTime+"秒");
				return report;
			}
			//等待10秒后再试试
//			log.info("后台用例执行中。。。等待10秒后再尝试执行");
			CommUtils.sleep(10000);
		}//死循环判断完毕			
	}
	
	/**
	 * 设置任务运行配置信息
	 * @param runInfo
	 */
	public void setRunInfo(TestRunInfo runInfo) {
		this.runInfo = runInfo;
	}
	
	private void setResNoAndMsg(int resNo,String resMsg) {
		this.report.setResNo(resNo);
		this.report.setResMsg(resMsg);
	}
}
