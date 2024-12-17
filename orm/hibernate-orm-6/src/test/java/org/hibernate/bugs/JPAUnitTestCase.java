package org.hibernate.bugs;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.bugs.model.Text;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@BeforeEach
	void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@AfterEach
	void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		// create and persist a new Text entity
		Text text = new Text();
		text.setId(1L);
		text.setText("Some text ".repeat(450));
		entityManager.persist(text);
		entityManager.getTransaction().commit();
		entityManager.close();

		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		// Do stuff...

		HibernateCriteriaBuilder hcb = entityManager.unwrap(Session.class).getCriteriaBuilder();
	 	CriteriaQuery<Text> criteriaQuery = hcb.createQuery(Text.class);
		Root<Text> root = criteriaQuery.from(Text.class);
//		criteriaQuery.where(hcb.ilike(root.get("text"), "%some text%"));
		criteriaQuery.where(hcb.like(hcb.lower(root.get("text")), "%some text%"));
		List<Text> result = entityManager.createQuery(criteriaQuery).getResultList();
		System.out.println(result);
		Assertions.assertTrue(result.contains(text) && result.size() == 1);

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
