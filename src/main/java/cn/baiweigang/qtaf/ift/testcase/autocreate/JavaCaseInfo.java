package cn.baiweigang.qtaf.ift.testcase.autocreate;

import java.util.List;

import cn.baiweigang.qtaf.ift.testcase.IftTestCase;

/**
 * 生成java文件时需要的信息
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class JavaCaseInfo {

	private List<IftTestCase> allCase;//所有用例列表
	private String packageName;//包名
	private String javaFileName;//java文件名
	private String javaSavePath;//java文件保存目录
	private String caseDataPathName;//用例数据文件路径
	private String caseDataSheetName;//Excel用例数据sheet名称
	private String templatePathName;//模板路径
	
	private String excelReportSheetName;//excel测试报告sheet名称
	private String excelReportName;//excel测试报告文件名称
	private String excelReportPath;//excel测试报告存储目录
	public List<IftTestCase> getAllCase() {
		return allCase;
	}
	public void setAllCase(List<IftTestCase> allCase) {
		this.allCase = allCase;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getJavaFileName() {
		return javaFileName;
	}
	public void setJavaFileName(String javaFileName) {
		this.javaFileName = javaFileName;
	}
	public String getJavaSavePath() {
		return javaSavePath;
	}
	public void setJavaSavePath(String javaSavePath) {
		this.javaSavePath = javaSavePath;
	}
	public String getCaseDataPathName() {
		return caseDataPathName;
	}
	public void setCaseDataPathName(String caseDataPathName) {
		this.caseDataPathName = caseDataPathName;
	}
	public String getTemplatePathName() {
		return templatePathName;
	}
	public void setTemplatePathName(String templatePathName) {
		this.templatePathName = templatePathName;
	}
	public String getExcelReportSheetName() {
		return excelReportSheetName;
	}
	public void setExcelReportSheetName(String excelReportSheetName) {
		this.excelReportSheetName = excelReportSheetName;
	}
	public String getExcelReportName() {
		return excelReportName;
	}
	public void setExcelReportName(String excelReportName) {
		this.excelReportName = excelReportName;
	}
	public String getExcelReportPath() {
		return excelReportPath;
	}
	public void setExcelReportPath(String excelReportPath) {
		this.excelReportPath = excelReportPath;
	}
	public String getCaseDataSheetName() {
		return caseDataSheetName;
	}
	public void setCaseDataSheetName(String caseDataSheetName) {
		this.caseDataSheetName = caseDataSheetName;
	}
}
