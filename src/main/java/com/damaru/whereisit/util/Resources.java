package com.damaru.whereisit.util;


import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

public class Resources {
    
    private static final String EJB_CONTEXT;
    private static Logger logger = Logger.getLogger(Resources.class.getName());


    static {
        try {
            EJB_CONTEXT = "java:global/" + new InitialContext().lookup("java:app/AppName") + "/";
        } catch (NamingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    
    @Produces
    @PersistenceContext
    private EntityManager em;

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
    
    @SuppressWarnings("unchecked") // Because of forced cast on (T).
    public static <T> T lookup(Class<T> ejbClass) {
        try {
            
            InitialContext initialContext = new InitialContext();
            String jndiName = EJB_CONTEXT + ejbClass.getSimpleName();
            logger.info("lookup " + jndiName);
            // Do not use ejbClass.cast(). It will fail on local/remote interfaces.
            return (T) initialContext.lookup(jndiName);
        } catch (NamingException e) {
            throw new IllegalArgumentException(
                String.format("Cannot find EJB class %s", ejbClass), e);
        }
    }

}
