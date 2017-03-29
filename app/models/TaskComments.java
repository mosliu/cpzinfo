package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.JPA;

@Entity
@Table(name = "taskcomments")
public class TaskComments {
	@Id
    @GeneratedValue
    @Column(name = "TASKCOMMENTSID", nullable = false)
	public int taskCommentsId; //任务备注编号
	
	@Column(name = "COMMENT")
    private String comment;
	
	@Temporal(TemporalType.TIMESTAMP) 
    @Column(name = "COMMENTTIME")    
    public Date commentTime;
	
	@JoinColumn(name = "TASKINFOID", referencedColumnName = "TASKINFOID")//设置对应数据表的列名和引用的数据表的列名
    @ManyToOne//设置在“一方”pojo的外键字段上
    private TaskInfo taskInfoId;
	
	
	public static TaskInfo findById(Long taskCommentsId) {
		return JPA.em().find(TaskInfo.class, taskCommentsId);
	}

	/**
	 * Retrieve all users.
	 */
	public static List<Appinfo> findAll() {
		return JPA.em().createQuery("from TaskComments order by taskCommentsId").getResultList();
//		return User.findAll();
	}

	/**
     * Insert this new faq.
     */
    public void save() {
        JPA.em().persist(this);
    }
	
	
}

