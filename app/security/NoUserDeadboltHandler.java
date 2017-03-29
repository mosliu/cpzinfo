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

import models.User;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import be.objectify.deadbolt.core.models.Subject;
import play.libs.F;
import play.mvc.Http;
import play.mvc.SimpleResult;
import views.html.accessFailed;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class NoUserDeadboltHandler extends AbstractDeadboltHandler
{
    public F.Promise<SimpleResult> beforeAuthCheck(Http.Context context)
    {
        return null;
    }

    public Subject getSubject(Http.Context context)
    {
        return null;
    }

    public F.Promise<SimpleResult> onAuthFailure(Http.Context context,
                                String content)
    {
    	final User u = new User();
//        return ok(accessFailed.render(u));
    	return F.Promise.promise(new F.Function0<SimpleResult>()
    	        {
    	            @Override
    	            public SimpleResult apply() throws Throwable {
    	                return ok(accessFailed.render(u));
    	            }
    	        });
    }

    public DynamicResourceHandler getDynamicResourceHandler(Http.Context context)
    {
        return new MyAlternativeDynamicResourceHandler();
    }
}
