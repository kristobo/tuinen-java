package be.miras.programs.frederik.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


public class DbWerknemerOpdrachtTaakDao implements ICRUD {

	@Override
	public boolean voegToe(Object o) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try{
			DbWerknemerOpdrachtTaak dwot = (DbWerknemerOpdrachtTaak)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.save(dwot);
			session.getTransaction().commit();
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			isGelukt = false;
			e.printStackTrace();
		} finally {
			session.close();
		}	
		return isGelukt;
	}

	@Override
	public Object lees(int id) {
		DbWerknemerOpdrachtTaak dwot = new DbWerknemerOpdrachtTaak();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "FROM DbWerknemerOpdrachtTaak where id = :id";
		List<DbWerknemerOpdrachtTaak> lijst = new ArrayList<DbWerknemerOpdrachtTaak>();
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", id);
			lijst = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();

		} finally {
			session.close();
		}
		if (!lijst.isEmpty()) {
			dwot = lijst.get(0);
		}
		
		return dwot;
	}

	@Override
	public List<Object> leesAlle() {
		List<DbWerknemerOpdrachtTaak> lijst = new ArrayList<DbWerknemerOpdrachtTaak>();
		String query = "FROM DbWerknemerOpdrachtTaak"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			lijst = q.list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		List<Object> objectLijst = new ArrayList<Object>(lijst);
		return objectLijst;
	}

	@Override
	public boolean wijzig(Object o) {

		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		try {
			DbWerknemerOpdrachtTaak dwot = (DbWerknemerOpdrachtTaak)o;
			transaction = session.getTransaction();
			session.beginTransaction();
			session.saveOrUpdate(dwot);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			isGelukt = false;
			e.printStackTrace();
		} finally {
			session.close();
		}
		return isGelukt;
	}

	@Override
	public boolean verwijder(int id) {
		Session session = HibernateUtil.openSession();
		boolean isGelukt = true;
		Transaction transaction = null;
		String query = "DELETE FROM DbWerknemerOpdrachtTaak where id = :id";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("id", id);
			q.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			isGelukt = false;
			e.printStackTrace();
		} finally {
			session.close();
		}
		return isGelukt;
	}

	public List<DbWerknemerOpdrachtTaak> leesWaarTaakId(int taakId) {
		List<DbWerknemerOpdrachtTaak> lijst = new ArrayList<DbWerknemerOpdrachtTaak>();
		String query = "FROM DbWerknemerOpdrachtTaak where opdrachtTaakTaakId = :taakId"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("taakId", taakId);
			lijst = q.list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return lijst;
	}

	public void verwijderWaarOpdrachtId(int opdrachtId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbWerknemerOpdrachtTaak where opdrachtTaakOpdrachtId = :opdrachtId";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("opdrachtId", opdrachtId);
			q.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void verwijderWaarTaakId(int taakId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		String query = "DELETE FROM DbWerknemerOpdrachtTaak where opdrachtTaakTaakId = :taakId";
		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("taakId", taakId);
			q.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		
	}

	public List<Object> leesOpdrachtIdTaakIdBeginuur(int werknemerId) {
		List<Object> lijst = new ArrayList<Object>();
		String query = "SELECT id, opdrachtTaakOpdrachtId, opdrachtTaakTaakId, beginuur "
				+ "FROM DbWerknemerOpdrachtTaak where werknemerId = :werknemerId"; 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createQuery(query);
			q.setParameter("werknemerId", werknemerId);
			lijst = q.list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return lijst;
	}
        
        
        /**
         * Kristof Bourgeois
         * @return List
         */
        
	public List<Object> getAllCurrentTaskByUser(int userId) {
		List<Object> lijst = new ArrayList<Object>();
                      
                String query = "SELECT DISTINCT ot.taakID as id, t.naam as taakNaam, o.KlantID as klantId, v.Percentage as vooruitgangPercentage "
                        + "FROM opdracht_taak as ot "
                        + "INNER JOIN opdracht as o on "
                        + "ot.OpdrachtID = o.ID "
                        + "INNER JOIN taak as t on "
                        + "ot.TaakID = t.ID "
                        + "INNER JOIN werknemer_opdracht_taak as wot on "
                        + "ot.TaakID = wot.Opdracht_TaakTaakID "
                        + "LEFT OUTER JOIN vooruitgang as v on "
                        + "ot.VooruitgangID = v.ID "
                        + "WHERE wot.WerknemerID = :userId "
                     // + "AND NOW() BETWEEN o.startdatum AND o.einddatum "
                        + "AND DATE(wot.Beginuur) = DATE(ADDDATE(NOW(), INTERVAL 8 HOUR)) "
                        + "ORDER BY taakNaam";
 
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.getTransaction();
			session.beginTransaction();
			Query q = session.createSQLQuery(query);
                        q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                        q.setParameter("userId", userId);
			lijst = q.list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		List<Object> objectLijst = new ArrayList<Object>(lijst);
		return objectLijst;
	}
}
