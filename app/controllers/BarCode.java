package controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.lang3.StringUtils;

import models.Appinfo;
import models.User;
import net.liuxuan.utils.CharacterPlus;
import net.liuxuan.utils.ZXingPlus;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http.Response;
import play.mvc.Result;
import play.mvc.Security;
import views.html.barcode.barcode;

@Security.Authenticated(Secured.class)
public class BarCode extends Controller {
	// -- Actions

	// 计数增加1，id=9；
	public static final long appid = 9L;

	/**
		 * 
		 */
	@Transactional
	public static Result index() {
		Appinfo.addcount(appid);
		User u = User.findByEmail(session("email"));
		
		// return
		// ok(DeviceDict.render(form(DeviceDict.class).fill(DeviceDict.findById(
		// 3L)), "", u));
//		return ok(devicedict.render(form(DeviceDict.class), "", null, u));
		return ok(barcode.render("",u));
		// return ok("<b>" + a + "</b>").as("text/html");
	}
	@Transactional
	public static Result query(String f){
		User u = User.findByEmail(session("email"));
		return ok(barcode.render("",u));
	}
	public static Result pic(String str){

		//排除空和中文字
		if(CharacterPlus.hasChinese(str)||StringUtils.isBlank(str)){
			str = "bad input!!";
		}
		//去除前后空格
		str = StringUtils.strip(str);
		ZXingPlus.setFonsize(18);
//		BufferedImage img = ZXingPlus.create1dbar(str);
		BufferedImage img = ZXingPlus.create1dbar(str,300,90,true);
		//需要判断含有中文时排除掉。
		//写stream中
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(img,"png", os);
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		response().setContentType("image/png");
		InputStream fis = new ByteArrayInputStream(os.toByteArray());
//		InputStream a = new imagein
		;		
		return ok(fis);
	}
	
	public static Result pic2(String str){
		
		BufferedImage img = ZXingPlus.create2dcode(str);
		
		//写stream中
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(img,"png", os);
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		response().setContentType("image/png");
		InputStream fis = new ByteArrayInputStream(os.toByteArray());
//		InputStream a = new imagein
		;		
		return ok(fis);
	}
	
}
