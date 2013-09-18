package cn.baiweigang.qtaf.ift.testcase.autocreate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.baiweigang.qtaf.ift.testcase.IftTestCase;
import cn.baiweigang.qtaf.ift.testcase.format.FormatCase;
import cn.baiweigang.qtaf.toolkit.util.FileUtil;
import cn.baiweigang.qtaf.toolkit.util.FreeMakerUtil;
import cn.baiweigang.qtaf.toolkit.util.LogUtil;


/**
 * 封装用例集，对集合进行分组格式化等处理，当前是1个excel文件对应1个此类的实例
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/> 2012-11-12
 *
 */
public class TestCaseSet {	
		
	private static LogUtil log=LogUtil.getLogger(TestCaseSet.class);//日志记录
	
	private  String javasavepath="";//生成的java用例的路径	
	private  String casetemplatepath="";//用例模板路径
	private  String casedatapath="";//用例数据路径
	private  String casedatasheetName="";//Excel用例数据的sheet名
	private  Class<?> cls;//执行用例的类
	private  String method;//执行用例的方法
	
	private  String reportsheetname="sheet"+System.currentTimeMillis();//默认excel格式测试报告sheet名称
	private  String reportexcelname="未命名"+System.currentTimeMillis();//默认excel格式测试报告名称
	private  String reportpath="report/Temp/Excel";//默认Excel格式测试存储的目录
	
	private  String testname="testcase";//生成的java源文件名
	private  String packagename="";//生成的java源文件存放的包位置
	
	private List<IftTestCase> allcase;//所有用例实体List
	
	private String xmlfilepath;//xml文件所在的目录，是在哪个文件夹下
	private String xmlname;//xml文件的名称，包括后缀名.xml；

	/**
	 * 读取测试用例
	 * @return boolean 读取成功返回true
	 */
	public boolean readCaseInfo() {
		boolean flag=false;
		
		try {
			if (null!=allcase) {
				log.info("已读取测试用例，请勿重复读取！！");
				return false;		
			}
			FormatCase formatcase=new FormatCase();
			allcase=new ArrayList<IftTestCase>();
			formatcase.FormatCaseFromObj(getCasedatapath(),getCasedatasheetName());
			//存储用例实体列表信息
			allcase=formatcase.getTestCase();	
			
			//获取测试集名称作为输出的测试报告名称
			this.reportexcelname=formatcase.getCasesetName();
			flag=true;
//			log.info("读取用例"+getCasedatapath()+"成功！");
		} catch (Exception e) {		
			log.error("读取用例"+getCasedatapath()+"失败！！");
			log.error(e.getMessage());
		}
		return flag;		
	}
	
	/**
	 * 根据用例数据信息创建对应的.java源文件
	 * @return boolean 创建成功返回true，失败返回false
	 */
	public boolean creatJavaSrcFile() {
		boolean flag=false;
		try {
			FreeMakerUtil creatjava=new FreeMakerUtil();
			flag=creatjava.CreateJavaFile(getCasetemplatepath(), getJavaFileData(), getJavasavepath()+getCasename()+".java");
			String javaFilePathTmp = "";
			log.info("创建"+getCasename()+"对应的.java文件成功："+javaFilePathTmp);
			flag=true;	
		} catch (Exception e) {
			log.error("创建"+getCasename()+"对应的.java文件成功失败");
			log.error(e.getMessage());
			flag=false;
		}		
		return flag;
	}

	/**
	 * 判断用例集是否初始化配置完毕
	 * @return boolean 初始化完毕返回true，否则返回false
	 */
	public boolean isConfigOk() {
		boolean flag=false;
		try {
			//判断用例数是否不小于1
			if (getAllTestCase().size()<1) {
				log.info("用例："+getCasename()+"的用例数小于1");
				return false;
			}
			flag=true;
		} catch (Exception e) {
			log.error("初始化用例："+getCasename()+"异常");
			log.error(e.getMessage());
		}
		return flag;
	}
	
	
	/**
	 * 设置用例数据文件路径
	 * @param casedatapath 
	 * @return boolean 设置成功返回true，失败返回false
	 */
	public boolean setCasedatapath(String casedatapath) {
		if (!FileUtil.isEmeyxist(casedatapath)) {
			log.error("用例数据"+casedatapath+"文件不存在，请检查");
			return false;
		}
		this.casedatapath = casedatapath;
		return true;
	}
	/**
	 * 设置Excel用例数据的Sheet表名
	 * @param casedatasheetName
	 */
	public void setCasedatasheetName(String casedatasheetName) {
		this.casedatasheetName = casedatasheetName;
	}

	
	/**
	 * 设置Excel格式测试报告的名称
	 * @param reportexcelname
	 * @return boolean 设置成功返回true，失败返回false
	 */
	public boolean setReportexcelname(String reportexcelname) {
		if (null==reportexcelname || reportexcelname.length()<1) {
			return false;
		}
		this.reportexcelname = reportexcelname;
		return true;
	}

	
	/**
	 * 设置用例模板的路径
	 * @param casetemplatepath
	 * @return boolean 设置成功返回true，失败返回false
	 */
	public boolean setCasetemplatepath(String casetemplatepath) {
		if (!new File(casetemplatepath).exists()) {
			System.out.println("用例模板【"+casetemplatepath+"】不存在，请检查");
			return false;
		}
		this.casetemplatepath = casetemplatepath;
		return true;
	}
	
