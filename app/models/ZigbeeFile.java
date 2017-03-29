package models;

import java.util.*;

import javax.persistence.*;

import models.Computer.Page;

import play.data.format.*;
import play.data.validation.*;

import play.db.jpa.*;

/**
 * USER entity managed by JPA
 */
@Entity
@Table(name = "zigbeefilelog")
public class ZigbeeFile{

	@Id   
    @GeneratedValue
    public Long id;
	
	@Formats.NonEmpty
	public String email;

	public String filedescribe;
	
	@Constraints.Required
	public String encodings;
	
	@Constraints.Required
	public String filepath;
	
	public Date filedate;

	public ZigbeeFile(){
		this.filedate = new Date();
	}
	
	
	public static ZigbeeFile findById(Long id) {
		return JPA.em().find(ZigbeeFile.class, id);
	}
	
	/**
     * Update this zigbeeFiles.
     */
    public void update(Long id) {
        JPA.em().merge(this);
    }
	
    /**
     * Insert this new computer.
     */
    public void save() {
        this.id = id;
        JPA.em().persist(this);
    }
    
    /**
     * Delete this computer.
     */
    public void delete() {
        JPA.em().remove(this);
    }
    
	/**
	 * Retrieve all zigbeeFiles.
	 */
	public static List<ZigbeeFile> findAll() {
		return JPA.em().createQuery("from ZigbeeFile").getResultList();
//		return User.findAll();
	}

	/**
     * Return a page of computer
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page page(int page, int pageSize, String sortBy, String order, String filter) {
        if(page < 1) page = 1;
        Long total = (Long)JPA.em()
            .createQuery("select count(c) from ZigbeeFile c where lower(c.filedescribe) like ?")
            .setParameter(1, "%" + filter.toLowerCase() + "%")
            .getSingleResult();
        List<ZigbeeFile> data = JPA.em()
            .createQuery("from ZigbeeFile c where lower(c.filedescribe) like ? order by c." + sortBy + " " + order)
            .setParameter(1, "%" + filter.toLowerCase() + "%")
            .setFirstResult((page - 1) * pageSize)
            .setMaxResults(pageSize)
            .getResultList();
        return new Page(data, total, page, pageSize);
    }
    
    /**
     * Used to represent a computers page.
     */
    public static class Page {
        
        private final int pageSize;
        private final long totalRowCount;
        private final int pageIndex;
        private final List<ZigbeeFile> list;
        
        public Page(List<ZigbeeFile> data, long total, int page, int pageSize) {
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
        
        public List<ZigbeeFile> getList() {
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
