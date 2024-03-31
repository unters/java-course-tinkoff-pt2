package edu.scrapper.jpa;

import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {

    private static final Logger LOGGER = LogManager.getLogger(HibernateSessionFactoryUtil.class);

    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties props = new Properties();
                props.put("hibernate.connection.url", "jdbc:postgresql://localhost:6432/tracking");
                props.put("hibernate.connection.username", "postgres");
                props.put("hibernate.connection.password", "postgres");
                props.put("hibernate.current_session_context_class", "thread");
                configuration.addAnnotatedClass(Tracking.class);
                configuration.setProperties(props);
                StandardServiceRegistryBuilder builder =
                    new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                throw e;
            }
        }

        return sessionFactory;
    }
}
