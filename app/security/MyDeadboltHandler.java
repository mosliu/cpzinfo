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
package security;

import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import be.objectify.deadbolt.core.models.Subject;
import models.User;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;
import views.html.*;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class MyDeadboltHandler extends AbstractDeadboltHandler
{
//    public F.Promise<SimpleResult> beforeRoleCheck(Http.Context context)
//    {
//        // returning null means that everything is OK.  Return a real result if you want a redirect to a login page or
//        // somewhere else
//        return null;
//    }

//    public Subject getRoleHolder(Http.Context context)
//    {
//        // in a real application, the user name would probably be in the session following a login process
//    	User u = User.findByEmail((String)context.session().get("email"));
//    	if(u==null){
//    		u = new User();
//    	}
//    	return u;
//    }

    public DynamicResourceHandler getDynamicResourceHandler(Http.Context context)
    {
        return new MyDynamicResourceHandler();
    }
//
//    public Result onAccessFailure(Http.Context context,
//                                  String content)
//    {
//        // you can return any result from here - forbidden, etc
//    	User u = User.findByEmail((String)context.session().get("email"));
//        return ok(accessFailed.render(u));
////        return ok("error");
//    }

	@Override
	public  F.Promise<SimpleResult> beforeAuthCheck(Context context) {
		// returning null means that everything is OK.  Return a real result if you want a redirect to a login page or
        // somewhere else
        return null;
	}

	@Override
	public Subject getSubject(Context context) {
		 // in a real application, the user name would probably be in the session following a login process
    	User u = User.findByEmail((String)context.session().get("email"));
    	if(u==null){
    		u = new User();
    	}
    	return u;
	}

	@Override
	public F.Promise<SimpleResult> onAuthFailure(Context context, String s) {
		 // you can return any result from here - forbidden, etc
		// you can return any result from here - forbidden, etc
		final User u = User.findByEmail((String)context.session().get("email"));
//        return ok(accessFailed.render(u));
        return F.Promise.promise(new F.Function0<SimpleResult>()
                {
                    @Override
                    public SimpleResult apply() throws Throwable {
                        return ok(accessFailed.render(u));
                    }
                });
//        return ok("error");
	}
}
