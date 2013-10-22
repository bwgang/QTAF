/**
 * 
 */
package cn.baiweigang.qtaf.toolkit;

import cn.baiweigang.qtaf.ift.IftConf;
import cn.baiweigang.qtaf.toolkit.dbunit.DbUnitUtil;
import cn.baiweigang.qtaf.toolkit.mysql.ConnMysql;

/**
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class TestDbunitUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConnMysql conn = new ConnMysql("127.0.0.1", "3306", "att", "auto", "auto");
		DbUnitUtil.writeToFileFromMysql(IftConf.RootPath+"test.xml", conn, "select * from att_click_info;select * from att_task_info");
		
		DbUnitUtil.writeToFileFromDataBase(IftConf.RootPath+"test.xls", DbUnitUtil.getDatabaseConnectionFromMysql(conn), "select * from att_click_info;select * from att_task_info");
		
	}

}
