package controllers;

import static play.data.Form.form;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import models.Appinfo;
import models.User;
import models.VersionControl;
import net.liuxuan.utils.Docx4JPlus;

import com.fasterxml.jackson.databind.JsonNode;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.versioncontrol.versioncontrol;
import views.html.versioncontrol.versioncontrollist;
import views.html.versioncontrol.versioncontrolreview;

/**
 * 版本管理
 * 
 * @author Moses
 * 
 */

@Security.Authenticated(Secured.class)
public class VersionControlParser extends Controller {

//	public static class Uploader {
//
//		//设备名称；
//		@Required
//		@MinLength(5)
//		public String device;
//		
//		
//		//语言类型：中文,英文,中英合版。
//		@Required
//		public String language;
//		
//		//特定版本：定制,非定制。
//		@Required
//		public String versionkind;
//		
//		//当前版本号；
//		@Required
//		public String versionno;
//		
//		//上一版本号；
//		public String fatherversionno;
//		public Long fatherversionid;
//		//修改人；
//		public String modifier;
//		//修改时间；
//		public String modifydate;
//		//修改内容；
//		public String modifycontent;
//		//打包内容；
//		public String packagecontent;
//		//对应单片机信息；
//		public String SCMinfo;
//		//是否在用的版本；
//		public String shippingversion;
//		
//		
////		public String validate() {
////
////			if (question == null || question.length() < 5) {
////				return "Invalid question";
////			}
////			if (answer == null || answer.length() < 5) {
////				return "Invalid answer";
////			}
////			return null;
////		}
//	}
	
	
	// -- Actions

	/**
	 * Home page
	 */
	@Transactional
	public static Result index() {
		//计数增加1，id=5；
    	Appinfo.addcount(5L);
		User u = User.findByEmail(session("email"));
		
//		return ok(versioncontrol.render(form(VersionControl.class).fill(VersionControl.findById( 3L)), "", u));
		return ok(versioncontrol.render(form(VersionControl.class), "", u));
		// return ok("<b>" + a + "</b>").as("text/html");
	}
	
	/**
	 * 处理提交
	 */
	@Transactional
	public static Result upload() {
		Form<VersionControl> form = form(VersionControl.class).bindFromRequest();
//		VersionControl vc1 = form.bindFromRequest().get();
		// 获取相应用户。
		User u = User.findByEmail(session("email"));
		String rtnstr = "";
		if (form.hasErrors()) { 
			// return badRequest(index2.render(form));
			// flash("error", "Error occurred！！！！！！！！！");
			// return badRequest("badRequest");
			// form.get().answer = "1111";
			return badRequest(versioncontrol.render(form, "", u));
		} else {
			VersionControl vc = form.get();
			vc.email=u.email;
			if(vc.versionid!=null&&vc.versionid!=0){
				rtnstr = "更改内容已提交";
				vc.update();
			}else{
				rtnstr = "新记录已提交";
				vc.save();
			}
//			if(faqinfo.id!=0){
//				faq= ElectricFaq.findById(faqinfo.id);
//				faq.answerdate = new Date();
//			}
//			faq.question = faqinfo.question;
//			faq.answer = faqinfo.answer;
//			faq.comment = faqinfo.comment;
//			if (faq.question == null || faq.question.equals("")) {
//				faq.question = "无";
//			}
//			if (faq.answer == null || faq.answer.equals("")) {
//				faq.answer = "无";
//			}
//			if (faq.comment == null || faq.comment.equals("")) {
//				faq.comment = "无";
//			}
			// 记录文件位置
		}

		
//		return ok(electricfag.render(form, "", u));
		flash("success",rtnstr);
		return ok(versioncontrol.render(form(VersionControl.class), "", u));
		// return ok("File uploaded");

	}

	
	
	/**
	 * 获取列表
	 */
	@Transactional
	public static Result list(int page, String sortBy, String order,
			String filter) {
		// 计数增加1，id=5；
		Appinfo.addcount(5L);
		// return ok(index.render(form(Hello.class)));
		// List<ZigbeeFile> allfile= ZigbeeFile.findAll();
		User u = User.findByEmail(session("email"));
		// return ok(zigbeelogfilelist.render(u,allfile));

		return ok(versioncontrollist.render(
				VersionControl.page(page, 10, sortBy, order, filter.trim()),
				sortBy, order, filter, u));

	}

