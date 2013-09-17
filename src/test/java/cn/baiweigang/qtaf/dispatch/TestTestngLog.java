package cn.baiweigang.qtaf.dispatch;


import org.testng.annotations.Test;

import cn.baiweigang.qtaf.dispatch.log.TestngLog;

public class TestTestngLog {

	@Test
	public void test(){
		TestngLog.CaseStart("Tba框架--dispatch执行测试");
		TestngLog.Log("执行测试");
		TestngLog.CaseEnd("Tba框架--dispatch执行测试");
		
	}
}
