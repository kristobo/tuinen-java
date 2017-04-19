package be.miras.programs.frederik.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Utility class om een Hibernate session aan te maken.
 * 
 * @author Frederik
 *
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	/*
	 * Een static methode zonder naam heet een 'Static Initialization Block'
	 * Deze block wordt gerund als de class geload wordt. Dit kan gebruikt
	 * worden om static member variabelen te initialiseren.
	 */
	static {
		try {
			// 1. hibernate configureren
			Configuration configuration = new Configuration();
			configuration.configure();
			// 2. een sessionfactory creeren
			ServiceRegistry sr = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
					.buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(sr);
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * 
	 * @return hibernate Session
	 */
	public static Session openSession() {
		return sessionFactory.openSession();
	}
	
}
