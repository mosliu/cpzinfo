package controllers;

import java.io.File;


import controllers.Authenticate.Login;
import controllers.ElectricFaqParser.Uploader;

import models.User;
import net.liuxuan.utils.FilePlus;

import play.data.Form;
import static play.data.Form.*;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Security;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;

@Security.Authenticated(Secured.class)
public class Profile extends Controller {

	/**
	 * Describes the Uploader form.
	 */
	public static class ProfilePass {
		@Required
		public String userpass;
		@Required
		@MinLength(5)
		public String newPassword;
		@Required
		@MinLength(5)
		public String repeatPassword;

		public String validate() {

			if (newPassword.equals(repeatPassword)) {
				return null;
			}else{
				return "repeatPassword Error";
			}
		}
	}
	// -- Actions

	/**
	 * Home page
	 */
	@Transactional(readOnly=true)
	public static Result index() {
		User u = User.findByEmail(session("email"));
		return ok(profile.render(form(ProfilePass.class),u));
	}
	
	/**
	 * 更改密码
	 */
	@Transactional(readOnly=true)
	public static Result changePassword() {
		User u = User.findByEmail(session("email"));
		Form<ProfilePass> form = form(ProfilePass.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(profile.render(form,u));
		}
		ProfilePass passform = form.get();
		u.password = passform.newPassword;
		u.save();
		flash("success", "Password changed.");
		return redirect(routes.Authenticate.logout());
	}
	

}