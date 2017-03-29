package controllers;

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
import views.html.formulas.formulas;

@Security.Authenticated(Secured.class)
public class FormulaCompute extends Controller {
	// -- Actions

	// 计数增加1，id=9；
	public static final long appid = 10L;

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
		return ok(formulas.render("",u));
		// return ok("<b>" + a + "</b>").as("text/html");
	}
	@Transactional
	public static Result query(String f){
		User u = User.findByEmail(session("email"));
		return ok(formulas.render("",u));
	}
	
	
}
