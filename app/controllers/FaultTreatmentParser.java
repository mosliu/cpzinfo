package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import controllers.ElectricFaqParser.Uploader;

import net.liuxuan.utils.SimilarityUtils;
import models.Appinfo;
import models.DeviceDict;
import models.ElectricFaq;
import models.FaultTreatment;
import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.devicedict.devicedict;
import views.html.electricfag.electricfag;
import views.html.faulttreament.faulttreamentlist;
import views.html.faulttreament.faulttreament;
/**
 * 故障处理
 * 
 * @author Moses
 * @version 2013.03.14
 * 
 */

@Security.Authenticated(Secured.class)
public class FaultTreatmentParser extends Controller {
	// -- Actions

		public static final long appid = 8L;

		/**
		 * 获取列表
		 */
		@Transactional
		public static Result list(int page, String sortBy, String order,
				String filter) {
			// 计数增加1，id=8；
			Appinfo.addcount(appid);
			// return ok(index.render(form(Hello.class)));
			// List<ZigbeeFile> allfile= ZigbeeFile.findAll();
			User u = User.findByEmail(session("email"));
			// return ok(zigbeelogfilelist.render(u,allfile));

			return ok(faulttreamentlist.render( 
					FaultTreatment.page(page, 30, sortBy, order, filter.trim()),
					sortBy, order, filter, u));
		}
		
		/**
		 * 
		 */
		@Transactional
		public static Result index() {
			// 计数增加1，id=8；
			Appinfo.addcount(appid);
			User u = User.findByEmail(session("email"));
			
//			return ok(DeviceDict.render(form(DeviceDict.class).fill(DeviceDict.findById( 3L)), "", u));
			return ok(faulttreament.render(form(FaultTreatment.class), "", u));
			// return ok("<b>" + a + "</b>").as("text/html");
		}
		
		/**
		 * 处理提交
		 */
		@Transactional
		public static Result upload() {
			Form<FaultTreatment> form = form(FaultTreatment.class).bindFromRequest();
//			DeviceDict vc1 = form.bindFromRequest().get();
			// 获取相应用户。
			User u = User.findByEmail(session("email"));
			String rtnstr = "";
			if (form.hasErrors()) { 
				// return badRequest(index2.render(form));
				// flash("error", "Error occurred！！！！！！！！！");
				// return badRequest("badRequest");
				// form.get().answer = "1111";
				return badRequest(faulttreament.render(form, "", u));
								   
			} else {
				FaultTreatment tc = form.get();
				tc.email=u.email;
//				if(tc.checked.equals("是")){
//					tc.setcheckUser(u);
//				}
//				
				if(tc.id!=null&&tc.id!=0){
					rtnstr = "更改内容已提交";
					tc.update();
				}else{
					rtnstr = "新纪录已提交";
					tc.save();
				}
			}

			
//			return ok(electricfag.render(form, "", u));
			flash("success",rtnstr);
			return ok(faulttreament.render(form(FaultTreatment.class), "", u));
			// return ok("File uploaded");

		}

		
		/**
		 * 
		 * 
		 * @param dictid
		 *            Id of the versionrecord to edit
		 */
		@Transactional
		public static Result edit(Long id) {
			FaultTreatment ft = FaultTreatment.findById(id);
			User u = User.findByEmail(session("email"));
			Form<FaultTreatment> form = form(FaultTreatment.class).fill(ft);
			
			return ok(faulttreament.render(form, "", u));
		}
		
		
		/**
		 * 
		 * 
		 * @param id
		 *            Id of the versionrecord to edit
		 */
		/*
		@Transactional
		public static Result edit(Long id) {
			Appinfo.addcount(6L);
			DeviceDict zf = DeviceDict.findById(id);
			// String a = "document.getElementById('question').value='"
			// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
			// + "document.getElementById('answer').value='"
			// + zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
			// + "document.getElementById('comment').value='"
			// + zf.comment.replaceAll("\r\n", "\\\\r\\\\n") + "';"
			// + "document.getElementById('id').value='" + orderid + "';";
			String a = "";
			User u = User.findByEmail(session("email"));
			
			List<DeviceDict> dictlist= DeviceDict.findAll();
			List<DeviceDict> dictlistout = new ArrayList<DeviceDict>();
			for (Iterator iterator = dictlist.iterator(); iterator.hasNext();) {
				DeviceDict dd = (DeviceDict) iterator.next();
				if(dd.id==zf.id){
					continue;
				}
				double bili1 = SimilarityUtils.levenshteinsSimilarity(zf.wordch, dd.wordch);
				double bili2 = SimilarityUtils.levenshteinsSimilarity(zf.worden, dd.worden);
				if(zf.wordch.length()<=3&&bili1>0.66){
					dictlistout.add(dd);
				}else if(bili1>0.74){
					dictlistout.add(dd);
				}
				if(bili2>0.80){
					dictlistout.add(dd);
				}
			}
//			System.out.println(dictlistout.size());
			return ok(devicedict.render(form(DeviceDict.class).fill(zf), "",dictlistout, u));
		}
		*/
		
}
