package resoft.basLink.log;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;

import resoft.xlink.comm.helper.ThreadLocalContext;

/**
 * function:BasLinkµÄLog
 * User: albert lee
 * Date: 2005-10-12
 * Time: 13:44:40
 */
public class BasLinkLog implements Log{

    public BasLinkLog(String className) {
        logger = Logger.getLogger(className);
    }
    public boolean isDebugEnabled() {
        return true;
    }

    public boolean isErrorEnabled() {
        return true;
    }

    public boolean isFatalEnabled() {
        return true;
    }

    public boolean isInfoEnabled() {
        return true;
    }

    public boolean isTraceEnabled() {
        return true;
    }

    public boolean isWarnEnabled() {
        return true;
    }

    public void log(Level level, String msg, Throwable ex) {
        if(logger.isLoggable(level)) {
            Throwable dummyException=new Throwable();
            StackTraceElement locations[]=dummyException.getStackTrace();
            // Caller will be the third element
            String cname="unknown";
            String method="unknown";
            if( locations!=null && locations.length >3 ) {
                StackTraceElement caller=locations[3];
                cname=caller.getClassName();
                method=caller.getMethodName();
            }
            //µÃµ½Request ID
            String connId = ThreadLocalContext.getInstance().getRequestId();
            if(connId!=null) {
                msg = "Conn Id:" + connId + " Msg:" + msg;
            }

            if( ex==null ) {
                logger.logp( level, cname, method, msg );
            } else {
                logger.logp( level, cname, method, msg, ex );
            }
        }
    }

    public void trace(Object o) {

    }

    public void trace(Object o, Throwable throwable) {
        //logger.trace(o,throwable);
    }

    public void debug(Object o) {
        //logger.debug(o);
    }

    public void debug(Object o, Throwable throwable) {
        //logger.debug(o,throwable);
    }

    public void info(Object o) {
        info(o,null);
    }



    public void info(Object o, Throwable throwable) {
//        String connId = ThreadLocalContext.getInstance().getRequestId();
//        String msg = o.toString();
//        if(connId!=null) {
//            msg = "Conn Id:" + connId + " Msg:" + msg;
//        }
        log(Level.INFO,o.toString(),throwable);
    }

    public void warn(Object o) {
        //logger.warn(o);
    }

    public void warn(Object o, Throwable throwable) {
        //logger.warn(o,throwable);
    }

    public void error(Object o) {
        log(Level.SEVERE,o.toString(),null);
    }

    public void error(Object o, Throwable throwable) {
        log(Level.SEVERE,o.toString(),throwable);
    }

    public void fatal(Object o) {
        log(Level.SEVERE,o.toString(),null);
    }

    public void fatal(Object o, Throwable throwable) {
        log(Level.SEVERE,o.toString(),throwable);
    }

    private Logger logger;
}
