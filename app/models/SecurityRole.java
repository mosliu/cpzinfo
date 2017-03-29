package models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.JPA;
import be.objectify.deadbolt.core.models.Role;

/**
 * USER entity managed by JPA
 */
@Entity
@Table(name = "security_role")
public class SecurityRole implements Role{

	
	@Id
	@GeneratedValue
    public Long id;

    public String roleName;

    public String roleDescribe;
    @Override
	public String getName() {
		// TODO Auto-generated method stub
		return roleName;
	}


	public static Map<String,String> options() {
        List<SecurityRole> roles = JPA.em().createQuery("from SecurityRole order by id").getResultList();
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(SecurityRole c: roles) {
            options.put(""+c.id,c.roleDescribe);
        }
        return options;
    }
	
	public static SecurityRole findByID(long id){
		return JPA.em().find(SecurityRole.class, id);
	}

	/**
     * Insert this new userpermission.
     */
    public void save() {
        JPA.em().persist(this);
    }
	
	public static List<SecurityRole> findAll() {
		return JPA.em().createQuery("from SecurityRole").getResultList();
//		return User.findAll();
	}
	public static Long findAllcount() {
		Long total = (Long)JPA.em()
	            .createQuery("select count(c) from SecurityRole c")
	            .getSingleResult();
		return total;
//		return User.findAll();
	}

	

	
}
