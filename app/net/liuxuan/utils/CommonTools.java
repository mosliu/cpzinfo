package net.liuxuan.utils;

public class CommonTools {
	public static void main(String[] args) {
		System.out.println("=================");
		for (Object str : System.getProperties().keySet()) {
			System.out.print(str);
			System.out.print(":");
			System.out.println(System.getProperties().get(str));
		}
	}
}
