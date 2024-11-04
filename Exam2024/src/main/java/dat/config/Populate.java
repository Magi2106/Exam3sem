// Populate.java
package dat.config;

import dat.dtos.GuideDTO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;

public class Populate {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Guide guide1 = new Guide("John", "Doe", "john.doe@example.com", "1234567890", 5);
        Guide guide2 = new Guide("Jane", "Doe", "jane.doe@example.com", "0987654321", 7);

        em.persist(guide1);
        em.persist(guide2);

        em.getTransaction().commit();

        em.getTransaction().begin();

        Trip trip1 = new Trip("Trip to the Mountains", 299.99, LocalDateTime.of(2023, 6, 1, 9, 0), LocalDateTime.of(2023, 6, 5, 18, 0), "City Center", Category.ADVENTURE, guide1);
        Trip trip2 = new Trip("Beach Vacation", 499.99, LocalDateTime.of(2023, 7, 1, 9, 0), LocalDateTime.of(2023, 7, 7, 18, 0), "City Center", Category.RELAXATION, guide2);

        em.persist(trip1);
        em.persist(trip2);

        em.getTransaction().commit();
        em.close();
    }
}