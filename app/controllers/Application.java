package controllers;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import models.Appinfo;
import models.Proverbs;
import models.SecurityRole;
import models.User;
import models.UserPermission;
import net.liuxuan.utils.FilePlus;

import play.data.Form;
import static play.data.Form.*;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.Required;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;
import com.typesafe.plugin.*;

public class Application extends Controller {

	// -- Actions

	/**
	 * Home page
	 */
	@Transactional(readOnly = true)
	public static Result index() {
		User u = User.findByEmail(session("email"));
		List<Appinfo> list = Appinfo.findAll();
		Proverbs pv = Proverbs.getRandom();//每次刷新都给出新的格言
		if(pv!=null){
			session("proverb",pv.proverbcontent);
			session("proverbcite",pv.provercite);
		}
		return ok(index.render("ok", u, list));
	}

	@Transactional(readOnly = true)
	public static Result testemail() {
		MailerAPI mail = play.Play.application().plugin(MailerPlugin.class)
				.email();
		mail.setSubject("simplest mailer test");
		mail.setRecipient("some display name <liuxuan@labthink.cn>");
		mail.setFrom("some dispaly name <jira@labthink.cn>");
		mail.send("A text only message",
				"<html><body><p>An <b>html</b> message</p></body></html>");
		User u = User.findByEmail(session("email"));
		List<Appinfo> list = Appinfo.findAll();
		return ok(index.render("ok", u, list));
	}
	
	@Transactional(readOnly = false)
	public static Result upgradelog() {
		Appinfo.addcount(80L);
		
		
//		MailerAPI mail = play.Play.application().plugin(MailerPlugin.class)
//				.email();
//		mail.setSubject("simplest mailer test");
//		mail.addRecipient("some display name <liuxuan@labthink.cn>");
//		mail.addFrom("some dispaly name <jira@labthink.cn>");
//		mail.send("A text only message",
//				"<html><body><p>An <b>html</b> message</p></body></html>");
		User u = User.findByEmail(session("email"));
		List<Appinfo> list = Appinfo.findAll();
		return ok(upgradelog.render("ok", u));
	}

	@Transactional
	public static Result test() {
		User u = User.findByEmail(session("email"));
		// UserPermission p = new UserPermission();
		// p.value = "8";
		// p.save();
		// List<UserPermission> ll = (List<UserPermission>) u.getPermissions();
		// ll.add(p);
		// u.save();
		if (SecurityRole.findAllcount() == 0) {
			for (String roleName : Arrays
					.asList("foo", "bar", "hurdy", "gurdy")) {
				SecurityRole role = new SecurityRole();
				role.roleName = roleName;
				role.save();
			}
		}

		if (UserPermission.findAll().size() == 0) {
			UserPermission permission = new UserPermission();
			permission.value = "printers.edit";
			permission.save();
		}

		if (User.findAll().size() == 0) {
		}

		List<Appinfo> list = Appinfo.findAll();
		return ok(index.render("kkkkk", u, list));
	}

}