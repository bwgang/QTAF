package cn.baiweigang.qtaf.ift.testcase.autocreate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.baiweigang.qtaf.dispatch.testcase.SuperCase;
import cn.baiweigang.qtaf.ift.IftConf;
import cn.baiweigang.qtaf.toolkit.log.Tklogger;
import cn.baiweigang.qtaf.toolkit.string.CommUtils;
import cn.baiweigang.qtaf.toolkit.string.TkString;


/**
 * 数据文件类型的测试用例
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class IftDataFileCase extends SuperCase{
	Tklogger log =Tklogger.getLogger(IftDataFileCase.class);//日志记录
	
	//任务名称
	private String taskName;//测试任务名称
	//根据用例数据文件，创建java、xml文件相关配置信息
	private String javaCase;//生成的java文件存储目录
	private String times;// 获取当前时间戳
	private String allReportPath ;// html、excel测试报告存储的上级目录
	private String htmlReportPath ;// excel格式测试报告存储的目录
	private String excelReportPath ;// excel格式测试报告存储的目录
	private String reportExcelName ;// excel格式测试报告名称
	private String xmlFilePath ;// 生成xml文件的路径
	private String xmlFileName ;// 生成xml文件的名称
	private String packageInfo;//生成java文件的包名

	private TestngXmlSuite xmlSuite;//用例自动生成用到的测试套
	private List<Map<String, Object>> testCaseList;//用例集列表
	
	public   IftDataFileCase() {
		super();
		xmlFilePath = IftConf.IftPath+ "suites/data/";
		javaCase=IftConf.JavaPath;

		xmlSuite=new TestngXmlSuite();
		testCaseList=new ArrayList<Map<String, Object>>();
		
		times = CommUtils.getNowTime()+"_"+CommUtils.getRandomStr(5);// 获取当前时间戳+5位随机串
		allReportPath = IftConf.IftPath + "report/" + times;// 此次测试报告存储路径
		excelReportPath = allReportPath + "/excel";// excel格式测试报告存储的目录
		htmlReportPath = allReportPath + "/html";
		packageInfo="ift.testcases";
		
		reportExcelName = "未命名的Excel测试报告" + CommUtils.getRandomStr(5);// 默认的Excel格式测试报告名称
		xmlFileName = "未命名的XML文件" + CommUtils.getRandomStr(5);// 默认的生成xml文件的名称
	}

	/**
	 * 
	 * @param casePath 用例路径 必填
	 * @param sheetName Excel的sheet表名 可选
	 * @param caseName  用例名称 必填
	 * @param cls		执行用例的类 必填
	 * @param method    类中的方法 必填
	 */
	public void addCase(String casePath, String sheetName, String caseName,
			Class<?> cls,String method) {
		String template = IftConf.TemplatePath;
		if (TkString.IsNullOrEmpty(casePath) || TkString.IsNullOrEmpty(caseName)
				|| TkString.IsNullOrEmpty(template)) {
			return;
		}//任一项空值或长度小于1时，不做处理
		
		Map<String, Object> casemap = new TreeMap<String, Object>();
		casemap.put("javaname",caseName.replace(".", "_"));
		casemap.put("casepath", casePath);
		casemap.put("sheetName", sheetName);
		casemap.put("template", template);
		casemap.put("casename", caseName);
		casemap.put("class", cls);
		casemap.put("method", method);
		testCaseList.add(casemap);	
	}
	
	/**
	 * 
	 * @param casePath 用例路径 必填
	 * @param sheetName Excel的sheet表名 可选
	 * @param caseName  用例名称 必填
	 * @param cls		执行用例的类 必填
	 * @param method    类中的方法 必填
	 */
	public void addCase(String casePath, String caseName,Class<?> cls,String method) {
		addCase(casePath,"TestCase",caseName,cls,method);
	}
	
	
	private void setReportExcelName(String reportExcelName) {
		if (null==reportExcelName || reportExcelName.length()<1)return;
		this.reportExcelName = reportExcelName;
	}

	private void setXmlFileName(String xmlFileName) {
		if (null==xmlFileName || xmlFileName.length()<1)return;
		this.xmlFileName = xmlFileName;
	}

	public void setIftTaskName(String setTaskName) {
		setTaskName(setTaskName);
		setReportExcelName(taskName);
		setXmlFileName(taskName);
		this.allReportPath = IftConf.IftPath + "report/" +taskName+"/"+ times;// 此次测试报告存储路径
		this.excelReportPath = this.allReportPath + "/excel";// excel格式测试报告存储的目录
		this.htmlReportPath = this.allReportPath + "/html";
	}

	
	public void setExcelReportPath(String excelReportPath) {
		if (TkString.IsNullOrEmpty(excelReportPath)) return;
		this.excelReportPath=excelReportPath;
	}
	
	/**
	 * 设置xmlPathNameList列表
	 * @return
	 */
	public boolean updateXmlFileList() {
		//创建java、xml文件
		boolean res = createJavaAndXmlFile();
		
		if (res) {
//			log.info("添加测试套文件："+this.xmlSuite.getXmlFilePath()+this.xmlSuite.getXmlName());
			xmlPathNameList.add(this.xmlSuite.getXmlFilePath()+this.xmlSuite.getXmlName());
		}else{
			log.info("生成java或xml文件失败，请检查日志记录");
		}
		return res;
	}
	
	public String getExcelReportPath() {
		return this.excelReportPath+"/"+this.reportExcelName+".xlsx";
	}
	public String getHtmlReportPath() {
		return this.htmlReportPath;
	}
	private boolean createJavaAndXmlFile() {
		boolean flag = false;
		try {
			for (int i = 0; i < testCaseList.size(); i++) {
				TestCaseSet iftCaseSet = new TestCaseSet();
				TestngXmlTest iftXmlTest = new TestngXmlTest();
				
				//设置用例数据文件位置
				flag = iftCaseSet.setCasedatapath(testCaseList.get(i).get("casepath").toString());
				if (!flag) continue;
				iftCaseSet.setCasedatasheetName(testCaseList.get(i).get("sheetName").toString());
				
				//设置测试用例模板的位置
				flag=iftCaseSet.setCasetemplatepath(testCaseList.get(i).get("template").toString());
				if (!flag) continue;
				iftCaseSet.setCls((Class<?>) testCaseList.get(i).get("class"));
				iftCaseSet.setMethod(testCaseList.get(i).get("method").toString());
				//设置java文件保存目录
				flag=iftCaseSet.setJavasavepath(this.javaCase);
				if (!flag) continue;
				
				//从用例数据库文件中读取信息
				if (!iftCaseSet.readCaseInfo() || !iftCaseSet.isConfigOk()) {
					log.info("此用例集读取失败！请检查日志");
					continue;
				}
				
				//TestCaseSet的其它配置信息
				flag = iftCaseSet.setPackagename(this.packageInfo);//固定的java文件包名
				if (!flag) continue;
				
				flag = iftCaseSet.setCasename(testCaseList.get(i).get("javaname").toString());//设置生成的用例名
				if (!flag) continue;
				
				flag = iftCaseSet.setReportpath(excelReportPath);//设置Excel报告存储目录
				if (!flag) continue;
				
				flag = iftCaseSet.setReportexcelname(reportExcelName);//Excel报告的文件名
				if (!flag) continue;
				
				flag = iftCaseSet.setReportsheetname(testCaseList.get(i).get("javaname").toString());//Excel文件的sheet名称
				if (!flag) continue;
				
				flag = iftXmlTest.setTestCaseSet(iftCaseSet);
				if (!flag) continue;
				
				flag =  iftXmlTest.setXmltestname(testCaseList.get(i).get("javaname").toString());//对应的测试集名称
				if (!flag) continue;
				
				flag = xmlSuite.addXmlTest(iftXmlTest);
				if (!flag) continue;
			}
			
			flag = xmlSuite.setXmlFilePath(xmlFilePath);
			flag = xmlSuite.setXmlName(xmlFileName);
			flag = xmlSuite.updateTestToXmlSuite();
			flag = xmlSuite.createXmlFile();
		} catch (Exception e) {
			log.error("创建测试套失败！");
			log.error(e.getMessage());
		}
		return flag;
	}

	public String getTaskName() {
		return taskName;
	}

	private void setTaskName(String taskName) {
		this.taskName = taskName;
	}


}
