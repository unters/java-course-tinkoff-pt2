package edu.scrapper.jpa;

import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

@RequiredArgsConstructor
public class HibernateSessionFactoryUtil {

    private static final Logger LOGGER = LogManager.getLogger(HibernateSessionFactoryUtil.class);

    private static SessionFactory sessionFactory;

    private final String connectionUrl;
    private final String username;
    private final String password;

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties props = new Properties();
                props.put("hibernate.connection.url", connectionUrl);
                props.put("hibernate.connection.username", username);
                props.put("hibernate.connection.password", password);
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
