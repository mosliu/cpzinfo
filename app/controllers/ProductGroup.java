package controllers;

import java.io.File;

import models.Appinfo;
import models.ProductGroupInfo;
import models.User;
import net.liuxuan.utils.FilePlus;

import play.data.Form;
import static play.data.Form.*;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.Required;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Security;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;

@Security.Authenticated(Secured.class)
public class ProductGroup extends Controller {

	// -- Actions

	/**
	 * Home page
	 */
	@Transactional
	public static Result index() {
		// 计数增加1，id=2；
		Appinfo.addcount(2L);
		User u = User.findByEmail(session("email"));
//		u.permissions.contains(arg0)
		return ok(productgroupinfos.render("ok", u,ProductGroupInfo.findAllActived()));
	}

}