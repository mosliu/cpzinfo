package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import static play.data.Form.*;

import models.*;

public class Secured extends Security.Authenticator {
    
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {
    	
    	ctx.flash().put("deny", "You hava to log in to use this Application!");
        return redirect(routes.Authenticate.login());
    }
    
    // Access rights
    
//    public static boolean isMemberOf(Long project) {
//        return Project.isMember(
//            project,
//            Context.current().request().username()
//        );
//    }
    
//    public static boolean isOwnerOf(Long task) {
//        return Task.isOwner(
//            task,
//            Context.current().request().username()
//        );
//    }
    
}