package dat.routes;

import dat.config.ApplicationConfig;
import dat.controllers.impl.TripController;
import dat.daos.impl.TripDAO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static io.javalin.testtools.JavalinTest.test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripRouteTest {

    private static Javalin app;
    private static EntityManagerFactory emf;
    private static TripDAO tripDAO;
    private static TripController tripController;

    @BeforeAll
    public static void setUp() {
        emf = Persistence.createEntityManagerFactory("test");
        tripDAO = TripDAO.getInstance(emf);
        tripController = new TripController();
        app = ApplicationConfig.startServer(7070);
    }

    @AfterAll
    public static void tearDown() {
        ApplicationConfig.stopServer(app);
        emf.close();
    }

    @BeforeEach
    public void setUpTestData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Guide guide1 = new Guide("John", "Doe", "john.doe@example.com", "1234567890", 5);
        Guide guide2 = new Guide("Jane", "Doe", "jane.doe@example.com", "0987654321", 7);

        em.persist(guide1);
        em.persist(guide2);

        Trip trip1 = new Trip("Trip to the Mountains", 299.99, LocalDateTime.of(2023, 6, 1, 9, 0), LocalDateTime.of(2023, 6, 5, 18, 0), "City Center", Category.ADVENTURE, guide1);
        Trip trip2 = new Trip("Beach Vacation", 499.99, LocalDateTime.of(2023, 7, 1, 9, 0), LocalDateTime.of(2023, 7, 7, 18, 0), "City Center", Category.RELAXATION, guide2);

        em.persist(trip1);
        em.persist(trip2);

        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testGetTripById() {
        test(app, (server, client) -> {
            var response = client.get("/api/trips/2");
            assertEquals(200, response.code());

        });
    }


}