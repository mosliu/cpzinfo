package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.google.common.io.Files;

import models.Appinfo;
import models.Computer;
import models.User;
import models.ZigbeeFile;
import net.liuxuan.utils.FilePlus;

import play.api.Play;
import play.data.Form;
import static play.data.Form.*;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.Required;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Security;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;
import views.html.zigbeelog.*;

@Security.Authenticated(Secured.class)
public class ZigbeeLogFileParser extends Controller {
	public static final String savepath = "d:/play/logfile/zigbee/";

	/**
	 * Describes the Uploader form.
	 */
	public static class Uploader {
		@Required
		public String encoding;
		public String savefilename;
		public File logFile;
	}

	// -- Actions

	/**
	 * Home page
	 */
	@Transactional
	public static Result index() {
		//计数增加1，id=1；
		Appinfo.addcount(1L);
		User u = User.findByEmail(session("email"));
		return ok(zigbeelogfile.render(form(Uploader.class), "", u,0L));
		// return ok("<b>" + a + "</b>").as("text/html");
	}

	 /**
     * Display the 'edit form' of a existing Computer.
     *
     * @param id Id of the computer to edit
     */
    @Transactional(readOnly=true)
    public static Result review(Long id) {
    	ZigbeeFile zf = ZigbeeFile.findById(id);
    	String a = logfileparse(new File(zf.filepath), zf.encodings,"nil");
    	User u = User.findByEmail(session("email"));
    	return ok(zigbeelogfile.render(form(Uploader.class), a, u,id));
    }
    
    /**
     * Display the 'edit form' of a existing Computer.
     *
     * @param id Id of the computer to edit
     */
    @Transactional(readOnly=true)
    public static Result searchreview(Long id,String f) {
    	
    	ZigbeeFile zf = ZigbeeFile.findById(id);
    	if(f==null||f.isEmpty()==true){
    		f="nil";
    	}
    	String a = logfileparse(new File(zf.filepath), zf.encodings,f);
    	User u = User.findByEmail(session("email"));
    	return ok(zigbeelogfile.render(form(Uploader.class), a, u,id));
    }
	
	/**
	 * 处理上传
	 */
	@Transactional
	public static Result upload() {
		Form<Uploader> form = form(Uploader.class).bindFromRequest();
		String encoding = "gbk";
		String fileName = null;
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart logfilepart = body.getFile("logFile");
		// 获取相应用户。
		User u = User.findByEmail(session("email"));
		// 记录对象
		ZigbeeFile zf = new ZigbeeFile();
		zf.email = session("email");

		if (form.hasErrors()) {
			// return badRequest(index2.render(form));
			return badRequest("badRequest");
		} else {
			Uploader logfileinfo = form.get();
			encoding = logfileinfo.encoding;
			// 记录文件位置

			zf.encodings = encoding;
			if (logfileinfo.savefilename == null
					|| logfileinfo.savefilename.trim().equals("")) {
				fileName = logfilepart.getFilename();
				zf.filedescribe = "这个人很懒，他啥也没填。";
			} else {
				fileName = logfileinfo.savefilename + ".log";
				zf.filedescribe = logfileinfo.savefilename;
			}
		}

		if (logfilepart != null) {
			File logfile = logfilepart.getFile();
			// 创建文件目录
			File savedir = new File(savepath);
			// // 如果目录不存在就创建
			// if (!savedir.exists()) {
			// savedir.mkdirs();
			// }
			File imageFile = new File(savedir, fileName);
			// 记录文件位置
			zf.filepath = imageFile.getAbsolutePath();
			FilePlus.createFile(imageFile);
			// FileOutputStream fops = new FileOutputStream(imageFile);
			// // 将上传的文件信息保存到相应的文件目录里
			//
			// fops.write(Files.toByteArray(logfile));
			// fops.close();
			try {
				FilePlus.CopyFile(logfile, imageFile);
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: handle exception
			}

			// Files.createParentDirs(imageFile);
			// Files.copy(file,imageFile);
			// String a = FilePlus.ReadTextFileToString(file, "<br/>",
			// encoding);
			String a = logfileparse(logfile, encoding,"nil");
			// String b = new String(a.getBytes( "ISO8859-1"),"UTF-8");
			// return ok("<b>" + a + "</b>").as("text/html");
			// 保存这个文件
			zf.save();

			return ok(zigbeelogfile.render(form(Uploader.class), a, u,zf.id));
			// return ok("File uploaded");
		} else {
			flash("error", "Missing file");
			return redirect(routes.ZigbeeLogFileParser.index());
		}
	}

