/**
 * 
 */
package cn.baiweigang.qtaf.toolkit.util;


import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;



/**
 * XML文件解析处理
 *
 */
public class XmlUtil {
	private static TreeMap<String, String> resultMap;
	private static LogUtil log = LogUtil.getLogger(XmlUtil.class);// 日志记录
	
	public static TreeMap<String, String> fomatXMLToMap(String strXML) {
		resultMap = new TreeMap<String, String>();
		try {
			Document doc = DocumentHelper.parseText(strXML);
			Element root = doc.getRootElement();
			AttriToMap(root);
			traverse(root);
			Dom2Map(doc);
		} catch (DocumentException e) {
			log.error(e.getMessage());
		}
		return resultMap;
	}

	/**
	 * 标签属性解析
	 * 
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	private static void AttriToMap(Element e) {
		List<Attribute> tempList = e.attributes();
		for (int i = 0; i < tempList.size(); i++) {
			// 属性的取得
			Attribute item = tempList.get(i);
			MyPut(resultMap, item.getName(), item.getValue());
//			resultMap.put(item.getName(), item.getValue());
		}

	}

	/**
	 * 标签属性解析，遍历
	 * 
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	private static void traverse(Element e) {
		List<Element> list = e.elements();
		for (int i = 0; i < list.size(); i++) {
			Element iter = list.get(i);
			AttriToMap(iter);
			traverse(iter);
		}
	}

	@SuppressWarnings("unchecked")
	private static void Dom2Map(Document doc) {
		if (doc == null)
			return;
		Element root = doc.getRootElement();
		for (Iterator<Element> iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = iterator.next();
			List<Element> list = e.elements();
			if (list.size() > 0) {
				Dom2Map(e);
			} else
				MyPut(resultMap, e.getName(), e.getText());
//				resultMap.put(e.getName(), e.getText());
		}
	}

	@SuppressWarnings("unchecked")
	private static void Dom2Map(Element e) {
		List<Element> list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = list.get(i);
				if (iter.elements().size() > 0) {
					Dom2Map(iter);
				} else {
					MyPut(resultMap, iter.getName(), iter.getText());
//					resultMap.put(iter.getName(), iter.getText());
				}
			}
		} else {
			MyPut(resultMap, e.getName(), e.getText());
//			resultMap.put(e.getName(), e.getText());
		}
	}
	
	/**
	 * map增加key-value逻辑，如果已存在，则不覆盖，以&连接原值
	 * @param map
	 * @param key
	 * @param value
	 */
	private static void MyPut(TreeMap<String, String>map,String key,String value){
		for (Entry<String, String> entry : map.entrySet()) {
			String keyOld = entry.getKey();
			String valueOld = entry.getValue();
			if (keyOld.equals(key)) {
				value=valueOld+"&"+value;
				break;
			}
		}
		map.put(key, value);
	}
}
