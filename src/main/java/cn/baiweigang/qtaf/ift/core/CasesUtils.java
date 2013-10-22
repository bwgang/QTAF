package cn.baiweigang.qtaf.ift.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import cn.baiweigang.qtaf.ift.IftConf;
import cn.baiweigang.qtaf.ift.testcase.IftTestCase;
import cn.baiweigang.qtaf.ift.util.ExportReportExcel;
import cn.baiweigang.qtaf.toolkit.httpclient.HttpUtil;
import cn.baiweigang.qtaf.toolkit.httpclient.ResponseInfo;
import cn.baiweigang.qtaf.toolkit.util.CommUtils;
import cn.baiweigang.qtaf.toolkit.util.LogUtil;
import cn.baiweigang.qtaf.toolkit.util.StringUtil;

/**
 * 说明：用例执行的公共处理类，执行粒度是一个TestCase实例 即Excel用例文件中的一条用例，或Mysql数据库中的一条用例
 * 执行后，返回的信息为map表，目前存储get请求的url串或post请求的form
 * 
 * 注意：当不满足新加入项目需要时，创建一子类，重写其中的方法，不允许在此类中修改
 * 
 * @author bwgang 2013-01-10
 * 
 */
public class CasesUtils {
	/**
	 * 日志记录
	 */
	protected static LogUtil log = LogUtil.getLogger(CasesUtils.class);
	/**
	 * get方法所需参数拼接后的字符串
	 */
	protected String getUrl;
	/**
	 * post方法所需参数拼接后的字符串
	 */
	protected String postUrl;
	/**
	 * 发起请求的地址
	 */
	protected String httpUrl;
	/**
	 * header的参数值对
	 */
	protected TreeMap<String, String> headersMap;
	/**
	 * httpurl的参数值对
	 */
	protected LinkedHashMap<String, String> urlParaMap;
	/**
	 * post提交时form的参数值对
	 */
	protected LinkedHashMap<String, String> formParaMap;
	/**
	 * http请求执行类
	 */
	protected HttpUtil sendRequestCore;
	
	/**
	 * 无参构造函数 说明：创建httpclient连接初始化
	 */
	public CasesUtils() {
		this.getUrl = "";
		this.postUrl = "";
		this.httpUrl = "";
		this.headersMap = new TreeMap<String, String>();
		this.urlParaMap = new LinkedHashMap<String, String>();
		this.formParaMap = new LinkedHashMap<String, String>();
		if (IftConf.ProxyEnable.equals("Y")) {
			sendRequestCore = new HttpUtil(IftConf.ProxyIp,IftConf.PROXY_PORT);
		} else {
			sendRequestCore = new HttpUtil();
		}
	}

