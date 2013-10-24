package cn.baiweigang.qtaf.dispatch.log;

import org.testng.Reporter;

import cn.baiweigang.qtaf.toolkit.util.CommUtils;



/**
 * 封装TestNG log记录
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a>(bwgang@163.com)<br/>
 *
 */
public class TestngLog {
	private static boolean OutputTestNGLog=true;//是否输出到TestNG Log日志
	/**
	 * 说明：记录用例开始，写入TestNG日志
	 * 
	 * @param testCaseName 用例名称
	 */
	public static void CaseStart(String testCaseName) {
		TestNGLog("[" + CommUtils.getNowTime() + "]" + "--用例-------【" + testCaseName
				+ "】----------开始执行---------------");
	}

	/**
	 * 说明：记录用例结束，写入TestNG日志
	 * 
	 * @param testCaseName 用例名称
	 */
	public static void CaseEnd(String testCaseName) {
		TestNGLog("[" + CommUtils.getNowTime() + "]" + "--用例-------【" + testCaseName
				+ "】----------执行完毕---------------");
		TestNGLog(
				"///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
	}

	/**
	 * 说明：把指定信息写入TestNG日志
	 * @param str 字符串
	 */
	public static void Log(String str) {
		TestNGLog("[" + CommUtils.getNowTime() + "]" + "---" + str + "\n");
	}

	/**
	 * 设置是否输出TestNG执行记录到TestNG日志中
	 * @param outputTestNGLog 默认true
	 */
	public static void setOutputTestNGLog(boolean outputTestNGLog) {
			OutputTestNGLog = outputTestNGLog;
	}
	
	private static void TestNGLog(String info) {
		if (OutputTestNGLog) {
			Reporter.log(info,false);
		}
	}
}
