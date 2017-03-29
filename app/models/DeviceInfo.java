package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import play.db.jpa.*;

/**
 * Company entity managed by JPA
 */
@Entity 
@Table(name = "deviceinfo")
public class DeviceInfo {

    @Id
    public Long deviceid;
    
    public String devicename;
    public String devicereadblename;
    public String devicereadblenameen;
    public String devicetype;
    public String status;
    public String info;
    public String comments;
    
    public static DeviceInfo findById(Long id) {
        return JPA.em().find(DeviceInfo.class, id);
    }

    public static Map<String,String> options() {
        List<DeviceInfo> devices = JPA.em().createQuery("from DeviceInfo order by deviceid").getResultList();
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(DeviceInfo c: devices) {
            options.put(c.devicename,c.devicename+ "  " + c.devicereadblename);
        }
        return options;
    }

}