	/**
	 * 发起请求，目前只支持get和post两个方法，待扩展
	 * 
	 * @param testCase
	 * @return ResponseInfo
	 */
	public ResponseInfo execResquest(IftTestCase testCase) {
		ResponseInfo resInfo=new ResponseInfo();
		// 设置发起请求时使用的编码
		this.sendRequestCore.setCharset(testCase.getEnCoding());
		boolean flag = false;
		// 获取发起请求的http地址
		flag = this.updateHttpUrl(testCase);
		if (!flag) {
			log.info("发起http请求时，获取http地址失败");
			resInfo.setErrMsgInfo("发起http请求时，获取http地址失败");
			return resInfo;
		}
		// 获取发起请求的url参数信息
		flag = this.updateUrlPara(testCase);
		if (!flag) {
			log.info("发起http请求时，获取url参数信息失败");
			resInfo.setErrMsgInfo("发起http请求时，获取url参数信息失败");
			return resInfo;
		}
		// 获取发起请求的post参数信息
		flag = updateFormParaMap(testCase);
		if (!flag) {
			log.info("发起http请求时，获取post参数信息失败");
			resInfo.setErrMsgInfo("发起http请求时，获取post参数信息失败");
			return resInfo;
		}
		// 获取发起请求的headers信息
		flag = updateHeadersMap(testCase);
		if (!flag) {
			log.info("发起http请求时，获取headers信息失败");
			resInfo.setErrMsgInfo("发起http请求时，获取headers信息失败");
			return resInfo;
		}
		try {
			// 发起请求
			if (testCase.getHttpMethod().toLowerCase().equals("get")) {
				resInfo= sendRequestCore.get(headersMap, httpUrl + getUrl);
				resInfo.setHttpUrl(httpUrl + getUrl);
			} else if (testCase.getHttpMethod().toLowerCase().equals("post")) {
				resInfo= sendRequestCore.post(headersMap, httpUrl+getUrl, postUrl);
				resInfo.setHttpUrl("post请求的url信息：" + httpUrl + postUrl);
			} else {// 待扩展
				
			}
		} catch (Exception e) {
			log.error("发送http请求异常");
			log.error("httpurl:" + httpUrl);
			log.error("getUrl:" + getUrl);
			log.error("postUrl:" + postUrl);
			log.error(e.getMessage());
			resInfo.setErrMsgInfo("发送http请求异常,请查看执行日志记录");
			return resInfo;
		}

		// 存储执行结果和拼接的url
		return resInfo;
	}

	
	/**
	 * 返回结果比对后的信息
	 * @param resInfo http请求后返回的信息
	 * @param expRes 从用例中读取的预期结果
	 * @param actRes 格式化后的实际结果 目前只支持json和xml格式
	 * @return IFtResultInfo
	 */
	public IFtResultInfo getIFtResultInfo(ResponseInfo resInfo,String expRes,String actRes) {
		return getIFtResultInfo(resInfo, expRes,actRes, 1);
	}
	/**
	 * 返回结果比对后的信息
	 * @param resInfo http请求后返回的信息
	 * @param expRes 从用例中读取的预期结果
	 * @param actRes 格式化后的实际结果 目前只支持json和xml格式
	 * @param parseJson json的解析方式 默认为单层解析
	 * @return IFtResultInfo
	 */
	public IFtResultInfo getIFtResultInfo(ResponseInfo resInfo,String expRes,String actRes,int parseJson) {
		IFtResultInfo iFtResultInfo =  new IFtResultInfo();
		//结果比对处理类 目前只能处理json和xml格式
		CompareResult comresult=new CompareResult();
		boolean compareRes=false;
		
		//http请求执行成功后，才进行比对
		if (resInfo.getErrMsgInfo() == null) {
			//结果比对
			compareRes=comresult.getCompareResult(expRes,actRes,parseJson);//单层方式解析json串
			actRes=comresult.getClearActres();
			expRes=comresult.getClearExpres();
		}else{
			resInfo.setResBodyInfo(resInfo.getErrMsgInfo());
			actRes="";
		}

		//处理返回结果
		iFtResultInfo.setResponseInfo(resInfo);
		iFtResultInfo.setCompareRes(compareRes);
		iFtResultInfo.setActRes(actRes);
		iFtResultInfo.setExpRes(expRes);
		return iFtResultInfo;
	}
	/**
	 * 关闭httpclient连接
	 */
	public void closeConn() {
		this.sendRequestCore.close();
	}

	/**
	 * 更新httpurl
	 * @param testCase
	 * @return boolean 更新成功返回true
	 */
	protected boolean updateHttpUrl(IftTestCase testCase) {
		String httpUrlTmp = testCase.getUrl();
		if (null == httpUrlTmp || httpUrlTmp.length() < 1) {
			log.error("用例请求的http地址为空，请检查");
			return false;
		}
		String secondurl = "";
		try {
			secondurl = testCase.getCaseMap().get("secondurl");
		} catch (Exception e) {
			secondurl = "";
		}
		if (null != secondurl && !secondurl.equals("")) {
			if (secondurl.equals("rand"))
				secondurl = CommUtils.getRandomStr(5);
			/*if (!secondurl.matches("[0-9a-zA-Z.]*"))
				secondurl = CommUtils.urlEncode(secondurl,testCase.getEnCoding());*/
			httpUrlTmp += secondurl;
		}
		this.httpUrl = httpUrlTmp;
		return true;
	}

	/**
	 * 拼接get方法所需的字符串 中文按照enCoding字段指定编码进行urlEncode转码
	 * @param testCase
	 * @return boolean 拼接成功返回true
	 */
	protected boolean updateUrlPara(IftTestCase testCase) {
		this.urlParaMap = new LinkedHashMap<String, String>();
		String urlTmp = "";
		if (null == testCase.getParalist()) {
			log.error("传入的httpurl的参数名列表为null，请检查");
			return false;
		}
		if (testCase.getParalist().size() < 1) {
			urlTmp = "";// 参与get参数拼接的个数为0
		} else {
			for (int i = 0; i < testCase.getParalist().size(); i++) {
				String value = testCase.getCaseMap().get(testCase.getParalist().get(i));
				if (value.toLowerCase().equals("null")) {
					// 参数值为null时，不参与拼接
				} else if (value.matches("[0-9a-zA-Z.]*")) {
					this.urlParaMap.put(testCase.getParalist().get(i), value);
					urlTmp += testCase.getParalist().get(i) + "=" + value + "&";
				} else {
					// 中文按照enCoding字段指定编码进行urlEncode转码
					value = CommUtils.urlEncode(value, testCase.getEnCoding());
					this.urlParaMap.put(testCase.getParalist().get(i), value);
					urlTmp += testCase.getParalist().get(i) + "=" + value + "&";
				}
			}
			// 去掉最后一个&符号
			if(!StringUtil.IsNullOrEmpty(urlTmp)){
				urlTmp = urlTmp.substring(0, urlTmp.length() - 1);
			}
			this.getUrl = urlTmp;
		}
		return true;
	}

