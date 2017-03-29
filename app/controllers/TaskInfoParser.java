package controllers;

import static play.data.Form.form;
import models.Appinfo;
import models.FaultTreatment;
import models.TaskInfo;
import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.faulttreament.faulttreament;
import views.html.faulttreament.faulttreamentlist;
import views.html.taskinfo.*;
/**
 * 故障处理
 * 
 * @author Moses
 * @version 2013.03.14
 * 
 */

@Security.Authenticated(Secured.class)
public class TaskInfoParser extends Controller {
	// -- Actions

		public static final long appid = 11L;

		/**
		 * 获取列表
		 */
		@Transactional
		public static Result list(int page, String sortBy, String order,
				String filter) {
			
			return listbyuser(page,sortBy,order,filter,-1);
		}
		
		/**
		 * 获取列表,by User
		 */
		@Transactional
		public static Result listbyuser(int page, String sortBy, String order,
				String filter ,int userid) {
			// 计数增加1，
			Appinfo.addcount(appid);
			// return ok(index.render(form(Hello.class)));
			// List<ZigbeeFile> allfile= ZigbeeFile.findAll();
			User u = User.findByEmail(session("email"));
			// return ok(zigbeelogfilelist.render(u,allfile));
			
			return ok(taskinfolist.render( 
					TaskInfo.page(page, 30, sortBy, order, filter.trim(),(int) u.id),
					sortBy, order, filter, u));
		}
		
		/**
		 * 
		 */
		@Transactional
		public static Result index() {
			// 计数增加1，
			Appinfo.addcount(appid);
			User u = User.findByEmail(session("email"));
			
			
			//为了设置时间。
			TaskInfo ti = new TaskInfo();
			Form<TaskInfo> form = form(TaskInfo.class).fill(ti);
			
//			return ok(DeviceDict.render(form(DeviceDict.class).fill(DeviceDict.findById( 3L)), "", u));
			return ok(taskinfo.render(form, "", u));
			// return ok("<b>" + a + "</b>").as("text/html");
		}
		
		/**
		 * 处理提交
		 */
		@Transactional
		public static Result upload() {
			Form<TaskInfo> form = form(TaskInfo.class).bindFromRequest();
//			DeviceDict vc1 = form.bindFromRequest().get();
			// 获取相应用户。
			User u = User.findByEmail(session("email"));
			String rtnstr = "";
			if (form.hasErrors()) { 
				// return badRequest(index2.render(form));
				 flash("error", "Error occurred！！！！！！！！！");
				// return badRequest("badRequest");
				// form.get().answer = "1111";
				return badRequest(taskinfo.render(form, "", u));
								   
			} else {
				TaskInfo ti = form.get();
//				ti.email=u.email;
//				if(tc.checked.equals("是")){
//					tc.setcheckUser(u);
//				}
//				
				if(ti.taskInfoId!=null&&ti.taskInfoId!=0){
					rtnstr = "更改内容已提交";
					ti.update();
				}else{
					rtnstr = "新纪录已提交";
					ti.save();
				}
			}

			
//			return ok(electricfag.render(form, "", u));
			flash("success",rtnstr);
			return ok(taskinfo.render(form(TaskInfo.class), "", u));
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
			TaskInfo ti = TaskInfo.findById(id);
			Form<TaskInfo> form = form(TaskInfo.class).fill(ti);
			
			User u = User.findByEmail(session("email"));
			
			return ok(taskinfo.render(form, "", u));
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
