package models;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import play.data.format.*;
import play.data.validation.*;

import play.db.jpa.*;

/**
 * USER entity managed by JPA
 */
@Entity
@Table(name = "VersionControl")
public class VersionControl {

	// id号
	@Id
	@GeneratedValue
	public Long versionid;
	// 设备名称；
	@Constraints.Required
	// @Constraints.MinLength(4)
	public String device;
	// 语言类型：中文,英文,中英合版。
	public String language;
	// 特定版本：定制,非定制。
	public String versionkind;
	// 当前版本号；
	@Constraints.Required
	// @Constraints.MinLength(4)
	public String versionno;
	// 上一版本号；
	public String fatherversionno;
	public Long fatherversionid;
	// 修改人；
	@Constraints.Required
	public String modifier;
	// 修改时间；
	@Constraints.Required
	public String modifydate;
	// 修改原因；
	@Constraints.Required
	public String modifyreason;

	// 修改内容；
	@Constraints.Required
	public String modifycontent;
	// 打包内容；
	public String packagecontent;
	// 对应单片机信息；
	public String SCMinfo;
	// 是否在用的版本；
	public String shippingversion;

	// 录入者email
	public String email;

	public static VersionControl findById(Long versionid) {
		return JPA.em().find(VersionControl.class, versionid);
	}

	// 查重，如果重，返回true；
	public boolean findDuplicted() {

		if (!(versionid == null || versionid == 0)) {
			return false;
		}
		if (isEmtpy(device)) {
			return false;
		}
		if (isEmtpy(versionno)) {
			return false;
		}
		if (isEmtpy(language)) {
			return false;
		}

		List a = JPA
				.em()
				.createQuery(
						"from VersionControl where device = '" + device
								+ "' and versionno = '" + versionno
								+ "' and language = '" + language + "'")
				.getResultList();
		if (a.size() > 0) {
			return true;
		}
		return false;
	}

	// 查重，如果重，返回true；
	public void updatefatherversionid() {
		if (isEmtpy(this.device)) {
			return;
		}
		if (isEmtpy(this.fatherversionno)) {
			return;
		}
		if (isEmtpy(language)) {
			return;
		}

		List a = JPA
				.em()
				.createQuery(
						"from VersionControl where device = '" + this.device
								+ "' and versionno = '" + this.fatherversionno
								+ "' and language = '" + this.language + "'")
				.getResultList();
		if (a.size() > 1) {
		}
	}

	/**
	 * Retrieve all zigbeeFiles.
	 */
	public static List<VersionControl> findByDeviceAndLanguage(String _device,
			String _language) {

		if (isEmtpy(_device)) {
			return new ArrayList<VersionControl>();
		}
		if (isEmtpy(_language)) {
			return new ArrayList<VersionControl>();
		}

		List rtnList = JPA
				.em()
				.createQuery(
						"from VersionControl where device = '" + _device
								+ "' and language = '" + _language + "'")
				.getResultList();
		return rtnList;
		// return User.findAll();
	}

	/**
	 * Retrieve all zigbeeFiles.
	 */
	public static List<VersionControl> findAll() {
		return JPA.em().createQuery("from VersionControl").getResultList();
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
		String totalsql = "select count(c) from VersionControl c ";
		// if (filter == null || filter.equals("")) {
		//
		// } else {
		totalsql = totalsql + " where  lower(c.device) like :queryparam "
				+ "or lower(c.language) like :queryparam "
				+ "or lower(c.versionkind) like :queryparam "
				+ "or lower(c.versionno) like :queryparam "
				+ "or lower(c.fatherversionno) like :queryparam "
				+ "or lower(c.modifier) like :queryparam "
				+ "or lower(c.modifydate) like :queryparam "
				+ "or lower(c.modifyreason) like :queryparam "
				+ "or lower(c.modifycontent) like :queryparam "
				+ "or lower(c.packagecontent) like :queryparam "
				+ "or lower(c.SCMinfo) like :queryparam "
				+ "or lower(c.shippingversion) like :queryparam ";
		// }
		Long total = (Long) JPA.em().createQuery(totalsql)
				.setParameter("queryparam", "%" + filter.toLowerCase() + "%")
				.getSingleResult();

		String listsql = "from VersionControl c  ";
		if (filter == null || filter.equals("")) {

		} else {
			listsql = listsql + " where  lower(c.device) like :queryparam "
					+ "or lower(c.language) like :queryparam "
					+ "or lower(c.versionkind) like :queryparam "
					+ "or lower(c.versionno) like :queryparam "
					+ "or lower(c.fatherversionno) like :queryparam "
					+ "or lower(c.modifier) like :queryparam "
					+ "or lower(c.modifydate) like :queryparam "
					+ "or lower(c.modifyreason) like :queryparam "
					+ "or lower(c.modifycontent) like :queryparam "
					+ "or lower(c.packagecontent) like :queryparam "
					+ "or lower(c.SCMinfo) like :queryparam "
					+ "or lower(c.shippingversion) like :queryparam ";
		}

		listsql = listsql + " order by c." + sortBy + " " + order;

		List<VersionControl> data;
		if (filter == null || filter.equals("")) {
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
	 * Used to represent a faqs page.
	 */
	public static class Page {

		private final int pageSize;
		private final long totalRowCount;
		private final int pageIndex;
		private final List<VersionControl> list;

		public Page(List<VersionControl> data, long total, int page,
				int pageSize) {
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

		public List<VersionControl> getList() {
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
		updatefatherversionid();
		JPA.em().persist(this);
	}

	/**
	 * Delete this faq.
	 */
	public void delete() {
		JPA.em().remove(this);
	}

	public static void main(String[] args) {
	}

	public static boolean isEmtpy(String str) {
		return str == null || str.length() == 0;
	}

	public String validate() {

		boolean rtn = findDuplicted();
		if (rtn) {
			return "版本重复";
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

	public Long getVersionid() {
		return versionid;
	}

	public void setVersionid(Long versionid) {
		this.versionid = versionid;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getVersionkind() {
		return versionkind;
	}

	public void setVersionkind(String versionkind) {
		this.versionkind = versionkind;
	}

	public String getVersionno() {
		return versionno;
	}

	public void setVersionno(String versionno) {
		this.versionno = versionno;
	}

	public String getFatherversionno() {
		return fatherversionno;
	}

	public void setFatherversionno(String fatherversionno) {
		this.fatherversionno = fatherversionno;
	}

	public Long getFatherversionid() {
		return fatherversionid;
	}

	public void setFatherversionid(Long fatherversionid) {
		this.fatherversionid = fatherversionid;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifydate() {
		return modifydate;
	}

	public void setModifydate(String modifydate) {
		this.modifydate = modifydate;
	}

	public String getModifycontent() {
		return modifycontent;
	}

	public void setModifycontent(String modifycontent) {
		this.modifycontent = modifycontent;
	}

	public String getPackagecontent() {
		return packagecontent;
	}

	public void setPackagecontent(String packagecontent) {
		this.packagecontent = packagecontent;
	}

	public String getSCMinfo() {
		return SCMinfo;
	}

	public void setSCMinfo(String sCMinfo) {
		SCMinfo = sCMinfo;
	}

	public String getShippingversion() {
		return shippingversion;
	}

	public void setShippingversion(String shippingversion) {
		this.shippingversion = shippingversion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