	/**
	 * 拼接post方法所需的字符串 中文按照enCoding字段指定编码进行urlEncode转码
	 * @param testcase
	 * @return boolean 拼接成功返回true
	 */
	protected boolean updateFormParaMap(IftTestCase testcase) {
		this.formParaMap = new LinkedHashMap<String, String>();
		String postTmp = "";
		if (null == testcase.getFormlist()) {
			log.error("传入的form参数名列表为null，请检查");
			return false;
		}
		if (testcase.getFormlist().size() < 1) {
			postTmp = "";// 参与post参数拼接的个数为0
		} else {
			for (int i = 0; i < testcase.getFormlist().size(); i++) {
				String value = testcase.getCaseMap().get(testcase.getFormlist().get(i));
				if (value.toLowerCase().equals("null")) {
					// 参数值为null时，不参与拼接
				} else if (value.matches("[0-9a-zA-Z.]*")) {
					this.formParaMap.put(testcase.getFormlist().get(i), value);
					postTmp += testcase.getFormlist().get(i) + "=" + value + "&";
				} else {
					// 中文按照enCoding字段指定编码进行urlEncode转码
					value = CommUtils.urlEncode(value, testcase.getEnCoding());
					this.formParaMap.put(testcase.getFormlist().get(i), value);
					postTmp += testcase.getFormlist().get(i) + "=" + value + "&";
				}
			}
			// 去掉最后一个&符号
			if(!StringUtil.IsNullOrEmpty(postTmp)){
				postTmp = postTmp.substring(0, postTmp.length() - 1);
			}
			this.postUrl = postTmp;
		}
		return true;
	}

	/**
	 * 往headers中添加参数信息
	 * 
	 * @param testcase
	 * @return boolean 添加成功返回true
	 */
	protected boolean updateHeadersMap(IftTestCase testcase) {
		if (null != testcase.getHeaderMap()) {
			this.headersMap = testcase.getHeaderMap();
		} else {
			this.headersMap = new TreeMap<String, String>();
		}
		if (null == testcase.getHeaderlist()) {
			log.error("传入的headers参数名列表为null，请检查");
			return false;
		}
		for (int i = 0; i < testcase.getHeaderlist().size(); i++) {
			String value = testcase.getCaseMap().get(
					testcase.getHeaderlist().get(i));
			if (value.toLowerCase().equals("null")) {
				// 参数值为null时，不参与拼接
			} else if (value.matches("[0-9a-zA-Z.]*")) {
				this.headersMap.put(testcase.getHeaderlist().get(i), value);
			} else {
				// 中文按照enCoding字段指定编码进行urlEncode转码
				value = CommUtils.urlEncode(value, testcase.getEnCoding());
				this.headersMap.put(testcase.getHeaderlist().get(i), value);
			}
		}
		// cookie信息添加到headers中
		if (null != testcase.getCookie() && testcase.getCookie().length() > 0) {
			headersMap.put("Cookie", testcase.getCookie());
		}
		return true;
	}

	// 用例处理相关的公共方法
	/**
	 * 获取参与签名计算的键值对map表
	 * @param testcase
	 * @return TreeMap<String, String> 参与签名计算的键值对map表
	 */
	protected static TreeMap<String, String> getSignMap(IftTestCase testcase) {
		TreeMap<String, String> signMap = new TreeMap<String, String>();
		if (null == testcase.getSignlist()) {
			log.error("传入的签名参数名列表为null，请检查");
			return signMap;
		}
		for (int i = 0; i < testcase.getSignlist().size(); i++) {
			String value = testcase.getCaseMap().get(
					testcase.getSignlist().get(i));
			if (value.toLowerCase().equals("null")) {
				// 参数值为null时，不参与签名拼接
			} else {
				signMap.put(testcase.getSignlist().get(i), value);
			}
		}
		return signMap;
	}

