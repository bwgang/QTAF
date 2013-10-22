package cn.baiweigang.qtaf.toolkit;

import java.util.ArrayList;
import java.util.List;

import cn.baiweigang.qtaf.toolkit.mysql.MysqlUtil;
import cn.baiweigang.qtaf.toolkit.mysql.dao.DaoUtil;
import cn.baiweigang.qtaf.toolkit.mysql.dao.ITable;


/**
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public class TestMysqlUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		att_click_info info = new att_click_info();
		List<ITable> infoList = new ArrayList<>();
		MysqlUtil.connMysql("127.0.0.1", "3306", "att", "auto", "auto");
		infoList=DaoUtil.getAllInfo(info);
		for (ITable table : infoList) {
			info=(att_click_info) table;
			List<String> keyList = info.getColList();
			for (String key : keyList) {
				P(key+"="+info.get(key));
			}
		}
	}
	
	private static void P(Object obj) {
		System.out.print(obj+"\n");
	}

}
