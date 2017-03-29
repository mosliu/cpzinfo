package models;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import models.FaultTreatment.Page;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

@Entity
@Table(name = "taskinfo")
public class TaskInfo {
	@Id
    @GeneratedValue
    @Column(name = "TASKINFOID", nullable = false)
	public Long taskInfoId; //任务编号
	
    @Column(name = "TASKNAME", nullable = false,length = 255)
    @Constraints.Required
    public String taskName;
    
    @Column(name = "ASSIGNEE", length = 255)
    public String assignee; // 任务分配人
    
    @Column(name = "ASSIGNEEID", nullable = false,length = 11)
    @Constraints.Required
    public Long assigneeId; // 任务分配人ID
    
    @Column(name = "CREATER", length = 255)
    public String creater; // 任务创建人
    
    @Column(name = "CREATERID", nullable = false,length = 11)
    @Constraints.Required
    public Long createrId; // 任务创建人ID
    
    @Column(name = "WORKHOUR", nullable = false,length = 255)
    @Constraints.Required
    public String workHour; // 工时
    
    @Column(name = "WORKHOURAUDIT", nullable = false,length = 255)
    public String workHourAudit; // 工时审计
    
    @Column(name = "PROGRESS")
	public int progress; //任务进度
    
    @Temporal(TemporalType.DATE) 
    @Column(name = "CREATETIME")
    public Date createTime; //创建时间
    
    @Temporal(TemporalType.DATE) 
    @Column(name = "ENDTIME")    
    public Date endTime; //截止时间
    
    @Temporal(TemporalType.DATE) 
    @Column(name = "STARRTTIME")    
    public Date startTime; //开始时间
    
    @Lob
    @Column(name = "DESCRIPTION")
    public String description; //描述工作
    
    
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskInfoId")//指向多的那方的pojo的关联外键字段
    private Collection<TaskComments> CommentsCollection;
    
    
    public TaskInfo(){
    	createTime = new Date();
    	startTime = new Date();
    	endTime = new Date();
    }
   

	public static TaskInfo findById(Long taskInfoId) {
		return JPA.em().find(TaskInfo.class, taskInfoId);
	}

	/**
	 * Retrieve all users.
	 */
	public static List<Appinfo> findAll() {
		return JPA.em().createQuery("from TaskInfo order by taskInfoId").getResultList();
//		return User.findAll();
	}
	
	/**
	 * Retrieve  user task by assignee.
	 */
	public static List<Appinfo> findByAssigneeID(Long assigneeID) {
		return JPA.em().createQuery("from TaskInfo where assigneeID = :queryparam order by taskInfoId").setParameter("queryparam",
				assigneeID).getResultList();
//		return User.findAll();
	}

	/**
	 * Retrieve  user task by assignee.
	 */
	public static List<Appinfo> findByAssignee(String assignee) {
		return JPA.em().createQuery("from TaskInfo where assignee = :queryparam order by taskInfoId").setParameter("queryparam",
				assignee).getResultList();
//		return User.findAll();
	}
	
	/**
	 * Update this taskinfo.
	 */
	public void update() {
		this.creater=User.findByID(createrId).name;
		this.assignee=User.findByID(assigneeId).name;
		JPA.em().merge(this);
	}

	/**
	 * Insert this new taskinfo.
	 */
	public void save() {
//		this.id = id;
		this.creater=User.findByID(createrId).name;
		this.assignee=User.findByID(assigneeId).name;
		JPA.em().persist(this);
	}

	/**
	 * Delete this taskinfo.
	 */
	public void delete() {
		JPA.em().remove(this);
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
			String order, String filter,int userid) {
		if (page < 1)
			page = 1;
		String totalsql = "select count(c) ";
		String searchclause = "from TaskInfo c where 1=1 ";
		if (filter == null || filter.equals("")) {

		} else {
			searchclause = searchclause 
					+ "And ( lower(c.taskName) like :queryparam "
					+ "or lower(c.assignee) like :queryparam "
					+ "or lower(c.creater) like :queryparam "
					+ "or lower(c.createTime) like :queryparam "
					+ "or lower(c.endTime) like :queryparam "
					+ "or lower(c.startTime) like :queryparam "
					+ "or lower(c.description) like :queryparam )";
		}
		if(userid <0){
			//不处理
		}else{
			User user = User.findByID(userid);
			if(user == null){
				//不存在，不处理
			}else{
				searchclause = searchclause + " And assigneeid ="+userid+" ";
				
			}
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

		List<TaskInfo> data;
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
		private final List<TaskInfo> list;

		public Page(List<TaskInfo> data, long total, int page,
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

		public List<TaskInfo> getList() {
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
