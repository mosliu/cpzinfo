package models;

import java.util.*;

import javax.persistence.*;

import models.VersionControl.Page;

import play.data.format.*;
import play.data.validation.*;

import play.db.jpa.*;

/**
 * Company entity managed by JPA
 */
@Entity
@Table(name = "devicedict")
public class DeviceDict {

	// id号
	@Id
	@GeneratedValue
	public Long dictid;
	// 中文
	public String wordch;
	// 英文
	public String worden;
	// 用户名
	public String username;
	// 用户mail
	public String useremail;
	// 是否确定采纳
	public String checked;
	// 备注
	public String comment;
	// 采纳人
	public String checkusername;
	// 采纳人email
	public String checkuseremail;

	public static DeviceDict findById(Long id) {
		return JPA.em().find(DeviceDict.class, id);
	}

	public void setCommitUser(User u) {
		if (u == null) {
			return;
		}
		username = u.name;
		useremail = u.email;
	}

	public void setcheckUser(User u) {
		if (u == null) {
			return;
		}
		checkusername = u.name;
		checkuseremail = u.email;
	}

	// 查重，如果重，返回true；
	public boolean findDuplicted() {

		if (!(dictid == null || dictid == 0)) {
			return false;
		}
		if (isEmtpy(wordch)) {
			return false;
		}
		if (isEmtpy(worden)) {
			return false;
		}

		List a = JPA
				.em()
				.createQuery(
						"from DeviceDict where wordch = '" + wordch
								+ "' and worden = '" + worden + "'")
				.getResultList();
		if (a.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Retrieve all zigbeeFiles.
	 */
	public static List<DeviceDict> findAll() {
		return JPA.em().createQuery("from DeviceDict").getResultList();
		// return User.findAll();
	}

	/**
	 * Return a page of dict
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
			String order, String filter, String morewhere) {
		if (page < 1)
			page = 1;
		String totalsql = "select count(c) from DeviceDict c ";
		boolean orclause = false;
		if (filter == null || filter.equals("")) {

		} else {
			totalsql = totalsql + " where  (lower(c.wordch) like :queryparam "
					+ "or lower(c.worden) like :queryparam "
					+ "or lower(c.username) like :queryparam "
					+ "or lower(c.useremail) like :queryparam "
					+ "or lower(c.checked) like :queryparam "
					+ "or lower(c.comment) like :queryparam "
					+ "or lower(c.checkusername) like :queryparam "
					+ "or lower(c.checkuseremail) like :queryparam) ";
			orclause = true;
		}
		if (!isEmtpy(morewhere)) {
			if(orclause){
				totalsql = totalsql + " and " + morewhere;
			}else{
				totalsql = totalsql + " where " + morewhere;
				orclause = false;
			}
		}
		Long total = 0L;
		if (filter == null || filter.equals("")) {
			total = (Long) JPA.em().createQuery(totalsql).getSingleResult();
		} else {
			total = (Long) JPA
					.em()
					.createQuery(totalsql)
					.setParameter("queryparam",
							"%" + filter.toLowerCase() + "%").getSingleResult();
		}

		String listsql = "from DeviceDict c  ";
		if (isEmtpy(filter)) {

		} else {
			listsql = listsql + " where  (lower(c.wordch) like :queryparam "
					+ "or lower(c.worden) like :queryparam "
					+ "or lower(c.username) like :queryparam "
					+ "or lower(c.useremail) like :queryparam "
					+ "or lower(c.checked) like :queryparam "
					+ "or lower(c.comment) like :queryparam "
					+ "or lower(c.checkusername) like :queryparam "
					+ "or lower(c.checkuseremail) like :queryparam )";
			orclause = true;
		}
		if (!isEmtpy(morewhere)) {
			if(orclause){
				listsql = listsql + " and " + morewhere;
			}else{
				listsql = listsql + " where " + morewhere;
				orclause = false;
			}
		}
		listsql = listsql + " order by c." + sortBy + " " + order;

		List<DeviceDict> data;
		if (isEmtpy(filter)) {
			data = JPA.em().createQuery(listsql)
					.setFirstResult((page - 1) * pageSize)
					.setMaxResults(pageSize).getResultList();
		} else {
			data = JPA
					.em()
					.createQuery(listsql)
					.setParameter("queryparam",
							"%" + filter.toLowerCase() + "%")
					.setFirstResult((page - 1) * pageSize)
					.setMaxResults(pageSize).getResultList();
		}

		return new Page(data, total, page, pageSize);
	}

	/**
	 * Return a page of dict
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
		return page(page, pageSize, sortBy, order, filter, null);
	}

	/**
	 * Used to represent a faqs page.
	 */
	public static class Page {

		private final int pageSize;
		private final long totalRowCount;
		private final int pageIndex;
		private final List<DeviceDict> list;

		public Page(List<DeviceDict> data, long total, int page, int pageSize) {
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
		public int getPageLast() {
//			return (int) Math.ceil(totalRowCount/pageSize);
			if(totalRowCount%pageSize==0){
				return (int) (totalRowCount/pageSize);
			}else{
				return (int) (totalRowCount/pageSize)+1;
			}
		}

		public List<DeviceDict> getList() {
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

	/**
	 * Update this version.
	 */
	public void update() {
		JPA.em().merge(this);
	}

	/**
	 * Insert this version.
	 */
	public void save() {
		JPA.em().persist(this);
	}

	/**
	 * Delete this faq.
	 */
	public void delete() {
		JPA.em().remove(this);
	}

	public static boolean isEmtpy(String str) {
		return str == null || str.length() == 0;
	}

	public String validate() {

		boolean rtn = findDuplicted();
		if (rtn) {
			return "重复的词汇";
		}
		return null;
		//
		// if (question == null || question.length() < 5) {
		// return "Invalid question";
		// }
		// if (answer == null || answer.length() < 5) {
		// return "Invalid answer";
		// }
		// return null;
	}

}
