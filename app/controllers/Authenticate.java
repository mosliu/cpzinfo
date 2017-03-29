package controllers;

import java.io.File;

import controllers.routes;

import models.User;
import net.liuxuan.utils.FilePlus;

import play.data.Form;
import static play.data.Form.*;
import play.data.validation.*;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;

public class Authenticate extends Controller {

	// -- Authentication

	public static class Login {
		@Constraints.Required
		@Email
		public String email;
		@Constraints.Required
		@MinLength(1)
		public String userpass;
		

		public String validate() {
			if (User.authenticate(email, userpass) == null) {
				return "Invalid user or password";
			}
			return null;
		}
 
	}

	/**
	 * Login page.
	 */
	public static Result login() {
		return ok(auth.render(form(Login.class)));
	}

	/**
	 * Handle login form submission.
	 */
	@Transactional(readOnly=true)
	public static Result login2() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
//		System.out.println("test2");
//		return redirect(routes.ZigbeeLogFileParser.index());
		if (loginForm.hasErrors()) {
			return badRequest(auth.render(loginForm));
		} else {
			
			User u = User.findByEmail(loginForm.get().email);
			if(u==null){
				session("name", "请登录");
				session("email", "");
			}
			else{
				session("name", u.name);
				session("email", u.email);
			}
			
			return redirect(routes.Application.index());
		}
	}
	
	/**
	 * Logout and clean the session.
	 */
	public static Result logout() {
		session().clear();
		flash("logout", "You've been logged out.");
		return redirect(routes.Authenticate.login());
	}
	
	

}