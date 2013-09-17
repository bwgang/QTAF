package ${javaInfo.packageName};

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import cn.baiweigang.qtaf.ift.testcase.IftTestCase;
import cn.baiweigang.qtaf.ift.testcase.format.FormatCase;
import cn.baiweigang.qtaf.ift.core.IFtResultInfo;
import cn.baiweigang.qtaf.ift.core.IftLog;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ${clsInfo.importInfo};

/**
 * 自动生成的测试用例
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class ${javaInfo.javaFileName}  {
	
	private ${clsInfo.className} cau;//执行用例的类
	private String PATH_NAME;//用例文件路径
	private String CASE_SHEET_NAME;//用例的Sheet名
	private String REPORT_PATH;	//Excel测试报告输出目录
	private String REPORT_EXCEL_NAME;//EXcel报告文件名	
	private String REPORT_SHEET_NAME;//Excel报告sheet名	
	private List<IftTestCase> allcase ;//所有用例列表

	
	//记录预期值与实际值比对情况
	private List<LinkedHashMap<String,String>> arrres;
	
	@BeforeTest
	public void SetUp() {   
		
		cau=new ${clsInfo.className}();
		allcase=new ArrayList<IftTestCase>() ;		
		PATH_NAME="${javaInfo.caseDataPathName}";
		CASE_SHEET_NAME="${javaInfo.caseDataSheetName}";
		REPORT_PATH="${javaInfo.excelReportPath}";
		REPORT_EXCEL_NAME="${javaInfo.excelReportName}";
		REPORT_SHEET_NAME="${javaInfo.excelReportSheetName}";
		arrres=new ArrayList<LinkedHashMap<String,String>>();
		
		 FormatCase formatcase=new FormatCase();		 
		 formatcase.FormatCaseFromObj(PATH_NAME,CASE_SHEET_NAME);
		 allcase=formatcase.getTestCase();
	 }
	//根据待执行用例列表，生成对应的测试用例java源码
	<#list javaInfo.allCase as testCase>
	@Test(description="${testCase.testPoint}()")
	  public void ${testCase.caseId}(){
			
		IftTestCase testcase=new 	IftTestCase();
		testcase=allcase.get(${testCase_index});
		LinkedHashMap<String,String> result=new LinkedHashMap<String,String>();//记录Excel测试报告
		IFtResultInfo iftResInfo=new IFtResultInfo();
		String httpurl="";//记录请求发送的Url
		String expres="";//期望的结果
		String response="";//请求响应的返回字符串
		String actres="";//过滤后实际的结果
		boolean res=false;//记录比对结果
		
		String temp="";
		temp=Thread.currentThread().getStackTrace()[1].getClassName();
		temp=temp.substring(temp.lastIndexOf(".")+1, temp.length());
		
		//用例开始

		IftLog.CaseStart("测试集："+temp+"--的用例："+testcase.getCaseMap().get("CaseID")+"--"+testcase.getCaseMap().get("TestPoint"));
		
		//发起http请求，
		iftResInfo=cau.${clsInfo.method}(testcase);
		
		//获取返回结果信息和发起的http信息
		response=iftResInfo.getResponseInfo().getResBodyInfo();
		httpurl=iftResInfo.getResponseInfo().getHttpUrl();
		
		//获取比对结果信息
		res=iftResInfo.getCompareRes();
		actres=iftResInfo.getActRes();
		expres=iftResInfo.getExpRes();
		
		//记录执行结果到ArrayList,写Excel报告要用到
		result.put("CaseID", testcase.getCaseMap().get("CaseID"));
		result.put("TestPoint", testcase.getCaseMap().get("TestPoint"));
		result.put("ExpRes", expres);
		result.put("ActRes", actres);
		result.put("ResponseRes", response);
		result.put("Httpurl", httpurl);
		
		//比对结果
		if (res==true) {
			result.put("ExcResult", "Pass");
		}
		else {
			result.put("ExcResult", "Fail");
		}
		arrres.add(result);
		
		//写入TestNG日志
		IftLog.Log("此用例预期结果为："+expres);
		IftLog.Log("此用例实际结果为："+actres);
		IftLog.Log("此用例请求返回的完整字符串为："+response);
		IftLog.Log("此用例发送的请求URL为："+httpurl);
		IftLog.Log("执行结果为："+res);

		//用例结束
		IftLog.CaseEnd("测试集："+temp+"--的用例："+testcase.getCaseMap().get("CaseID")+"--"+testcase.getCaseMap().get("TestPoint"));
	 
		//比较结果记入TestNG断言中
		org.testng.Assert.assertTrue(res, "实际结果:"+actres+"  -预期结果:"+expres);
		//org.testng.Assert.assertEquals(actres, expres,res);
	}
	</#list>
	
	@AfterTest
	public void CloseConn() {   
		String temp="";
		cau.closeConn();
		
		//执行结果写入excel
		cau.CreatReportExcel(REPORT_PATH,REPORT_EXCEL_NAME,REPORT_SHEET_NAME,arrres);

		//记录到TestNG日志
		IftLog.Log("所有用例执行完毕");
		IftLog.Log("共验证检查点数为："+arrres.size());
		
		 //此测试套执行完毕记入TestNG日志
		temp=Thread.currentThread().getStackTrace()[1].getClassName();
		temp=temp.substring(temp.lastIndexOf(".")+1, temp.length());
		IftLog.Log("********************测试套：【"+temp+"】执行完毕**************************");
	 }
}
