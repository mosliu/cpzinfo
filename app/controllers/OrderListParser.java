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
import models.Computer;
import models.ElectricFaq;
import models.OrderList;
import models.User;
import models.VersionControl;
import models.ZigbeeFile;
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
import views.html.orderlist.*;

@Security.Authenticated(Secured.class)
public class OrderListParser extends Controller {
	
	// -- Actions


	/**
	 * 获取列表
	 */
	@Transactional
	public static Result list(int page, String sortBy, String order,
			String filter) {
		//计数增加1，id=4；
    	Appinfo.addcount(4L);
		// return ok(index.render(form(Hello.class)));
		// List<ZigbeeFile> allfile= ZigbeeFile.findAll();
		User u = User.findByEmail(session("email"));
		// return ok(zigbeelogfilelist.render(u,allfile));

		return ok(orderlist.render(
				OrderList.page(page, 10, sortBy, order, filter.trim()), sortBy,
				order, filter, u));

	}

	/**
	 * Display the 'edit form' of a existing Computer.
	 * 
	 * @param orderid
	 *            Id of the computer to edit
	 */
	@Transactional(readOnly = true)
	public static Result review(Long orderid) {
		System.out.println("http://"+request().host()+"/"+request().uri());
		OrderList zf = OrderList.findById(orderid);
//		String a = "document.getElementById('question').value='"
//				+ zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
//				+ "document.getElementById('answer').value='"
//				+ zf.aftersales.replaceAll("\r\n", "\\\\r\\\\n") + "';"
//				+ "document.getElementById('comment').value='"
//				+ zf.comment.replaceAll("\r\n", "\\\\r\\\\n") + "';"
//				+ "document.getElementById('id').value='" + orderid + "';";
		String a = "";
		User u = User.findByEmail(session("email"));
		return ok(order.render(form(OrderList.class).fill(zf),a, u));
	}

}
