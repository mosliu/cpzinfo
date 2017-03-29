package net.liuxuan.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimilarityUtils {

	/**
	 * 编辑距离的两字符串相似度 Levenshtein Distance 算法实现
	 * 
	 * @author chenlb 2008-6-24 下午06:41:55
	 */

	private int min2(int one, int two, int three) {
		int min = one;
		if (two < min) {
			min = two;
		}
		if (three < min) {
			min = three;
		}
		return min;
	}

	// 得到最小值
	private static int min(int... is) {
		int min = Integer.MAX_VALUE;
		for (int i : is) {
			if (min > i) {
				min = i;
			}
		}
		return min;
	}

	public static int levenshtein(String src, String tar) {
		// 计算两个字符串的长度。
		int srcsize = src.length();
		int tarsize = tar.length();
		// 建立上面说的数组，比字符长度大一个空间
		int d[][]; // 矩阵
		int i; // 遍历str1的
		int j; // 遍历str2的
		char ch1; // str1的
		char ch2; // str2的
		int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		if (srcsize == 0) {
			return tarsize;
		}
		if (tarsize == 0) {
			return srcsize;
		}
		d = new int[srcsize + 1][tarsize + 1];
		for (i = 0; i <= srcsize; i++) { // 初始化第一列
			d[i][0] = i;
		}
		for (j = 0; j <= tarsize; j++) { // 初始化第一行
			d[0][j] = j;
		}
		for (i = 1; i <= srcsize; i++) { // 遍历str1
			ch1 = src.charAt(i - 1);
			// 去匹配str2
			for (j = 1; j <= tarsize; j++) {
				ch2 = tar.charAt(j - 1);
				if (ch1 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}
				// 左边+1,上边+1, 左上角+temp取最小
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
						+ temp);
			}
		}
		return d[srcsize][tarsize];
	}

	public static double levenshteinsSimilarity(String str1, String str2) {
		int ld = levenshtein(str1, str2);
		return 1 - (double) ld / Math.max(str1.length(), str2.length());
	}

	public static double getCosineSimilarity(String doc1, String doc2){
		return getCosineSimilarity(doc1,doc2,false);
	}
	public static double getCosineSimilarity(String doc1, String doc2,
			boolean onlychinese) {
		if (doc1 != null && doc1.trim().length() > 0 && doc2 != null
				&& doc2.trim().length() > 0) {

			Map<Integer, int[]> AlgorithmMap = new HashMap<Integer, int[]>();

			// 将两个字符串中的中文字符以及出现的总数封装到，AlgorithmMap中
			for (int i = 0; i < doc1.length(); i++) {
				char d1 = doc1.charAt(i);
				int charIndex = -1;
				if (onlychinese == true) {
					// 仅汉字时
					if (isHanZi(d1)) {
						charIndex = getGB2312Id(d1);
						if (charIndex == -1) {
							continue;
						}
					}else{
						continue;
					}
				} else {
					// 含其他字符时
					charIndex = d1;
				}
				//放到列表中
				int[] fq = AlgorithmMap.get(charIndex);
				if (fq != null && fq.length == 2) {
					fq[0]++;
				} else {
					fq = new int[2];
					fq[0] = 1;
					fq[1] = 0;
					AlgorithmMap.put(charIndex, fq);
				}

			}

			for (int i = 0; i < doc2.length(); i++) {
				char d2 = doc2.charAt(i);
				int charIndex = -1;
				if (onlychinese == true) {
					// 仅汉字时
					if (isHanZi(d2)) {
						charIndex = getGB2312Id(d2);
						if (charIndex == -1) {
							continue;
						}
					}else{
						continue;
					}
				} else {
					// 含其他字符时
					charIndex = d2;
				}
				//放到列表中
				int[] fq = AlgorithmMap.get(charIndex);
				if (fq != null && fq.length == 2) {
					fq[1]++;
				} else {
					fq = new int[2];
					fq[0] = 0;
					fq[1] = 1;
					AlgorithmMap.put(charIndex, fq);
				}
			}

			Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
			double sqdoc1 = 0;
			double sqdoc2 = 0;
			double denominator = 0;
			while (iterator.hasNext()) {
				int[] c = AlgorithmMap.get(iterator.next());
				denominator += c[0] * c[1];
				sqdoc1 += c[0] * c[0];
				sqdoc2 += c[1] * c[1];
			}

			return denominator / Math.sqrt(sqdoc1 * sqdoc2);
		} else {
			throw new NullPointerException(
					" the Document is null or have not cahrs!!");
		}
	}

	public static boolean isHanZi(char ch) {
		// 判断是否汉字
		return (ch >= 0x4E00 && ch <= 0x9FA5);

	}

	/**
	 * 根据输入的Unicode字符，获取它的GB2312编码或者ascii编码，
	 * 
	 * @param ch
	 *            输入的GB2312中文字符或者ASCII字符(128个)
	 * @return ch在GB2312中的位置，-1表示该字符不认识
	 */
	public static short getGB2312Id(char ch) {
		try {
			byte[] buffer = Character.toString(ch).getBytes("GB2312");
			if (buffer.length != 2) {
				// 正常情况下buffer应该是两个字节，否则说明ch不属于GB2312编码，故返回'?'，此时说明不认识该字符
				return -1;
			}
			int b0 = (int) (buffer[0] & 0x0FF) - 161; // 编码从A1开始，因此减去0xA1=161
			int b1 = (int) (buffer[1] & 0x0FF) - 161; // 第一个字符和最后一个字符没有汉字，因此每个区只收16*6-2=94个汉字
			return (short) (b0 * 94 + b1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void main(String[] args) {
		SimilarityUtils s = new SimilarityUtils();
		String str1 = "我你好";
		String str2 = "你好我a";
		// String str1 = "chenlb.blogjava.net";
		// String str2 = "chenlb.iteye.com";
		System.out.println(str1.length());
		System.out.println(str2.length());
		System.out.println("ld=" + s.levenshtein(str1, str2));
		System.out.println("sim=" + s.levenshteinsSimilarity(str1, str2));
		System.out.println("CosineSimilarity="
				+ s.getCosineSimilarity(str1, str2));
//		int a = '我';
//		System.out.println(a);
	}

}
