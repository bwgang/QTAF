package cn.baiweigang.qtaf.ift.testcase.autocreate;

import java.util.Map;
import java.util.TreeMap;

import cn.baiweigang.qtaf.ift.IftConf;
import cn.baiweigang.qtaf.toolkit.util.FreeMakerUtil;
import cn.baiweigang.qtaf.toolkit.util.LogUtil;


/**
 * 封装用例集，对集合进行分组格式化等处理，当前是1个excel文件对应1个此类的实例
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a>(bwgang@163.com)<br/> 2012-11-12
 *
 */
public class CreateJavaFile {	
		
	private static LogUtil log=LogUtil.getLogger(CreateJavaFile.class);//日志记录
	
	/**
	 * 根据用例数据信息创建对应的.java源文件
	 * @return boolean 创建成功返回true，失败返回false
	 */
	public boolean creatJavaSrcFile(JavaCaseInfo javaInfo) {
		String javaFilePathTmp = javaInfo.getJavaSavePath()+javaInfo.getJavaFileName()+".java";
		FreeMakerUtil creatjava=new FreeMakerUtil();
		if (creatjava.CreateJavaFile(IftConf.TemplatePath, getJavaFileData(javaInfo), javaFilePathTmp)) {
			log.info("创建"+javaInfo.getJavaFileName()+"对应的.java文件成功："+javaFilePathTmp);
			return true;
		}
		log.error("创建"+javaInfo.getJavaFileName()+"对应的.java文件成功失败:"+javaFilePathTmp);
		return false;
	}

	
	//私有方法
	private Map<String, Object> getJavaFileData(JavaCaseInfo javaInfo){
		Map<String, Object> data = new TreeMap<String, Object>();
		data.put("javaInfo", javaInfo);
		Map<String, String> clsInfo = new TreeMap<>();
		String impotInfo = javaInfo.getCls().getPackage().toString();
		impotInfo = impotInfo.substring(8)+"."+javaInfo.getCls().getSimpleName();
		clsInfo.put("importInfo", impotInfo);
		clsInfo.put("className", javaInfo.getCls().getSimpleName());
		clsInfo.put("method", javaInfo.getMethod());
		data.put("clsInfo", clsInfo);
		return data;
	}
	
}
