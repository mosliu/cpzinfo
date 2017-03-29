package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import models.ElectricFaq.Page;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

@Entity
@Table(name = "faulttreatment")
public class FaultTreatment {

	@Id
	@GeneratedValue
	public Long id;

	@Formats.NonEmpty
	public String email;

	@Constraints.Required
	public String faultname;

	@Constraints.Required
	public String devicename;

	public Date occurdate;

	public String faultfrade;

	public String faultpart;

	@Constraints.Required
	public String faultsymptom;// 故障现象
	@Constraints.Required
	public String faultreporter;// 故障提交人
	@Constraints.Required
	public String faultdealer;// 故障处理人
	@Constraints.Required
	public String confirmation;// 故障确认
	@Constraints.Required
	public String examination;// 检查过程
	@Constraints.Required
	public String faultrootcause;// 故障原因
	@Constraints.Required
	public String troubleshoot;// 问题处理

	public String settlement;// 向后管理

	public String comment;// 备注

	public FaultTreatment() {
		this.occurdate = new Date();
	}

	public static FaultTreatment findById(Long id) {
		return JPA.em().find(FaultTreatment.class, id);
	}

	/**
	 * Update this zigbeeFiles.
	 */
	public void update() {
		JPA.em().merge(this);
	}

	/**
	 * Insert this new faq.
	 */
	public void save() {
//		this.id = id;
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
	public static List<FaultTreatment> findAll() {
		return JPA.em().createQuery("from FaultTreatment").getResultList();
		// return User.findAll();
	}

	/**
	 * Return a page of faq
	 * 
	 * @param page
	 *            Page to display
	 * @param pageSize
	 *            Number of faqs per page
	 * @param sortBy
	 *            faq property used for sorting
	 * @param order
	 *            Sort order (either or asc or desc)
	 * @param filter
	 *            Filter applied on the name column
	 */
	public static Page page(int page, int pageSize, String sortBy,
			String order, String filter) {
		if (page < 1)
			page = 1;
		String totalsql = "select count(c) ";
		String searchclause = "from FaultTreatment c ";
		if (filter == null || filter.equals("")) {

		} else {
			searchclause = searchclause + "where "
					+ "lower(c.faultname) like :queryparam "
					+ "or lower(c.devicename) like :queryparam "
					+ "or lower(c.occurdate) like :queryparam "
					+ "or lower(c.faultpart) like :queryparam "
					+ "or lower(c.faultsymptom) like :queryparam "
					+ "or lower(c.faultreporter) like :queryparam "
					+ "or lower(c.confirmation) like :queryparam "
					+ "or lower(c.examination) like :queryparam "
					+ "or lower(c.troubleshoot) like :queryparam "
					+ "or lower(c.faultrootcause) like :queryparam "
					+ "or lower(c.settlement) like :queryparam ";
		}
		Long total = 0L;
		if (filter == null || filter.equals("")) {
			total = (Long) JPA.em().createQuery(totalsql + searchclause)
					.getSingleResult();
		} else {
			total = (Long) JPA
					.em()
					.createQuery(totalsql + searchclause)
					.setParameter("queryparam",
							"%" + filter.toLowerCase() + "%").getSingleResult();
		}

		List<FaultTreatment> data;
		String listsql = searchclause + "order by c." + sortBy + " " + order;
		if (filter == null || filter.equals("")) {
			data = JPA.em().createQuery(listsql)
					.setFirstResult((page - 1) * pageSize)
					.setMaxResults(pageSize).getResultList();
		} else {
			data = JPA.em()
					.createQuery(listsql)
					.setParameter("queryparam",
							"%" + filter.toLowerCase() + "%").setFirstResult((page - 1) * pageSize)
							.setMaxResults(pageSize).getResultList();
		}
		
		return new Page(data, total, page, pageSize);
	}

	/**
	 * Used to represent a faqs page.
	 */
	public static class Page {

		private final int pageSize;
		private final long totalRowCount;
		private final int pageIndex;
		private final List<FaultTreatment> list;

		public Page(List<FaultTreatment> data, long total, int page,
				int pageSize) {
			this.list = data;
			this.totalRowCount = total;
			this.pageIndex = page;
			this.pageSize = pageSize;
		}

		public long getTotalRowCount() {
			return totalRowCount;
		}

		public int getPageLast() {
			// return (int) Math.ceil(totalRowCount/pageSize);
			if (totalRowCount % pageSize == 0) {
				return (int) (totalRowCount / pageSize);
			} else {
				return (int) (totalRowCount / pageSize) + 1;
			}
		}

		public int getPageIndex() {
			return pageIndex;
		}

		public List<FaultTreatment> getList() {
			return list;
		}

		public boolean hasPrev() {
			return pageIndex > 1;
		}

		public boolean hasNext() {
			return (totalRowCount / pageSize) >= pageIndex;
		}

		public String getDisplayXtoYofZ() {
			int start = ((pageIndex - 1) * pageSize + 1);
			int end = start + Math.min(pageSize, list.size()) - 1;
			return start + " to " + end + " of " + totalRowCount;
		}

	}
}