	/**
	 * 更新参与签名计算、url参数、form参数、header参数的List到用例中
	 * @param testcase
	 * @param urlParaCheck
	 * @param formParaCheck
	 * @param headerParaCheck
	 * @return  IftTestCase
	 */
	protected static IftTestCase updateAllToListForCase(IftTestCase testcase,String[] urlParaCheck, 
			String[] formParaCheck,String[] headerParaCheck) {
		ArrayList<String> signList = new ArrayList<String>();// 参与签名计算的参数名列表
		ArrayList<String> urlParaList = new ArrayList<String>();// 参与url拼接的参数名列表
		ArrayList<String> formParaList = new ArrayList<String>();// 参与form构造的参数名列表
		ArrayList<String> headersParaList = new ArrayList<String>();// 参与header的参数名列表
		Iterator<Entry<String, String>> it = testcase.getCaseMap().entrySet().iterator();
		int i = 0;
		String argKey[] = new String[testcase.getCaseMap().size()];
		String argValue[] = new String[testcase.getCaseMap().size()];
		while (it.hasNext()) {
			Map.Entry<String, String> entity = (Entry<String, String>) it.next();
			argKey[i] = entity.getKey().toString();
			argValue[i] = entity.getValue().toString();
			i++;
		}
		// 默认为需要计算签名
		testcase.setSignFlag(true);
		try {
			// 从第四个参数 method开始遍历之后所有键值对
			for (int j = IftConf.paramStartCol; j < argValue.length; j++) {
				// 整理签名所需的字段
				if (testcase.getArgCount() == 0) {
					// 无需计算签名
					testcase.setSignFlag(false);
				} else {
					if (j < IftConf.paramStartCol + testcase.getArgCount()) {
						signList.add(argKey[j]);
					}
					if (j == IftConf.paramStartCol + testcase.getArgCount()) {
						testcase.setSignKey(argKey[j]);
					}
				}
				// 检查url、form、header内包含的参数列表
				for (int n = 0; n < urlParaCheck.length; n++) {
					if (argKey[j].toLowerCase().equals(urlParaCheck[n].toLowerCase())) {
						urlParaList.add(argKey[j]);
					}
				}
				for (int n = 0; n < formParaCheck.length; n++) {
					if (argKey[j].toLowerCase().equals(formParaCheck[n].toLowerCase())) {
						formParaList.add(argKey[j]);
					}
				}
				for (int n = 0; n < headerParaCheck.length; n++) {
					if (argKey[j].toLowerCase().equals(headerParaCheck[n].toLowerCase())) {
						headersParaList.add(argKey[j]);
					}
				}

			}// 用例中的所有键值对遍历完毕
			testcase.setParalist(urlParaList);
			testcase.setFormlist(formParaList);
			testcase.setHeaderlist(headersParaList);
			testcase.setSignlist(signList);
		} catch (Exception e) {
			log.error("传入的用例实体类信息有误，无法完成读取，请检查");
			log.error(e.getMessage());
		}
		return testcase;
	}

	/**
	 * 更新计算后的签名值到用例中 
	 * @param testCase
	 * @param signValue
	 * @return IftTestCase
	 */
	protected static IftTestCase updateSignValueForCase(IftTestCase testCase,String signValue) {
		LinkedHashMap<String, String> caseMap = testCase.getCaseMap();
		if (!testCase.isSignFlag()) {
			// 无需计算签名
			log.info("无需计算签名");
			return testCase;
		}
		String value = caseMap.get(testCase.getSignKey());
		if (value.length() < 1) {
			// 签名项原参数值为空时，表示要发送的签名值为空，
			value = "";
		} else if (value.equalsIgnoreCase("null")) {
			// 签名项原参数值为null时，不改变签名值
		} else {
			// 签名项原参数值不为空，且不等于null时，更新为计算后的签名值
			value = signValue;
		}
		caseMap.put(testCase.getSignKey(), value);
		testCase.setCaseMap(caseMap);
		return testCase;
	}

