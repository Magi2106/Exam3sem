package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.GuideDTO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.stream.Collectors;

public class GuideDAO implements IDAO<GuideDTO, Long> {

    private static GuideDAO instance;
    private static EntityManagerFactory emf;

    public static GuideDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuideDAO();
        }
        return instance;
    }

    @Override
    public GuideDTO create(GuideDTO guideDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Guide guide = new Guide();
            guide.setFirstname(guideDTO.getFirstname());
            guide.setLastname(guideDTO.getLastname());
            guide.setEmail(guideDTO.getEmail());
            guide.setPhone(guideDTO.getPhone());
            guide.setYearsOfExperience(guideDTO.getYearsOfExperience());
            em.persist(guide);
            em.getTransaction().commit();
            return new GuideDTO(guide);
        } finally {
            em.close();
        }
    }

    @Override
    public List<GuideDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<GuideDTO> query = em.createQuery("SELECT new dat.dtos.GuideDTO(g) FROM Guide g", GuideDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public GuideDTO getById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Guide guide = em.find(Guide.class, id);
            return new GuideDTO(guide);
        } finally {
            em.close();
        }
    }

    @Override
    public GuideDTO update(Long id, GuideDTO guideDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, id);
            if (guide == null) {
                em.getTransaction().rollback();
                return null;
            }
            guide.setFirstname(guideDTO.getFirstname());
            guide.setLastname(guideDTO.getLastname());
            guide.setEmail(guideDTO.getEmail());
            guide.setPhone(guideDTO.getPhone());
            guide.setYearsOfExperience(guideDTO.getYearsOfExperience());
            List<Trip> trips = guideDTO.getTrips().stream()
                    .map(tripDTO -> em.find(Trip.class, tripDTO.getId()))
                    .collect(Collectors.toList());
            guide.setTrips(trips);
            Guide mergedGuide = em.merge(guide);
            em.getTransaction().commit();
            return new GuideDTO(mergedGuide);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, id);
            if (guide != null) {
                em.remove(guide);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}