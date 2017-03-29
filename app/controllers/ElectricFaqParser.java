package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.google.common.io.Files;

import controllers.ZigbeeLogFileParser.Uploader;

import models.Appinfo;
import models.ElectricFaq;
import models.User;
import net.liuxuan.utils.FilePlus;

import play.api.Play;
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
import views.html.electricfag.*;

@Security.Authenticated(Secured.class)
public class ElectricFaqParser extends Controller {
	/**
	 * Describes the Uploader form.
	 */
	public static class Uploader {
		@Required
		@MinLength(5)
		public String question;
		@Required
		@MinLength(5)
		public String answer;
		public String comment;
		public long id;

		public String validate() {

			if (question == null || question.length() < 5) {
				return "Invalid question";
			}
			if (answer == null || answer.length() < 5) {
				return "Invalid answer";
			}
			return null;
		}
	}

	// -- Actions

	/**
	 * Home page
	 */
	@Transactional
	public static Result index() {
		//计数增加1，id=3；
    	Appinfo.addcount(3L);
		User u = User.findByEmail(session("email"));
		return ok(electricfag.render(form(Uploader.class), "", u));
		// return ok("<b>" + a + "</b>").as("text/html");
	}

	/**
	 * 处理提交
	 */
	@Transactional
	public static Result upload() {
		Form<Uploader> form = form(Uploader.class).bindFromRequest();
		// 获取相应用户。
		User u = User.findByEmail(session("email"));

		// 记录对象
		ElectricFaq faq = new ElectricFaq();
		faq.email = session("email");
		faq.answerdate = new Date();
		if (form.hasErrors()) {
			// return badRequest(index2.render(form));
			// flash("error", "Error occurred！！！！！！！！！");
			// return badRequest("badRequest");
			// form.get().answer = "1111";
			return badRequest(electricfag.render(form, "", u));
		} else {
			Uploader faqinfo = form.get();
			if(faqinfo.id!=0){
				faq= ElectricFaq.findById(faqinfo.id);
				faq.answerdate = new Date();
			}
			faq.question = faqinfo.question;
			faq.answer = faqinfo.answer;
			faq.comment = faqinfo.comment;
			if (faq.question == null || faq.question.equals("")) {
				faq.question = "无";
			}
			if (faq.answer == null || faq.answer.equals("")) {
				faq.answer = "无";
			}
			if (faq.comment == null || faq.comment.equals("")) {
				faq.comment = "无";
			}
			// 记录文件位置
		}

		
		// 保存这个数据
		faq.save();
//		return ok(electricfag.render(form, "", u));
		flash("success","New record saved!");
		return ok(electricfag.render(form(Uploader.class), "", u));
		// return ok("File uploaded");

	}

	/**
	 * 获取文件列表
	 */
	@Transactional
	public static Result list(int page, String sortBy, String order,
			String filter) {
		//计数增加1，id=3；
    	Appinfo.addcount(3L);
		// return ok(index.render(form(Hello.class)));
		// List<ZigbeeFile> allfile= ZigbeeFile.findAll();
		User u = User.findByEmail(session("email"));
		// return ok(zigbeelogfilelist.render(u,allfile));

		return ok(electricfaglist.render(
				ElectricFaq.page(page, 10, sortBy, order, filter), sortBy,
				order, filter, u));

	}

	/**
	 * Display the 'edit form' of a existing Computer.
	 * 
	 * @param id
	 *            Id of the computer to edit
	 */
	@Transactional(readOnly = true)
	public static Result review(Long id) {
		ElectricFaq zf = ElectricFaq.findById(id);
		Form<Uploader> form = form(Uploader.class);
		Uploader aaaa = new Uploader();
		aaaa.answer = zf.answer;
		form.fill(aaaa);
		String a = "document.getElementById('question').value='"
				+ zf.question.replaceAll("\r\n", "\\\\r\\\\n") + "';"
				+ "document.getElementById('answer').value='"
				+ zf.answer.replaceAll("\r\n", "\\\\r\\\\n") + "';"
				+ "document.getElementById('comment').value='"
				+ zf.comment.replaceAll("\r\n", "\\\\r\\\\n") + "';"
				+ "document.getElementById('id').value='" + id + "';";
		User u = User.findByEmail(session("email"));
		return ok(electricfag.render(form(Uploader.class), a, u));
	}

}
