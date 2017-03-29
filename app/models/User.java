package models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

/**
 * USER entity managed by JPA
 */
@Entity
@Table(name = "user")
public class User implements Subject{

	
	@Id
	@Constraints.Required
	public long id;
	
	@Constraints.Required
	@Formats.NonEmpty
	public String email;

	@Constraints.Required
	public String name;

	@Constraints.Required
	public String password;
	
	public int right;
	
	public String group;

	@ManyToMany
    public List<SecurityRole> roles;

    @ManyToMany
    public List<UserPermission> permissions;
	
    public static Map<String,String> options() {
        List<User> users = JPA.em().createQuery("from User order by name").getResultList();
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(User c: users) {
            options.put(""+c.id,c.name+":"+c.email);
        }
        return options;
    }
    
	public static User findByID(long id){
		return JPA.em().find(User.class, id);
	}
	
	public static User findByEmail(String email) {
		if(email == null){
			email = "";
			return null;
		}
		List<User> l = JPA.em().createQuery("from User where email =? ").setParameter(1,email).getResultList();
//		JPA.em().createQuery("from User where email = "+email).
		if(l.size()==1){
			return l.get(0);
		}else if(l.size()>1){
			System.out.println("===================error===================");
			for (User user : l) {
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
	public static List<User> findAll() {
		return JPA.em().createQuery("from User").getResultList();
//		return User.findAll();
	}

	/**
	 * Authenticate a User.
	 */
	public static User authenticate(String email, String password) {
		User u = findByEmail(email);
		
//		User u =  JPA.em().find(User.class,email, map);
		if(u!=null&&u.password.equals(password)){
			System.out.println(u);
			return u;
		}else{
			return null;
		}
//		return find.where().eq("email", email).eq("password", password)
//				.findUnique();
	}

	/**
     * Insert this new user.
     */
    public void save() {
        JPA.em().persist(this);
    }
    
	// --
	@Override
	public String toString() {
		return "User(" + email + ")";
	}

	@Override
	public List<? extends Permission> getPermissions() {
		return permissions;
	}

	@Override
	public List<? extends Role> getRoles() {
		return roles;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return ""+id;
	}

}