	/**
	 * 获取所有用例实体信息列表
	 * @return List<IftTestCase>
	 */
	public  List<IftTestCase> getAllTestCase() {
		return allcase;
	}
	
	
	/**
	 * 获取创建的.java文件的包名
	 * @return String 格式为ift.TestCases这样格式的
	 */
	public String getPackagename() {
		if (this.packagename.length()<1) this.packagename="ift.testcases";
		return packagename;
	}

	/**
	 * 设置待创建的用例.java源文件所在的包名
	 * @param packagename
	 * @return boolean 设置成功返回true，失败返回false
	 */
	public boolean setPackagename(String packagename) {
		if (null==packagename || packagename.length()<1) {
			return false;
		}
		
//		String temp=packagename.replace(".", "/");
//		File dirname = new File(javasavepath+"/"+temp); 
//		if (!dirname.isDirectory())
//		{ //目录不存在
//		     dirname.mkdir(); //创建目录
//		}  
		this.packagename = packagename;
		return true;
	}
	
	/**
	 * 获取用例对应的.java文件名称
	 * @return String
	 */
	public String getCasename() {
		return testname;
	}
	
	/**
	 * 设置用例对应的.java文件名称
	 * @param testname
	 * @return boolean
	 */
	public boolean  setCasename(String testname) {
		if (null==testname || testname.length()<1 ) {
			return false;
		}
		this.testname = testname;
		return true;
	}

	/**
	 * @return the xmlfilepath
	 */
	public String getXmlfilepath() {
		return xmlfilepath;
	}

	/**
	 * @param xmlfilepath the xmlfilepath to set
	 */
	public boolean setXmlfilepath(String xmlfilepath) {
		if (null==xmlfilepath || xmlfilepath.length()<1) {
			return false;
		}
		File xmlfile_dir= new File(xmlfilepath);
		if (!xmlfile_dir.exists()) {
			xmlfile_dir.mkdirs();
		}
		this.xmlfilepath = xmlfilepath;
		return true;
	}

	/**
	 * @return the xmlname
	 */
	public String getXmlname() {
		return xmlname;
	}

	/**
	 * @param xmlname the xmlname to set
	 */
	public boolean setXmlname(String xmlname) {
		if (null==xmlname || xmlname.length()<1) {
			return false;
		}
		this.xmlname = xmlname;
		return true;
	}

	

	/**
	 * @param reportsheetname the reportsheetname to set
	 */
	public boolean  setReportsheetname(String reportsheetname) {
		if (null==reportsheetname || reportsheetname.length()<1) {
			return false;
		}		
		this.reportsheetname = reportsheetname;
		return true;
	}


	/**
	 * @param reportpath the reportpath to set
	 */
	public boolean setReportpath(String reportpath) {
		if (null==reportpath || reportpath.length()<1) {
			return false;
		}
		File xmlfile_dir= new File(reportpath);
		if (!xmlfile_dir.exists()) {
			xmlfile_dir.mkdirs();
		}
		this.reportpath = reportpath;
		return true;
	}

	public String getJavasavepath() {
		return javasavepath;
	}

	public boolean setJavasavepath(String javasavepath) {
		try {
			this.javasavepath = javasavepath;
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
		
	}

	
	//私有方法
	private Map<String, Object> getJavaFileData(){
		Map<String, Object> data = new TreeMap<String, Object>();
		JavaCaseInfo javaInfo=new JavaCaseInfo();
		javaInfo.setAllCase(getAllTestCase());
		javaInfo.setPackageName(getPackagename());
		javaInfo.setJavaFileName(getCasename());
		javaInfo.setJavaSavePath(getJavasavepath());
		javaInfo.setCaseDataPathName(getCasedatapath());
		javaInfo.setCaseDataSheetName(getCasedatasheetName());
		javaInfo.setTemplatePathName(getCasetemplatepath());
		javaInfo.setExcelReportSheetName(getReportsheetname());
		javaInfo.setExcelReportName(getReportexcelname());
		javaInfo.setExcelReportPath(getReportpath());
		data.put("javaInfo", javaInfo);
		
		Map<String, String> clsInfo = new TreeMap<>();
		String impotInfo = getCls().getPackage().toString();
		impotInfo = impotInfo.substring(8)+"."+getCls().getSimpleName();
		clsInfo.put("importInfo", impotInfo);
		clsInfo.put("className", getCls().getSimpleName());
		clsInfo.put("method", getMethod());
		data.put("clsInfo", clsInfo);
		return data;
	}

	/**
	 * 返回用例数据文件路径
	 * @return String
	 */
	private String getCasedatapath() {
		return casedatapath;
	}

	/**
	 * 返回Excel报告的文件名
	 * @return String
	 */
	private String getReportexcelname() {
		return reportexcelname;
	}
	
	
	/**
	 * 获取用例生成模板的路径
	 * @return String
	 */
	private String getCasetemplatepath() {
		return casetemplatepath;
	}
	
	/**
	 * @return the reportsheetname
	 */
	private String getReportsheetname() {
		return reportsheetname;
	}
		
	/**
	 * @return the reportpath
	 */
	private String getReportpath() {
		return reportpath;
	}

	private String getCasedatasheetName() {
		return casedatasheetName;
	}

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}


}
