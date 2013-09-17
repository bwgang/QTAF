package cn.baiweigang.qtaf.toolkit.string;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * 说明：解析Json字符串，解析后未map存储的键值对
 * 
 *
 */
public class JSONToMap {
	private Map<String, Object> oneResult;

	/**
	 * 说明：构造函数，初始化map表
	 */
	public JSONToMap() {
		this.oneResult = new TreeMap<String, Object>();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getResult(String str) {
		try {
			this.oneResult = JSONObject.fromObject(str);
		} catch (JSONException e) {
			
			this.oneResult=null;
		}
		return this.oneResult;
	}

	public Map<String, Object> getAllResult(String str) {
		try {
			JSONObject tempJSON = JSONObject.fromObject(str);
			this.jsonToMap(tempJSON,oneResult);
		} catch (Exception e) {
			this.oneResult=null;			
		}
		return oneResult;
	}

	@SuppressWarnings("unchecked")
	private void jsonToMap(JSONObject tempJSON,Map<String,Object> resultMap) {
		for (Iterator<String> it = tempJSON.keys(); it.hasNext();) {
			String key = it.next();
			String realKey = key;
			Object valueObj = tempJSON.get(key);
			if (valueObj instanceof JSONObject) {
				JSONObject jo = (JSONObject) valueObj;
				this.jsonToMap(jo,resultMap);
			} else if (valueObj instanceof JSONArray) {
				JSONArray ja = (JSONArray) valueObj;
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jo = ja.getJSONObject(i);
					this.jsonToMap(jo,resultMap);
				}
			} else { // 简单类型
				resultMap.put(realKey, valueObj.toString());
			}
		}
	}
}
