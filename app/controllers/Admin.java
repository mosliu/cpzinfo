package controllers;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import controllers.routes;
import controllers.Authenticate.Login;

import models.SecurityRole;
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
import views.html.admin.*;

public class Admin extends Controller {

	public static class User_Role {
		@Constraints.Required
		public Long uid;
		@Constraints.Required
		public Long rid;
		
		public boolean addordel;
	}

	/**
	 * List user
	 */
	@Transactional(readOnly = true)
	public static Result user() {
		User u = User.findByEmail(session("email"));
		return ok(user.render(form(User_Role.class),u));
	}
	/**
	 * List userrole
	 */
	@Transactional(readOnly = true)
	public static Result userroles() {
//		String str = request().getQueryString("selectuid");
//		System.out.println(str==null?"null":str);
		Map parameters = request().body().asFormUrlEncoded();
		String uuid =((String[]) parameters.get("selectuid"))[0];
		Long selectuid = 0L;
		try{
			selectuid = Long.parseLong(uuid);
		}catch(NumberFormatException ex){
			return ok("");
		}
		User u = User.findByID(selectuid);
		String rtn = "";
		for (Iterator iterator = u.roles.iterator(); iterator.hasNext();) {
			SecurityRole sr = (SecurityRole) iterator.next();
			rtn+=sr.roleDescribe+";";
		}
		return ok(rtn);
	}
	
	/**
	 * List user
	 */
	@Transactional
	public static Result usercontrol() {
		User rtnu = User.findByEmail(session("email"));
		Form<User_Role> urForm = form(User_Role.class).bindFromRequest();
		if (urForm.hasErrors()) {
			return badRequest(user.render(urForm,rtnu));
		} else {
			boolean ifadd = urForm.get().addordel;
			User u = User.findByID(urForm.get().uid);
			SecurityRole r = SecurityRole.findByID(urForm.get().rid);
			
			if(ifadd==false){
				//添加
				u.roles.add(r);
				u.save();
				flash("success", "用户 "+u.name+" 成功添加权限 \""+r.roleDescribe+"\"");
			}else{
				//删除
				u.roles.remove(r);
				u.save();
				flash("success", "用户 "+u.name+" 成功删除权限 \""+r.roleDescribe+"\"");
			}
			
			
			return ok(user.render(form(User_Role.class),rtnu));
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