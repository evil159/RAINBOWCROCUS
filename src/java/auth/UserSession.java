/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rol
 */
public class UserSession {
    private static final long SESSION_VALIDITY = TimeUnit.HOURS.toMillis(2);
    private static final Map<String, Date> SESSIONS = new HashMap<String, Date>();

    public static void register(String sessionKey) {
        SESSIONS.put(sessionKey, new Date(new Date().getTime() + SESSION_VALIDITY));
    }
    
    public static void deregister(String sessionKey) {
        SESSIONS.remove(sessionKey);
    }
    
    public static boolean isValid(String sessionKey) {
        
        if (!SESSIONS.containsKey(sessionKey)) {
            return false;
        }
                
        return SESSIONS.get(sessionKey).after(new Date());
    }
}
