/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */
package com.maxelyz.core.notification;

import java.sql.Connection;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;

import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;
import org.comet4j.core.event.MessageEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
//import org.comet4j.demo.eventmonitor.MessageEventListener;

/**
 * 应用初始化
 * @author jinghai.xiao@gmail.com
 * @date 2011-2-25
 */

public class AppInit implements HttpSessionListener {

	/**
     * @param se
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(se.getSession().getServletContext());
        DataSource ds =(DataSource) context.getBean("dataSource");

        CometContext cc = CometContext.getInstance();
        CometEngine engine = cc.getEngine();
        cc.registChannel(Constant.APP_CHANNEL);// Registered channel
        // Bind event listener
        engine.addConnectListener(new JoinListener());
        engine.addDropListener(new LeftListener());

        //engine.addListener(MessageEvent.class, new MessageEventListener());
        // Start system monitoring information transmitter

        /*Thread followupSender = new Thread(new FollowupSender(ds), "FollowupSender");
        followupSender.setDaemon(true);
        followupSender.start();

        Thread notificationSender = new Thread(new NotificationSender(ds), "NotificationSender");
        notificationSender.setDaemon(true);
        notificationSender.start();

        Thread approvalSender = new Thread(new ApprovalSender(ds), "ApprovalSender");
        approvalSender.setDaemon(true);
        approvalSender.start();*/

        Thread emailAlert = new Thread(new EmailAlert(ds), "EmailAlert");
        emailAlert.setDaemon(true);
        emailAlert.start();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

//	@Override
//	public void contextInitialized(ServletContextEvent event) {
//                ServletContext ctx = event.getServletContext();
//		WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(ctx);
//		DataSource dataSource = (DataSource) springContext.getBean("dataSource");
//		ctx.setAttribute("dataSource", dataSource);
//            
//		CometContext cc = CometContext.getInstance();
//		CometEngine engine = cc.getEngine();
//		cc.registChannel(Constant.APP_CHANNEL);// Registered channel
//		// Bind event listener
//		engine.addConnectListener(new JoinListener());
//		engine.addDropListener(new LeftListener());
//                
//                //engine.addListener(MessageEvent.class, new MessageEventListener());
//		// Start system monitoring information transmitter
//                
//		Thread followupSender = new Thread(new FollowupSender(), "FollowupSender");
//		followupSender.setDaemon(true);
//		followupSender.start();
//                
//		Thread notificationSender = new Thread(new NotificationSender(), "NotificationSender");
//		notificationSender.setDaemon(true);
//		notificationSender.start();
//                
//		Thread approvalSender = new Thread(new ApprovalSender(), "ApprovalSender");
//		approvalSender.setDaemon(true);
//		approvalSender.start();
//	}

	/**
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */

//	@Override
//	public void contextDestroyed(ServletContextEvent event) {
//		// TODO This method is not yet achieved
//
//	}

}
