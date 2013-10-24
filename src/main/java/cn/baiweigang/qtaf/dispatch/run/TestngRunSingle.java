package cn.baiweigang.qtaf.dispatch.run;

import cn.baiweigang.qtaf.dispatch.report.TestReport;

/**
 * 说明：封装用例执行 单例模式
 * 
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a>(bwgang@163.com)<br/> 2012-12-04
 * 
 */
public class TestngRunSingle {
	private static TestngRunSingle single;
	private static boolean Flag;//是否运行任务标识，为true时有任务在运行，false时无任务运行
	private TestRunInfo runInfo ;//任务名称
	private TestReport testReport;//执行完毕后，返回的测试报告信息
		
	//设置为单例模式
	private TestngRunSingle() 
	{
		setFlag(false);
		init();//第一次创建时，初始化属性
	}

	/**
	 * 运行前初始化相关信息
	 */
	private void init(){
		runInfo=new TestRunInfo();
		testReport=new TestReport();
	}
	
	/**
	 * 获取类的实例
	 * @return TestngRunSingle
	 */
	public synchronized  static TestngRunSingle getInstance() {
	      if (single == null) {  
	          single = new TestngRunSingle();
	      }  
	     return single;
	}
	
	/**
	 * 设置任务信息
	 * @param runInfo 任务配置信息
	 */
	public void setRunInfo(TestRunInfo runInfo) {
		// 设置任务信息
		this.runInfo=runInfo;
	}
	
	/**
	 * 获取当前任务运行标识
	 * @return boolean
	 */
	public  boolean getFlag() {
		return Flag;
	}
	
	/**
	 * 说明：执行任务返回测试报告信息
	 * @return  TestReport
	 */
	public TestReport execTask() {
		setFlag(true);
		if(doTask()){
			
		}else{
			this.testReport.setResNo(-1);
			this.testReport.setResMsg("未知错误，见TestNG日志记录");
		}
		setFlag(false);
		return this.testReport;		
	}
	
	//内部方法
	private static void setFlag(boolean flag) {
		Flag = flag;
	}

	/**
	 * 说明：执行测试过程
	 * 
	 * @return  boolean
	 */
	private boolean doTask() {
		boolean res=false;
		try {
			// 测试TestngRun
			TestngRun testngRun = new TestngRun();
			testngRun.setRunInfo(runInfo);
			res=testngRun.run();
			testReport = testngRun.getTestReport();
			testngRun = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
