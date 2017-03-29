package models;

import java.util.*;

import javax.persistence.*;
import javax.validation.Constraint;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

import play.data.format.*;
import play.data.validation.*;

import play.db.jpa.*;

/**
 * ProductGroupInfo entity managed by JPA
 */
@Entity
@Table(name = "productgroupinfo")
public class ProductGroupInfo{

	
	@Id
	public long id;
	
	public String name;
	public String position;
	public Date birthday;
	public String birthday_lunar;
	public String cellphone;
	public String email;
	
	public String comment;


	public String active;
	

	
    public static Map<String,String> options() {
        List<ProductGroupInfo> users = JPA.em().createQuery("from User order by name").getResultList();
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(ProductGroupInfo c: users) {
            options.put(""+c.id,c.name+":"+c.email);
        }
        return options;
    }
    
	public static ProductGroupInfo findByID(long id){
		return JPA.em().find(ProductGroupInfo.class, id);
	}
	
	public static ProductGroupInfo findByEmail(String email) {
		if(email == null){
			email = "";
			return null;
		}
		List<ProductGroupInfo> l = JPA.em().createQuery("from ProductGroupInfo where email =? ").setParameter(1,email).getResultList();
//		JPA.em().createQuery("from User where email = "+email).
		if(l.size()==1){
			return l.get(0);
		}else if(l.size()>1){
			System.out.println("===================error===================");
			for (ProductGroupInfo user : l) {
				System.out.println(user.email);
			}
			System.out.println("=================error end=================");
			return null;
		}else{
			return null;
		}
//		return JPA.em().find(User.class, email);
	}

	
	// public static Map<String, String> options() {
	// List<USER> companies = JPA.em()
	// .createQuery("from Company order by name").getResultList();
	// LinkedHashMap<String, String> options = new LinkedHashMap<String,
	// String>();
	// for (USER c : companies) {
	// options.put(c.id.toString(), c.name);
	// }
	// return options;
	// }
	// -- Queries

//	public static Model.Finder<String, User> find = new Model.Finder(
//			String.class, User.class);

	/**
	 * Retrieve all users.
	 */
	public static List<ProductGroupInfo> findAll() {
		return JPA.em().createQuery("from ProductGroupInfo").getResultList();
//		return User.findAll();
	}

	/**
	 * Retrieve all users.
	 */
	public static List<ProductGroupInfo> findAllActived() {
		return JPA.em().createQuery("from ProductGroupInfo where active='1'").getResultList();
//		return User.findAll();
	}

	/**
     * Insert this new user.
     */
    public void save() {
        JPA.em().persist(this);
    }
    
}
