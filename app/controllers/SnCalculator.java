package controllers;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import models.Appinfo;
import models.SecurityRole;
import models.User;
import models.UserPermission;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.calcsn;
import views.html.index;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

@play.mvc.Security.Authenticated(Secured.class)
//@SubjectPresent
@Restrict({@Group("sncalc")})
//@Restrict({@Group({"hurdy", "gurdy"}), @Group("foo")})
//@Restrictions({@And("editor"), @And("viewer")})
public class SnCalculator extends Controller {

	// -- Actions

	/**
	 * Home page
	 */
	@Transactional(readOnly = true)
	public static Result index() {
		Appinfo.addcount(7L);
		User u = User.findByEmail(session("email"));
		try {
			int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
			System.out.println(maxKeyLen);
			System.out.println(System.getProperty("java.home"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(calcsn.render("ok", u));
	}

	@Transactional(readOnly = true)
	public static Result calc(String f) {
		User u = User.findByEmail(session("email"));
		byte[] keyA = { (byte) 234, 61, 78, 29, 55, 11, 27, 43, (byte) 142,
				(byte) 211, (byte) 166, 31, (byte) 155, (byte) 191, 22,
				(byte) 213, 126, (byte) 175, (byte) 139, (byte) 247, 99,
				(byte) 199, 44, 71, (byte) 183, 121, 23, 19, 42, 94, 59, 101 };
		String result1 = calcresult(f, keyA);
		byte[] keyB = { 18, 23, 54, 32, 78, 35, 123, 43, 76, (byte) 198,
				(byte) 205, (byte) 245, (byte) 146, (byte) 255, 1, 54, 32, 67,
				(byte) 188, (byte) 200, 101, 0, 4, 44, (byte) 245, 21, 90, 56,
				72, 88, 39, (byte) 202 };
		String result2 = calcresult(f, keyB);
		String result = "主板号：\r\n" + result1 + "\r\nGetSn\r\n" + result2;
		return ok(result);
	}

	@Transactional(readOnly = true)
	public static Result calc2(String f) {
		User u = User.findByEmail(session("email"));
		byte[] keyB = { 18, 23, 54, 32, 78, 35, 123, 43, 76, (byte) 198,
				(byte) 205, (byte) 245, (byte) 146, (byte) 255, 1, 54, 32, 67,
				(byte) 188, (byte) 200, 101, 0, 4, 44, (byte) 245, 21, 90, 56,
				72, 88, 39, (byte) 202 };
		String result = calcresult(f, keyB);
		return ok(result);
	}

	private static String calcresult(String toencode, byte[] keyC) {

		// String content = "TEST";
		// System.out.println("Original content:");
		// System.out.println(content);
		try {
			Security.addProvider(new BouncyCastleProvider());
			Key key = new SecretKeySpec(keyC, "AES");
			Cipher in = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

			in.init(Cipher.ENCRYPT_MODE, key);

			byte[] enc = in.doFinal(toencode.getBytes("UTF-8"));
			// System.out.println("Encrypted Content:");
			// System.out.println(new String(Hex.encode(enc)));
//			String encbase64 = new Base64Encoder().encode(enc);
//			String encbase64 = new BASE64Encoder().encode(enc).toString();
			String encbase64 = Base64.encodeBase64String(enc);
			
//			String encbase64 = "this is a temp method.new Base64Encoder().encode(enc)";
			
//			String encbase64 = new sun.misc.BASE64Encoder().encode(enc);
			// new Base64Encoder().encode(arg0)
			// System.out.println("Encrypted Content,Base64ed:");
			// System.out.println(encbase64);
			encbase64 = encbase64.substring(0, encbase64.length() - 4)
					.toUpperCase();
			// System.out.println(encbase64);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < encbase64.length(); i++) {
				if (i % 5 == 0 && i != 0) {
					sb.append("-");
					sb.append(encbase64.charAt(i));
				} else {
					sb.append(encbase64.charAt(i));
				}

			}
			String rst = sb.toString().replaceAll("\\+", "J")
					.replaceAll("/", "X").replaceAll("0", "O")
					.replaceAll("1", "I").replaceAll("2", "Z")
					.replaceAll("3", "T").replaceAll("4", "F")
					.replaceAll("5", "V").replaceAll("6", "X")
					.replaceAll("7", "S").replaceAll("8", "E")
					.replaceAll("9", "N");
			// System.out.println(rst);
			return rst;
			// Cipher out = Cipher.getInstance("AES/ECB/PKCS7Padding");
			// out.init(Cipher.DECRYPT_MODE, key);
			// byte[] dec = out.doFinal(enc);
			// System.out.println("Decrypted Content:");
			// System.out.println(new String(dec));
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}

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