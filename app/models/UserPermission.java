package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.JPA;
import be.objectify.deadbolt.core.models.Permission;

/**
 * USER entity managed by JPA
 */
@Entity
@Table(name = "user_permission")
public class UserPermission implements Permission{

	@Id
	@GeneratedValue
	public Long id;

    public String value;

	@Override
	public String getValue()
    {
        return value;
    }

	public static UserPermission findByID(long id){
		return JPA.em().find(UserPermission.class, id);
	}
	
	/**
     * Insert this new userpermission.
     */
    public void save() {
        JPA.em().persist(this);
    }
	
    public static List<UserPermission> findAll() {
		return JPA.em().createQuery("from UserPermission").getResultList();
//		return User.findAll();
	}

}
