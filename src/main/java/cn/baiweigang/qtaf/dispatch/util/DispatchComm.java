package cn.baiweigang.qtaf.dispatch.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;


import cn.baiweigang.qtaf.dispatch.DispatchConf;

/**
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 */
public class DispatchComm {


	/**
	 * 生成随机数
	 * @param length 指定字符串长度
	 */
	public static String getRandomStr(int length) {
		Random randGen = null;
		char[] numbersAndLetters = null;
		Object initLock = new Object();
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();
					numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
				}
			}
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}
	
	/**
	 * 生成随机数(只有数字)
	 * @param length 指定字符串长度
	 */
	public static String getNumRandomStr(int length) {
		Random randGen = null;
		char[] numbersAndLetters = null;
		Object initLock = new Object();
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();
					numbersAndLetters = ("0123456789").toCharArray();
				}
			}
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 说明：获取当前时间，返回的格式为：yyyMMddHHmmss
	 * 
	 * @return String yyyMMddHHmmss
	 */
	public static String getNowTime() {
		String nowTime = "";
		Date date = new Date();
		SimpleDateFormat from = new SimpleDateFormat("yyyMMddHHmmss");
		nowTime = from.format(date);
		return nowTime;
	}

	/**
	 * 说明：获取当前时间戳，Unix时间戳格式
	 * 
	 * @return String
	 */
	public static String getTimestamp() {
		String nowTime = Long.toString(new Date().getTime() / 1000);
		return nowTime;
	}

	/**当前线程休息miliSeconds毫秒*/
	public static void sleep(int miliSeconds){
		try {
			Thread.sleep(miliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String getRandNum(int num) {
		String res="";
		if (num<1) return res;
		String arr[]={"0","1","2","3","4","5","6","7","8","9"};
		for (int i = 0; i < num; i++) {
			Random ran = new Random();
			int index=ran.nextInt(10);
			res=res+arr[index];
		}
		return res;
	}
	
	/**
	 * 说明：获取当前时间+指定位数随机数字，返回的格式为：yyyMMddHHmmss+rand
	 * 
	 * @return String yyyMMddHHmmss
	 */
	public static String getStrRandNum(int num) {
		return getNowTime()+getRandNum(num);
	}

	public static void delTmpPath() {
		// 清空xml文件生成目录
		DisPatchFile.delAllFile(DispatchConf.SuitsXmlPath);
		// TestNG输出目录
		DisPatchFile.delAllFile(DispatchConf.TestNgOutPath);
	}
	
}