	/**
	 * 获取文件列表
	 */
	@Transactional(readOnly = true)
	public static Result list(int page, String sortBy, String order,
			String filter) {
		// return ok(index.render(form(Hello.class)));
		// List<ZigbeeFile> allfile= ZigbeeFile.findAll();
		User u = User.findByEmail(session("email"));
		// return ok(zigbeelogfilelist.render(u,allfile));

		return ok(zigbeelogfilelist.render(
				ZigbeeFile.page(page, 20, sortBy, order, filter), sortBy,
				order, filter, u));

	}

	public static String logfileparse(File logfile, String encoding,String filter) {
		StringBuilder sb = new StringBuilder();
		String content = FilePlus.ReadTextFileToString(logfile, "<br/>", "GBK");
		String[] strlist = content.split("<br/>");
		int indent = 0;
		List<String> csbhList = new ArrayList<String>();// 参数编号
		for (int i = 0; i < strlist.length; i++) {
			if(!filter.equalsIgnoreCase("nil")){
				if(strlist[i].toLowerCase().contains(filter.toLowerCase())){
					//包含则处理
				}else{
					continue;
				}
					
			}
			if (strlist[i].startsWith("帧同步字:FBFB")) {
				sb.append("<br/><fieldset class=\"field_set\">");
				Scanner scanner = new Scanner(strlist[i]);
				while (scanner.hasNext()) {
					String str = scanner.next();
					if (str.startsWith("帧同步字")) {
						sb.append("<p>").append(str).append("</p>");
					} else if (str.startsWith("设备编号")) {
						sb.append("<p class='bluefont'>").append(str)
								.append("</p>");
					} else if (str.startsWith("参数个数")) {
						sb.append("<p class='tealfont'>").append(str)
								.append("</p>");
					} else if (str.startsWith("参数编号")) {
						sb.append("<p class='tealfont'>").append(str)
								.append("</p>");
						indent++;
						String record = str.replaceAll("参数编号：", "");
						str = scanner.next();
						if (str.startsWith("参数名称")) {
							sb.append("<p class='tealfont'>");
							for (int j = 0; j < indent; j++) {
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
							}
							sb.append(str).append("</p>");
						}
						record = record + "：" + str.replaceAll("参数名称：", "");
						csbhList.add(record);
					} else if (str.startsWith("参数长度")) {
						sb.append("<p class='tealfont'>");
						for (int j = 0; j < indent; j++) {
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
						}
						sb.append(str).append("</p>");
						// 显示数据
						sb.append("<p class='fuchsiafont'>");
						for (int j = 0; j < indent; j++) {
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
						}
						sb.append(scanner.next()).append("</p>");
						indent--;
					} else if (str.startsWith("数据种类：")) {
						sb.append("<p class='maroonfont'>").append(str)
								.append("</p>");
					} else if (str.startsWith("起始时间")) {
						sb.append("<p>").append(str).append("&nbsp;")
								.append(scanner.next()).append("</p>");
					} else if (str.startsWith("开始时间")) {
						sb.append("<p>").append(str).append("&nbsp;")
								.append(scanner.next()).append("</p>");
					} else if (str.startsWith("开机时间")) {
						sb.append("<p>").append(str).append("&nbsp;")
								.append(scanner.next()).append("</p>");
					} else if (str.startsWith("关机时间")) {
						sb.append("<p>").append(str).append("&nbsp;")
								.append(scanner.next()).append("</p>");
					} else if (str.startsWith("停止时间")) {
						sb.append("<p>").append(str).append("&nbsp;")
								.append(scanner.next()).append("</p>");
					} else if (str.startsWith("数值编号")) {
						sb.append("<p class='olivefont'>").append(str)
								.append("</p>");
					} else if (str.startsWith("数值个数")) {
						sb.append("<p class='olivefont'>").append(str)
								.append("</p>");
					} else if (str.startsWith("数值")) {
						sb.append("<p class='olivefont'>");
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
						sb.append(str).append("</p>");
					} else if (str.startsWith("结果个数")) {
						sb.append("<p class='purplefont'>").append(str)
								.append("</p>");
					} else if (str.startsWith("结果")) {
						sb.append("<p class='purplefont'>");
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
						sb.append(str).append("</p>");
					} else {

						sb.append("<p>").append(str).append("</p>");
					}
				}
				sb.append("</fieldset>");

			} else if (strlist[i].length() == 0) {
				// do nothing
			} else {
				sb.append("<p class='redfont'><b>").append(strlist[i])
						.append("</b></p>");
			}
		}
		sb.append("<fieldset><b>");
		sb.append("<legend>参数统计</legend>");

		sb.append("上报的参数有" + csbhList.size() + "个:");
		Collections.sort(csbhList);
		sb.append("<ul>");
		for (String str : csbhList) {
			sb.append("<li>").append(str).append("</li><br/>");
		}
		sb.append("</ul>");
		sb.append("</b></fieldset>");
		return sb.toString();

	}

}
