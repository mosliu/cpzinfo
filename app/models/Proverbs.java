package models;

import java.util.*;

import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import play.db.jpa.*;

/**
 * USER entity managed by JPA
 */
@Entity
@Table(name = "proverb")
public class Proverbs{

	@Id   
    @GeneratedValue
    public Long id;
	
	@Formats.NonEmpty
	public String proverbcontent;

	@Constraints.Required
	public String provercite;
	
	public Proverbs(){
	}
	
	
	public static Proverbs findById(Long id) {
		return JPA.em().find(Proverbs.class, id);
	}
	
	public static Proverbs getRandom() {
		long total = (Long)JPA.em().createQuery("select count(c) from Proverbs c ").getSingleResult();
		if(total==0){
			return null;
		}
		long id = System.currentTimeMillis()%total;
		return JPA.em().find(Proverbs.class, id);
	}
	
	
	/**
     * Update this zigbeeFiles.
     */
    public void update(Long id) {
        JPA.em().merge(this);
    }
	
    /**
     * Insert this new faq.
     */
    public void save() {
        this.id = id;
        JPA.em().persist(this);
    }
    
    /**
     * Delete this faq.
     */
    public void delete() {
        JPA.em().remove(this);
    }
    

}
