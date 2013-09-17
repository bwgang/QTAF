package cn.baiweigang.qtaf.dispatch.testcase;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试用例基类
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 */
public abstract class SuperCase implements ICase {

		protected List<String> xmlPathNameList;
		public SuperCase(){
			xmlPathNameList=new ArrayList<>();
		}
		public List<String> getCaseList() {
			return this.xmlPathNameList;
		}
}