	/**
	 * 针对参数值中的特殊标识做处理 当前只针对标识为rand、timetamp、date时进行处理
	 * @param testCase
	 * @param randNum
	 * @return IftTestCase
	 */
	protected static IftTestCase updateAllParaForCase(IftTestCase testCase,int randNum) {
		LinkedHashMap<String, String> caseMap = testCase.getCaseMap();
		if (null == caseMap || caseMap.size() < 1) {
			log.error("用例的参数值对为空，请检查");
			return testCase;
		}
		// 遍历所有键值对，针对特殊标识做处理
		Iterator<Entry<String, String>> it = caseMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entity = (Entry<String, String>) it.next();
			String key = entity.getKey().toString();
			String value = entity.getValue().toString();
			// 针对参数值特殊标识rand的处理，随机生成长度为10个字符串
			if (value.toLowerCase().equals("rand")) {
				value = CommUtils.getRandomStr(randNum);
				caseMap.put(key, value);
			}
			// 针对参数值特殊标识timestamp的处理，获取Unix格式时间戳
			if (value.toLowerCase().equals("timestamp")) {
				value = CommUtils.getTimestamp();
				caseMap.put(key, value);
			}
			// 参数值标识为date时，日期字符串格式20120626092109 年月日时分秒
			if (value.toLowerCase().equals("date")) {
				value = CommUtils.getStrRandNum(3);
				caseMap.put(key, value);
			}
			// 其它处理
		}
		testCase.setCaseMap(caseMap);
		return testCase;
	}

	/**
	 * 针对cookie信息的处理 中文按照用例中指定编码进行urlEncode转码
	 * 
	 * @param testCase
	 * @return IftTestCase
	 */
	protected static IftTestCase updateCookieForCase(IftTestCase testCase) {
		String cookies = "";
		if (null == testCase.getCookie() || testCase.getCookie().length() < 1) {
			cookies = "";
		}else{
			cookies = testCase.getCookie();
		}
		if(cookies.contains(";")){
			String cookiesTmp = "";
			TreeMap<String,String> cookieMap = new TreeMap<String,String>();
			cookieMap = CommUtils.parseQuery(cookies, ';', '=');
			Iterator<Entry<String,String>> ite = cookieMap.entrySet().iterator();
			while(ite.hasNext()){
				Entry<String,String> entry = (Entry<String,String>) ite.next();
				cookiesTmp += entry.getKey()+"="+CommUtils.urlEncode(entry.getValue().toString(),testCase.getEnCoding()) + ";";
			}
			cookies = cookiesTmp;
		}
		testCase.setCookie(cookies);
		return testCase;

	}
	
	/**
	 * 输出Excel格式测试报告
	 * @param folder
	 * @param excelName
	 * @param sheetName
	 * @param arrres
	 * @return boolean 输出成功返回true
	 */
	public boolean CreatReportExcel(String folder,String excelName,String sheetName,List<LinkedHashMap<String,String>> arrres) {
		ExportReportExcel exportexcel = new ExportReportExcel();
		return exportexcel.CreatReportExcel(folder,excelName,sheetName,arrres);
	}
	//直接执行已处理好的post
	/**
	 * 发起Post
	 * @param header 
	 * @param http url地址
	 * @param posturl 参数键值对 格式key=value&key=value
	 * @return ResponseInfo
	 */
	public ResponseInfo ExecPostResquest(TreeMap<String, String> header,String http, String posturl) {
		ResponseInfo resInfo=new ResponseInfo();
		// 设置发起请求时使用的编码
		this.sendRequestCore.setCharset("UTF-8");
		try {
			// 发起请求
			resInfo= sendRequestCore.post(header, http, posturl);
			resInfo.setHttpUrl("post请求的url信息：" + http + "  post请求的body信息为-" + posturl);
		} catch (Exception e) {
			log.error("发送http请求失败，请检查");
			log.error(e.getMessage());
			resInfo.setErrMsgInfo("发送http请求失败，请检查");
			return resInfo;
		}
		return resInfo;
	}

	/**
	 * 发起Get请求
	 * @param header
	 * @param gethttpurl get请求的url 包括参数键值对 ...?key=value&key=value
	 * @return ResponseInfo
	 */
	public ResponseInfo ExecGetResquest(TreeMap<String, String> header,String gethttpurl) {
		ResponseInfo resInfo=new ResponseInfo();
		// 设置发起请求时使用的编码
		this.sendRequestCore.setCharset("UTF-8");
		try {
			// 发起请求
			resInfo= sendRequestCore.get(header, gethttpurl);
			resInfo.setHttpUrl(gethttpurl);
		} catch (Exception e) {
			log.error("发送http请求" + gethttpurl + "失败，请检查");
			log.error(e.getMessage());
			resInfo.setErrMsgInfo("发送http请求" + gethttpurl + "失败，请检查");
			return resInfo;
		}
		return resInfo;
	}

}
