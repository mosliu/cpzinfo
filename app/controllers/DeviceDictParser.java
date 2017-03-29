package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import net.liuxuan.utils.SimilarityUtils;

import com.fasterxml.jackson.databind.JsonNode;

import controllers.ElectricFaqParser.Uploader;
import models.Appinfo;
import models.DeviceDict;
import models.ElectricFaq;
import models.User;
import models.DeviceDict;
import play.data.Form;
import static play.data.Form.*;
import play.data.validation.Constraints;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.electricfag.electricfag;
import views.html.devicedict.*;


/**
 * 辞典
 * 
 * @author Moses
 * 
 */

@Security.Authenticated(Secured.class)
public class DeviceDictParser extends Controller {
	
	
	// -- Actions

	public static final long appid = 6L;



	/**
	 * 获取列表
	 */
	@Transactional
	public static Result listnotrans(int page, String sortBy, String order,
			String filter,String whereclause) {
		// 计数增加1，id=5；
		Appinfo.addcount(appid);
		// return ok(index.render(form(Hello.class)));
		// List<ZigbeeFile> allfile= ZigbeeFile.findAll();
		User u = User.findByEmail(session("email"));
		// return ok(zigbeelogfilelist.render(u,allfile));

		return ok(devicedictlist.render( 
				DeviceDict.page(page, 30, sortBy, order, filter.trim()),
				sortBy, order, filter,whereclause, u));

	}
	
	
	/**
	 * 获取未翻译列表
	 */
	@Transactional
	public static Result list(int page, String sortBy, String order,
			String filter,String whereclause) {
		// 计数增加1，id=5；
		Appinfo.addcount(appid);
		// return ok(index.render(form(Hello.class)));
		// List<ZigbeeFile> allfile= ZigbeeFile.findAll();
		User u = User.findByEmail(session("email"));
		// return ok(zigbeelogfilelist.render(u,allfile));

		return ok(devicedictlist.render( 
				DeviceDict.page(page, 30, sortBy, order, filter.trim(),whereclause),
				sortBy, order, filter,whereclause, u));

	}
	
	/**
	 * 
	 */
	@Transactional
	public static Result index() {
		//计数增加1，id=5；
    	Appinfo.addcount(appid);
		User u = User.findByEmail(session("email"));
		
//		return ok(DeviceDict.render(form(DeviceDict.class).fill(DeviceDict.findById( 3L)), "", u));
		return ok(devicedict.render(form(DeviceDict.class), "",null, u));
		// return ok("<b>" + a + "</b>").as("text/html");
	}
	
	
	/**
	 * 处理提交
	 */
	@Transactional
	public static Result upload() {
		Form<DeviceDict> form = form(DeviceDict.class).bindFromRequest();
//		DeviceDict vc1 = form.bindFromRequest().get();
		// 获取相应用户。
		User u = User.findByEmail(session("email"));
		String rtnstr = "";
		if (form.hasErrors()) { 
			// return badRequest(index2.render(form));
			// flash("error", "Error occurred！！！！！！！！！");
			// return badRequest("badRequest");
			// form.get().answer = "1111";
			return badRequest(devicedict.render(form, "",null, u));
		} else {
			DeviceDict vc = form.get();
			vc.setCommitUser(u);
			
			if(vc.checked.equals("是")){
				vc.setcheckUser(u);
			}
			
			if(vc.dictid!=null&&vc.dictid!=0){
				rtnstr = "更改内容已提交";
				vc.update();
			}else{
				rtnstr = "新纪录已提交";
				vc.save();
			}
		}

		
//		return ok(electricfag.render(form, "", u));
		flash("success",rtnstr);
		return ok(devicedict.render(form(DeviceDict.class), "",null, u));
		// return ok("File uploaded");

	}

	
	
	/**
	 * 
	 * 
	 * @param dictid
	 *            Id of the versionrecord to edit
	 */
	@Transactional
	public static Result edit(Long dictid) {
		Appinfo.addcount(6L);
		DeviceDict zf = DeviceDict.findById(dictid);
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
			if(dd.dictid==zf.dictid){
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
//		System.out.println(dictlistout.size());
		return ok(devicedict.render(form(DeviceDict.class).fill(zf), "",dictlistout, u));
	}


	

	
}
