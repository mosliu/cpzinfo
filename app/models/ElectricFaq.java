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
@Table(name = "electricfaq")
public class ElectricFaq{

	@Id   
    @GeneratedValue
    public Long id;
	
	@Formats.NonEmpty
	public String email;

	@Constraints.Required
	public String question;
	
	@Constraints.Required
	public String answer;
	
	public String comment;
	
	public Date answerdate;

	public ElectricFaq(){
		this.answerdate = new Date();
	}
	
	
	public static ElectricFaq findById(Long id) {
		return JPA.em().find(ElectricFaq.class, id);
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
    
	/**
	 * Retrieve all zigbeeFiles.
	 */
	public static List<ElectricFaq> findAll() {
		return JPA.em().createQuery("from ElectricFaq").getResultList();
//		return User.findAll();
	}

	/**
     * Return a page of faq
     *
     * @param page Page to display
     * @param pageSize Number of faqs per page
     * @param sortBy faq property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page page(int page, int pageSize, String sortBy, String order, String filter) {
        if(page < 1) page = 1;
        Long total = (Long)JPA.em()
            .createQuery("select count(c) from ElectricFaq c where lower(c.question) like ? or lower(c.answer) like ? ")
            .setParameter(1, "%" + filter.toLowerCase() + "%")
            .setParameter(2, "%" + filter.toLowerCase() + "%")
            .getSingleResult();
        List<ElectricFaq> data = JPA.em()
            .createQuery("from ElectricFaq c where lower(c.question) like ?  or lower(c.answer) like ? order by c." + sortBy + " " + order)
            .setParameter(1, "%" + filter.toLowerCase() + "%")
            .setParameter(2, "%" + filter.toLowerCase() + "%")
            .setFirstResult((page - 1) * pageSize)
            .setMaxResults(pageSize)
            .getResultList();
        return new Page(data, total, page, pageSize);
    }
    
    /**
     * Used to represent a faqs page.
     */
    public static class Page {
        
        private final int pageSize;
        private final long totalRowCount;
        private final int pageIndex;
        private final List<ElectricFaq> list;
        
        public Page(List<ElectricFaq> data, long total, int page, int pageSize) {
            this.list = data;
            this.totalRowCount = total;
            this.pageIndex = page;
            this.pageSize = pageSize;
        }
        
        public long getTotalRowCount() {
            return totalRowCount;
        }
        
        public int getPageIndex() {
            return pageIndex;
        }
        
        public List<ElectricFaq> getList() {
            return list;
        }
        
        public boolean hasPrev() {
            return pageIndex > 1;
        }
        
        public boolean hasNext() {
            return (totalRowCount/pageSize) >= pageIndex;
        }
        
        public String getDisplayXtoYofZ() {
            int start = ((pageIndex - 1) * pageSize + 1);
            int end = start + Math.min(pageSize, list.size()) - 1;
            return start + " to " + end + " of " + totalRowCount;
        }
        
    }

}
