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
@Table(name = "orderlist")
public class OrderList{

	@Id
    public Long orderid;
	
	public String productname;
	public String samplecompany;
	public String marketing;
	public String departmentmanager;
	public Date incomedate;
	public Date giveoutdate;
	public String cost;
	public String produceperiod;
	public String orderrequirement;
	public String orderpaperid;
	public String mechanicalordercontent;
	public String mechanicalorderdone;
	public String mechanicalengineer;
	public String electricalordercontent;
	public String electricalorderdone;
	public String electricalengineer;
	public String softcontent;
	public String softdone;
	public String softengineer;
	public String usermanualcontent;
	public String usermanualdone;
	public String packinglistcontent;
	public String packinglistdone;
	public String other;
	public String orderstandard;
	public String jszxmanager;
	public Date   jszxchecktime;
	public String producemanager;
	public String producechecktime;
	public String aftersales;
	public String comment;

	

	public static OrderList findById(Long orderid) {
		return JPA.em().find(OrderList.class, orderid);
	}
	
	/**
	 * Retrieve all zigbeeFiles.
	 */
	public static List<OrderList> findAll() {
		return JPA.em().createQuery("from OrderList").getResultList();
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
        long orderidstr = 999999999;
        if(filter.matches("\\d+")&&filter.length()<18){
        	orderidstr = Long.parseLong(filter);
        }
        String totalsql = "select count(c) from OrderList c " +
				"where lower(c.orderid) = ? " +
				"or lower(c.productname) like :queryparam " +
				"or lower(c.samplecompany) like :queryparam " +
				"or lower(c.marketing) like :queryparam " +
				"or lower(c.departmentmanager) like :queryparam " +
				"or lower(c.orderrequirement) like :queryparam " +
				"or lower(c.orderpaperid) like :queryparam " +
				"or lower(c.mechanicalordercontent) like :queryparam " +
				"or lower(c.mechanicalorderdone) like :queryparam " +
				"or lower(c.mechanicalengineer) like :queryparam " +
				"or lower(c.electricalordercontent) like :queryparam " +
				"or lower(c.electricalorderdone) like :queryparam " +
				"or lower(c.electricalengineer) like :queryparam " +
				"or lower(c.softcontent) like :queryparam " +
				"or lower(c.softdone) like :queryparam " +
				"or lower(c.softengineer) like :queryparam " +
				"or lower(c.usermanualcontent) like :queryparam " +
				"or lower(c.usermanualdone) like :queryparam " +
				"or lower(c.packinglistcontent) like :queryparam " +
				"or lower(c.packinglistdone) like :queryparam " +
				"or lower(c.other) like :queryparam " +
				"or lower(c.orderstandard) like :queryparam " +
				"or lower(c.comment) like :queryparam ";
		Long total = (Long)JPA.em()
            .createQuery(totalsql)
            .setParameter("queryparam", "%" + filter.toLowerCase() + "%")
            .setParameter(1, orderidstr)
            .getSingleResult();
        String listsql = "from OrderList c " +
				"where lower(c.orderid) = :queryparamlong " +
				"or lower(c.productname) like :queryparam " +
				"or lower(c.samplecompany) like :queryparam " +
				"or lower(c.marketing) like :queryparam " +
				"or lower(c.departmentmanager) like :queryparam " +
				"or lower(c.orderrequirement) like :queryparam " +
				"or lower(c.orderpaperid) like :queryparam " +
				"or lower(c.mechanicalordercontent) like :queryparam " +
				"or lower(c.mechanicalorderdone) like :queryparam " +
				"or lower(c.mechanicalengineer) like :queryparam " +
				"or lower(c.electricalordercontent) like :queryparam " +
				"or lower(c.electricalorderdone) like :queryparam " +
				"or lower(c.electricalengineer) like :queryparam " +
				"or lower(c.softcontent) like :queryparam " +
				"or lower(c.softdone) like :queryparam " +
				"or lower(c.softengineer) like :queryparam " +
				"or lower(c.usermanualcontent) like :queryparam " +
				"or lower(c.usermanualdone) like :queryparam " +
				"or lower(c.packinglistcontent) like :queryparam " +
				"or lower(c.packinglistdone) like :queryparam " +
				"or lower(c.other) like :queryparam " +
				"or lower(c.orderstandard) like :queryparam " +
				"or lower(c.comment) like :queryparam " +
				"order by c." + sortBy + " " + order;
		List<OrderList> data = JPA.em()
            .createQuery(listsql)
            .setParameter("queryparam", "%" + filter.toLowerCase() + "%")
            .setParameter("queryparamlong", orderidstr)
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
        private final List<OrderList> list;
        
        public Page(List<OrderList> data, long total, int page, int pageSize) {
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
        
        public List<OrderList> getList() {
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
