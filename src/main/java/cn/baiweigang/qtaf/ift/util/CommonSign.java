/**
 * 
 */
package cn.baiweigang.qtaf.ift.util;


import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import cn.baiweigang.qtaf.toolkit.log.Tklogger;
import cn.baiweigang.qtaf.toolkit.string.CommUtils;


/**
 * 各种签名算法
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 */
public class CommonSign {
	protected static Tklogger log = Tklogger.getLogger(CommonSign.class);// 日志记录

	/**
	 * 说明：签名算法1 key=value&key=value&.....key=valuesecret_key 最后连接私钥时不带&符号
	 * 计算MD5时，中文按照UTF-8编码计算
	 * @param 参与签名计算的键值对TreeMap
	 * @param 签名计算所需的密钥secret_key
	 * @return String
	 */
	public static String signMethodOne(TreeMap<String, String> signpara,
			String secret_key) {
		String expBaseSign = "";
		String expSign = "";

		Iterator<?> ite = signpara.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			expBaseSign = expBaseSign + entry.getKey() + "="
					+ entry.getValue().toString() + "&";
		}
		if (expBaseSign.length() < 1) {
			expSign = expBaseSign + secret_key;
		} else {
			expSign = expBaseSign.substring(0, expBaseSign.length() - 1)
					+ secret_key;
		}
		return CommUtils.getMD5(expSign,"UTF-8");
	}

	/**
	 * 说明：签名算法2 key=value&key=value&.....key=valuesecret_key 最后连接私钥时不带&符号
	 * 计算MD5时，中文按照GBK编码计算
	 * @param 参与签名计算的键值对TreeMap
	 * @param 签名计算所需的密钥secret_key
	 * @return String
	 */
	public static String signMethodTwo(TreeMap<String, String> signpara,String secret_key) {
		String expBaseSign = "";
		String expSign = "";

		Iterator<?> ite = signpara.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			expBaseSign = expBaseSign + entry.getKey() + "="
					+ entry.getValue().toString() + "&";
		}
		if (expBaseSign.length() < 1) {
			expSign = expBaseSign + secret_key;
		} else {
			expSign = expBaseSign.substring(0, expBaseSign.length() - 1)
					+ secret_key;
		}
		log.info("签名计算串：" + expSign);
		return CommUtils.getPhpMD5(expSign);
	}

	public static String signMethodUserCenter(TreeMap<String, String> signpara,String secret_key) {
		String expBaseSign = "";
		String expSign = "";

		Iterator<?> ite = signpara.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			expBaseSign = expBaseSign + entry.getKey() + "="
					+ entry.getValue().toString();
		}
		expSign = expBaseSign + secret_key;
		log.info("签名计算串：" + expSign);
		return CommUtils.getMD5(expSign,"UTF-8");
	}

	
	/**
	 * 说明：ID化项目专用签名算法
	 * 
	 * @param tremap
	 * @return
	 */
	public static String signMethodForID(TreeMap<String, String> tremap,
			String secret_key) {
		String expBaseSign = "";
		String expSign = "";
		Iterator<?> ite = tremap.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			if (entry.getKey().toString().equals("qc") || entry.getKey().toString().equals("tc")) {
				expBaseSign = expBaseSign + entry.getKey() + "=" + CommUtils.urlDecode(entry.getValue().toString());
			} else {
				expBaseSign = expBaseSign + entry.getKey() + "="
						+ CommUtils.urlRawDecode(CommUtils.urlEncode(entry.getValue().toString()));
			}
		}
		expSign = CommUtils.getPhpMD5(expBaseSign + secret_key);
		return expSign;
	}

	/**
	 * 说明：签名算法3 value为空时 不参与签名计算 key=value&key=value&.....key=valuesecret_key
	 * 最后连接私钥时不带&符号 计算MD5时，中文按照UTF-8编码计算
	 * 
	 * @param 参与签名计算的键值对TreeMap
	 * @param 签名计算所需的密钥secret_key
	 * @return String
	 */
	public static String signMethodThird(TreeMap<String, String> signpara,
			String secret_key) {
		String expBaseSign = "";
		String expSign = "";

		Iterator<?> ite = signpara.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			if (entry.getValue().toString().length() > 0) {
				expBaseSign = expBaseSign + entry.getKey() + "=" + entry.getValue().toString() + "&";
			}
		}
		if (expBaseSign.length() < 1) {
			expSign = expBaseSign + secret_key;
		} else {
			expSign = expBaseSign.substring(0, expBaseSign.length() - 1) + secret_key;
		}
		return CommUtils.getMD5(expSign,"UTF-8");
	}
	
	/**
	 * 说明：签名算法4 value为空时 不参与签名计算 key=value&key=value&.....key=valuesecret_key
	 * 最后连接私钥时带&符号 计算MD5时，中文按照UTF-8编码计算
	 * 
	 * @param 参与签名计算的键值对TreeMap
	 * @param 签名计算所需的密钥secret_key
	 * @return String
	 */
	public static String signMethodFour(TreeMap<String, String> signpara,
			String secret_key) {
		String expBaseSign = "";
		String expSign = "";

		Iterator<?> ite = signpara.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			if (entry.getValue().toString().length() > 0) {
				String value=entry.getValue().toString();
//				if (MyString.isChinese(value)) {
//					value=CommUtils.urlEncode(value, IEnCoding.UTF8);
//				}
				expBaseSign = expBaseSign + entry.getKey() + "=" +value  + "&";
			}
		}
		if (expBaseSign.length() < 1) {
			expSign = expBaseSign + secret_key;
		} else {
			expSign = expBaseSign.substring(0, expBaseSign.length() - 1) + secret_key;
		}
//		log.info("参与签名的串："+expSign);
//		log.info("计算后的结果为："+CommUtils.getMD5(expSign,IEnCoding.UTF8));
		return CommUtils.getMD5(expSign,"UTF-8");
	}
	
	/**
	 * 云盘专用签名算法
	 * @param signpara
	 * @param secret_key
	 * @return
	 */
	public static String signMethodCloud(TreeMap<String, String> signpara,String secret_key){
		String yunpanBaseSign = "";
		Iterator<Entry<String,String>> ite = signpara.entrySet().iterator();
		while(ite.hasNext()){
			Entry<String,String>  entry = (Entry<String,String> ) ite.next();
			yunpanBaseSign = yunpanBaseSign + entry.getKey() + "=" + entry.getValue();
		}
		return CommUtils.getMD5(yunpanBaseSign + secret_key,"UTF-8");
	}
}