	/**
	 * Display the 'edit form' of a existing Computer.
	 * 
	 * @param versionid
	 *            Id of the computer to edit
	 */
	@Transactional
	public static Result review(Long versionid) {

		Appinfo.addcount(5L);
		VersionControl zf = VersionControl.findById(versionid);
		// String a = "document.getElementById('question').value='"
		// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('answer').value='"
		// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('comment').value='"
		// + zf.comment.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('id').value='" + orderid + "';";
		String a = "";
		User u = User.findByEmail(session("email"));
		return ok(versioncontrolreview.render(zf, a, u));
	}
	
	
	/**
	 * 
	 * 
	 * @param versionid
	 *            Id of the versionrecord to edit
	 */
	@Transactional
	public static Result edit(Long versionid) {
		Appinfo.addcount(5L);
		VersionControl zf = VersionControl.findById(versionid);
		// String a = "document.getElementById('question').value='"
		// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('answer').value='"
		// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('comment').value='"
		// + zf.comment.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('id').value='" + orderid + "';";
		String a = "";
		User u = User.findByEmail(session("email"));
		return ok(versioncontrol.render(form(VersionControl.class).fill(zf), "", u));
	}
	
	/**
	 * export download file
	 * 
	 * @param versionid
	 *            Id of the versionrecord to edit
	 */
	@Transactional
	public static Result downloadFile(Long versionid) {
		Appinfo.addcount(5L);
		VersionControl zf = VersionControl.findById(versionid);
		// String a = "document.getElementById('question').value='"
		// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('answer').value='"
		// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('comment').value='"
		// + zf.comment.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('id').value='" + orderid + "';";
		String a = "";
		User u = User.findByEmail(session("email"));
		
		try {
			a = Docx4JPlus.Test(zf);
		} catch (Docx4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File f = new File(a);
		if(f.exists()){
			return ok(f);
			
		}
		return ok(a);
	}

	/**
	 * 返回按设备和语言查询的当前版本列表
	 * 
	 * @param versionid
	 *            Id of the computer to edit
	 */
	@Transactional
	public static Result queryVersion() {
		Appinfo.addcount(5L);
//		VersionControl zf = VersionControl.findById(versionid);
		// String a = "document.getElementById('question').value='"
		// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('answer').value='"
		// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('comment').value='"
		// + zf.comment.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('id').value='" + orderid + "';";
		String a = "";
		Map<String,String[]> parameters = request().body().asFormUrlEncoded();
		
//		parameters.get(key)
	    String device = parameters.get("device")[0];
	    String language = parameters.get("language")[0];
	    List<VersionControl> lista = VersionControl.findByDeviceAndLanguage(device,language);
	    StringBuilder sb = new StringBuilder();
	    for (Iterator iterator = lista.iterator(); iterator.hasNext();) {
			VersionControl vc = (VersionControl) iterator.next();
			sb.append(vc.versionno).append(";&nbsp;&nbsp;&nbsp;");
		}
		User u = User.findByEmail(session("email"));
		
		return ok(sb.toString());
	}
	
	/**
	 * 返回按设备和语言查询的当前版本列表
	 * 
	 * @param versionid
	 *            Id of the computer to edit
	 */
	@Transactional
	public static Result queryVersionJson() {
		Appinfo.addcount(5L);
//		VersionControl zf = VersionControl.findById(versionid);
		// String a = "document.getElementById('question').value='"
		// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('answer').value='"
		// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('comment').value='"
		// + zf.comment.replaceAll("\r\n", "\\\\r\\\\n") + "';"
		// + "document.getElementById('id').value='" + orderid + "';";
		String a = "";
		
//		JsonNode json = request().body().asJson();
//		  if(json == null) {
//		    return badRequest("Expecting Json data");
//		  } else {
//		    String name = json.findPath("device").getTextValue();
//		    
//		  }
		
		
		Map<String,String[]> parameters = request().queryString();
		
//		parameters.get(key)
	    String device = parameters.get("device")[0];
	    String language = parameters.get("language")[0];
	    List<VersionControl> lista = VersionControl.findByDeviceAndLanguage(device,language);
	    StringBuilder sb = new StringBuilder();
	    List<String> liststr = new ArrayList<String>();
	    for (Iterator iterator = lista.iterator(); iterator.hasNext();) {
			VersionControl vc = (VersionControl) iterator.next();
			liststr.add(vc.versionno);
			sb.append(vc.versionno).append(";");
		}
		User u = User.findByEmail(session("email"));
		JsonNode jn = Json.toJson(liststr);
		return ok(jn);
	}
	
}
