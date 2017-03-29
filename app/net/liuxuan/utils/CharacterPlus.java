package net.liuxuan.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 各种字符的unicode编码的范围：
 * 
 * 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
 * 
 * 数字：[0x30,0x39]（或十进制[48, 57]）
 * 
 * 小写字母：[0x61,0x7a]（或十进制[97, 122]）
 * 
 * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
 * 
 * 判断一个字符串是不是仅有中文、英文、数字、或者含有中文
 * 
 * 
 * @author Moses
 * 
 */
public class CharacterPlus {
	/**
	 * 是否仅有英文字母 null时返回false。空和仅有空格是返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLetter(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches("^[a-zA-Z]+$");
	}

	/**
	 * 是否仅有数字 null时返回false。空和仅有空格是返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDigit(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches("^[0-9]+$");
	}

	/**
	 * 是否仅有中文 null时返回false。空和仅有空格是返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches("^[\u4e00-\u9fa5]+$");
	}

	/**
	 * 是否仅有英文数字 null时返回false。空和仅有空格是返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLetterDigit(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches("^[a-z0-9A-Z]+$");
	}

	/**
	 * 是否仅有中英文和数字 null时返回false。空和仅有空格是返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLetterDigitChinese(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches("^[a-z0-9A-Z\u4e00-\u9fa5]+$");
	}

	/**
	 * 是否含有中英文和数字 null时返回false。
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasChinese(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches(".*[\u4e00-\u9fa5]+.*");
	}

}
