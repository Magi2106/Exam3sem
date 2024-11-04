package dat.daos.impl;

import dat.daos.IDAO;
import dat.daos.ITripGuideDAO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.*;
import java.util.stream.Collectors;

public class TripDAO implements IDAO<TripDTO, Long>, ITripGuideDAO {

    private static TripDAO instance;
    private static EntityManagerFactory emf;

    public static TripDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripDAO();
        }
        return instance;
    }

    @Override
    public TripDTO create(TripDTO tripDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = new Trip();
            trip.setName(tripDTO.getName());
            trip.setPrice(tripDTO.getPrice());
            trip.setStarttime(tripDTO.getStarttime());
            trip.setEndtime(tripDTO.getEndtime());
            trip.setStartposition(tripDTO.getStartposition());
            trip.setDescription(tripDTO.getDescription());
            trip.setCategory(tripDTO.getCategory());
            Guide guide = em.find(Guide.class, tripDTO.getGuide().getId());
            trip.setGuide(guide);
            em.persist(trip);
            em.getTransaction().commit();
            return new TripDTO(trip);
        } finally {
            em.close();
        }
    }

    @Override
    public List<TripDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TripDTO> query = em.createQuery("SELECT new dat.dtos.TripDTO(t) FROM Trip t", TripDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public TripDTO getById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Trip trip = em.find(Trip.class, id);
            return new TripDTO(trip);
        } finally {
            em.close();
        }
    }

    @Override
    public TripDTO update(Long id, TripDTO tripDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                em.getTransaction().rollback();
                return null;
            }
            trip.setName(tripDTO.getName());
            trip.setPrice(tripDTO.getPrice());
            trip.setStarttime(tripDTO.getStarttime());
            trip.setEndtime(tripDTO.getEndtime());
            trip.setStartposition(tripDTO.getStartposition());
            trip.setDescription(tripDTO.getDescription());
            trip.setCategory(tripDTO.getCategory());
            Guide guide = em.find(Guide.class, tripDTO.getGuide().getId());
            trip.setGuide(guide);
            Trip mergedTrip = em.merge(trip);
            em.getTransaction().commit();
            return new TripDTO(mergedTrip);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip != null) {
                em.remove(trip);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void addGuideToTrip(int tripId, int guideId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, (long) tripId);
            Guide guide = em.find(Guide.class, (long) guideId);
            if (trip != null && guide != null) {
                trip.setGuide(guide);
                em.merge(trip);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public Set<TripDTO> getTripsByGuide(int guideId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t WHERE t.guide.id = :guideId", Trip.class);
            query.setParameter("guideId", (long) guideId);
            List<Trip> trips = query.getResultList();
            Set<TripDTO> tripDTOs = new HashSet<>();
            for (Trip trip : trips) {
                tripDTOs.add(new TripDTO(trip));
            }
            return tripDTOs;
        } finally {
            em.close();
        }
    }
    public List<Map<String, Object>> getTotalPriceByGuide() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT t.guide.id, SUM(t.price) FROM Trip t GROUP BY t.guide.id", Object[].class);
            List<Object[]> results = query.getResultList();
            return results.stream()
                    .map(result -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("guideId", result[0]);
                        map.put("totalPrice", result[1]);
                        return map;
                    })
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

}