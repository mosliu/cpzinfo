package models;

import java.util.*;
import javax.persistence.*;


import play.db.jpa.*;

/**
 * USER entity managed by JPA
 */
@Entity
@Table(name = "appinfo")
public class Appinfo{

	@Id
	public Long id;

	public String appname;
	public String appinfo;
	public String appaddress;
	public Long appcount;
	public String appstyle;
	public String appicon;
	public long appinuse;

	public static Appinfo findById(Long id) {
		return JPA.em().find(Appinfo.class, id);
	}

	/**
	 * Retrieve all users.
	 */
	public static List<Appinfo> findAll() {
		return JPA.em().createQuery("from Appinfo order by id").getResultList();
//		return User.findAll();
	}

	public static void addcount(Long id){
		Appinfo c = findById(id);
		c.appcount = c.appcount +1;
		c.save();
	}
	/**
     * Insert this new faq.
     */
    public void save() {
        JPA.em().persist(this);
    }
    

}
