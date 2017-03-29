package net.liuxuan.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ZXingPlus {
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 编码,示例，不使用
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param imgPath
	 */
	public void encode(String contents, int width, int height, String imgPath) {

		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "GBK");

		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
					BarcodeFormat.QR_CODE, width, height, hints);

			MatrixToImageWriter
					.writeToFile(bitMatrix, "png", new File(imgPath));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解码，示例，不使用
	 */
	public static void decodesample() {
		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			String filePath = "d:/temp/michael_zxing.png";
			File file = new File(filePath);
			BufferedImage image = ImageIO.read(file);
			;
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			Result result = formatReader.decode(binaryBitmap, hints);

			System.out.println("result = " + result.toString());
			System.out.println("resultFormat = " + result.getBarcodeFormat());
			System.out.println("resultText = " + result.getText());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String imgPath = "d:/temp/michael_zxing.png";
		// String contents = "Hello Michael(大大),welcome to Zxing!"
		// + "\nMichael’s blog [ http://sjsky.iteye.com ]"
		// + "\nEMail [ sjsky007@gmail.com ]" + "\nTwitter [ @suncto ]";
		// int width = 300, height = 300;
		// ZXingPlus handler = new ZXingPlus();
		// handler.encode(contents, width, height, imgPath);
		//
		// System.out.println("Michael ,you have finished zxing encode.");
		// System.out.println("========================================");
		// decodesample();
		// ZXingPlus.create1dbar("1234567890123", 200, 120, false);
//		BufferedImage bimg = ZXingPlus.create1dbar("1567935234785752975623765237479623459135",300,80,true);
		BufferedImage bimg = ZXingPlus.create1dbar("156793523435",300,80,true);
		try {
			ImageIO.write(bimg, "png", new File("d:/temp/a.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 图片两端所保留的空白的宽度
	 */
	private static int marginW = 20;
	/**
	 * 设置字号
	 */
//	private static int fonsize = 14;
	private static int fonsize = 14;
	
	public static int getFonsize() {
		return fonsize;
	}

	public static void setFonsize(int fonsize) {
		ZXingPlus.fonsize = fonsize;
	}

	/**
	 * 图片下方所保留的写文字地方的高度
	 */
	private static int marginUnder = fonsize + 20;

	/**
	 * 生成条形码,自动规划大小
	 * 
	 * @param contents
	 *            需要生成的内容
	 * @return
	 */
	public static BufferedImage create1dbar(String content) {
		int width = content.length() * 8;
		width = width > 100 ? width : 100;
		int height = width / 3;
		height = height>100?100:height;
		return create1dbar(content, width, height, true);

	}

	/**
	 * 生成条形码
	 * 
	 * @param contents
	 *            需要生成的内容
	 * @param desiredWidth
	 *            生成条形码的宽度,参考宽度，如字串太长自动调整
	 * @param desiredHeight
	 *            生成条形码的高度
	 * @param displayCode
	 *            是否在条形码下方显示内容
	 * @return
	 */
	public static BufferedImage create1dbar(String content, int desiredWidth,
			int desiredHeight, boolean displayCode) {
		int strsize = content.length();
		int computesize = strsize * 8;
		desiredWidth = desiredWidth + 2 * marginW;
		/*
		 * 计算大小，按照每个字符16宽度。
		 */
		desiredWidth = desiredWidth > computesize ? desiredWidth : computesize;
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.CODE_128, desiredWidth, desiredHeight);
			BufferedImage bimg = toBufferedImage(bitMatrix,
					new MatrixToImageConfig());

			if (displayCode) {
				drawStr(content, bitMatrix, bimg);
			}
			
			return bimg;
			// BufferedImage
			// ruseltBitmap=encodeAsBitmap(content,BarcodeFormat.CODE_128,
			// desiredWidth, desiredHeight);
			// return BufferedImage
			// MatrixToImageWriter.writeToFile(bitMatrix, suffix, new
			// File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage toBufferedImage(BitMatrix matrix,
			MatrixToImageConfig config) {
		// int[] coordinate = matrix.getTopLeftOnBit();
		// for (int i = 0; i < coordinate.length; i++) {
		// System.out.println(coordinate[i]);
		// }
		// coordinate = matrix.getBottomRightOnBit();
		// for (int i = 0; i < coordinate.length; i++) {
		// System.out.println(coordinate[i]);
		// }
		// // //{left,top,width,height}
		// coordinate = matrix.getEnclosingRectangle();
		// for (int i = 0; i < coordinate.length; i++) {
		// System.out.println(coordinate[i]);
		// }
		
		
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height + marginUnder,
				BufferedImage.TYPE_INT_ARGB);
		int onColor = config.getPixelOnColor();
		int offColor = config.getPixelOffColor();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void drawStr(String str, BitMatrix bitMatrix,
			BufferedImage image) {
		// {left,top,width,height}
		int[] coordinate = bitMatrix.getEnclosingRectangle();
//		//开始位置
		int startwidth = coordinate[0];
		int width = image.getWidth();
		int height = image.getHeight() - marginUnder;
		Graphics g = image.getGraphics();

		// 清除黑色
		// g.setColor(Color.WHITE);
		// g.fillRect(0, image.getHeight(), width, marginUnder);
		// 写入文字


		Font font = new Font("Arial", Font.BOLD, fonsize);
		
		// 创建一个FontMetrics对象
		FontMetrics fm = g.getFontMetrics(font); 
		//得到字高，这里没使用
		int fontheight = fm.getHeight();
		//得到字高宽
		int fontwidth = fm.stringWidth(str);
		//设置起始位置
		startwidth = (width-fontwidth)/2;
		g.setFont(font);
		g.setColor(Color.BLUE);
//		g.drawString(str, startwidth + coordinate[2] / 10, height + fonsize);
		g.drawString(str, startwidth, height + fonsize);
	}

	public static BufferedImage create2dcode(String content) {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
//		// 指定纠错等级
//		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//		// 指定编码格式
//		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BufferedImage img = null;
		try {
			
			
			
//			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,					
//					BarcodeFormat.QR_CODE, 300, 300, hints);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,					
					BarcodeFormat.QR_CODE, 300, 300);
			img = MatrixToImageWriter.toBufferedImage(bitMatrix);
			MatrixToImageWriter
					.writeToFile(bitMatrix, "png", new File("d:/temp/a.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return img;

	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 读取一维和二维码
	 * 
	 * @param path
	 *            文件地址，全路径
	 * @return
	 */
	public static String read(String path) {
		return read(path, "UTF-8");
	}

	/**
	 * 读取一维和二维码
	 * 
	 * @param path
	 *            文件地址，全路径
	 * @param coding
	 *            文字编码
	 * @return
	 */
	public static String read(String path, String coding) {
		try {
			Reader reader = new MultiFormatReader();
			File file = new File(path);
			BufferedImage image;

			image = ImageIO.read(file);
			if (image == null) {
				return null;
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Result result;
			Hashtable hints = new Hashtable();
			hints.put(DecodeHintType.CHARACTER_SET, coding);
			// 解码设置编码方式为：utf-8
			result = new MultiFormatReader().decode(bitmap, hints);
			String resultStr = result.getText();
			return resultStr;

		} catch (Exception ex) {
			return null;
		}
	}

}
