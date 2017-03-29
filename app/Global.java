/*
 * Copyright 2012 Steve Chaloner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import models.SecurityRole;
import models.User;
import models.UserPermission;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.db.jpa.Transactional;

import java.util.Arrays;

import javax.persistence.EntityManager;

/**
 * @author Moses (lx0319@gmail.com)
 */
public class Global extends GlobalSettings
{
    @Override
    @Transactional
    public void onStart(Application application)
    {
    	JPA.bindForCurrentThread(JPA.em("default"));
        if (SecurityRole.findAllcount() == 0)
        {
            for (String roleName : Arrays.asList("foo", "bar", "hurdy", "gurdy"))
            {
                SecurityRole role = new SecurityRole();
                role.roleName = roleName;
                role.save();
            }
        }

        if (UserPermission.findAll().size()  == 0)
        {
            UserPermission permission = new UserPermission();
            permission.value = "printers.edit";
            permission.save();
        }
        
        if (User.findAll().size()  == 0)
        {
        }
        
    }
}
