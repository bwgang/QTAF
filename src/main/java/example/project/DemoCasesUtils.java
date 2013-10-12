package example.project;

import java.util.TreeMap;

import cn.baiweigang.qtaf.ift.core.CasesUtils;
import cn.baiweigang.qtaf.ift.core.IFtResultInfo;
import cn.baiweigang.qtaf.ift.testcase.IftTestCase;
import cn.baiweigang.qtaf.ift.util.CommonSign;
import cn.baiweigang.qtaf.toolkit.httpclient.ResponseInfo;

/**
 * 说明：测试支付平台项目的接口执行类，继承自基础类CasesUtils
 * 
 * @author 
 *
 */
public class DemoCasesUtils extends CasesUtils{

	public IFtResultInfo DemoMethod1(IftTestCase testcase)  {
		//暂停100毫秒
//		BaseTools.sleep(100);
		//设置用例编码--可选
		testcase.setEnCoding(DemoConf.ENCODING);
		//设置用例使用的header信息--可选
		testcase.setHeaderMap(new TreeMap<String,String>());
		//更新用例的签名计算、url参数、form参数、header参数--必须
		testcase=updateAllToListForCase(testcase, DemoConf.GetPara, DemoConf.PostPara, DemoConf.HeardPara);
		//更新用例参数值，针对rand等特殊标识处理--必须
		testcase=updateAllParaForCase(testcase,10);
		//更新用例签名值--可选
		testcase=updateSignValueForCase(testcase, CommonSign.signMethodThird(getSignMap(testcase), DemoConf.SecretKey));
		//发起请求
		ResponseInfo resInfo = execResquest(testcase);
		//获取预期结果
		String expRes=testcase.getCaseMap().get("Expres");
		if (expRes=="" || null==expRes) {
			expRes="预期结果为空";
		}
		//获取处理后的实际结果 目前只支持json\xml格式
		String actRes= resInfo.getResBodyInfo();
		//设置json解析方式--可选
//		int parseJson = 2;
		return getIFtResultInfo(resInfo, expRes, actRes);
	}
	
	public IFtResultInfo DemoMethod2(IftTestCase testcase)  {
		IFtResultInfo iftResInfo=new IFtResultInfo();
		ResponseInfo resInfo = new ResponseInfo(); 
		//执行用例
		//。。。。
		//最终获取如下格式结果即可
		resInfo.setHttpUrl("this a demo2");
		resInfo.setResBodyInfo("demo");
		iftResInfo.setResponseInfo(resInfo);
		iftResInfo.setActRes("demo");
		iftResInfo.setExpRes("demo");
		iftResInfo.setCompareRes(true);
		return iftResInfo;
	}

}

 
	
	

