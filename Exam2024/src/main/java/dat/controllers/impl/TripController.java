// TripController.java
package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.daos.impl.TripDAO;
import dat.dtos.TripDTO;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TripController {

    private static final Logger logger = LoggerFactory.getLogger(TripController.class);
    private final TripDAO dao;

    public TripController() {
        this.dao = TripDAO.getInstance(HibernateConfig.getEntityManagerFactory());
    }

    public void readAll(Context ctx) {
        try {
            List<TripDTO> trips = dao.getAll();
            ctx.json(trips).status(200);
        } catch (Exception e) {
            logger.error("Error reading all trips", e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    public void read(Context ctx) {
        try {
            long id = ctx.pathParamAsClass("id", Long.class).get();
            TripDTO trip = dao.getById(id);
            if (trip == null) {
                throw new ApiException(404, "Trip with ID " + id + " not found");
            }
            ctx.json(trip).status(200);
        } catch (Exception e) {
            logger.error("Error reading trip with ID " + ctx.pathParam("id"), e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    public void create(Context ctx) {
        try {
            TripDTO trip = ctx.bodyValidator(TripDTO.class)
                    .check(t -> t.getName() != null && !t.getName().isEmpty(), "Name is required")
                    .check(t -> t.getPrice() > 0, "Price must be greater than 0")
                    .check(t -> t.getStarttime() != null, "Start time is required")
                    .check(t -> t.getEndtime() != null, "End time is required")
                    .check(t -> t.getStartposition() != null && !t.getStartposition().isEmpty(), "Start position is required")
                    .check(t -> t.getCategory() != null, "Category is required")
                    .check(t -> t.getGuide() != null, "Guide is required")
                    .get();

            ctx.json(dao.create(trip)).status(201);
        } catch (Exception e) {
            logger.error("Error creating trip", e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    public void update(Context ctx) {
        try {
            long id = ctx.pathParamAsClass("id", Long.class).get();
            TripDTO trip = ctx.bodyAsClass(TripDTO.class);
            TripDTO updatedTrip = dao.update(id, trip);
            if (updatedTrip != null) {
                ctx.json(updatedTrip).status(200);
            } else {
                throw new ApiException(404, "Trip with ID " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Error updating trip with ID " + ctx.pathParam("id"), e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    public void delete(Context ctx) {
        try {
            long id = ctx.pathParamAsClass("id", Long.class).get();
            TripDTO trip = dao.getById(id);
            if (trip == null) {
                throw new ApiException(404, "Trip with ID " + id + " not found");
            }
            dao.delete(id);
            ctx.status(204);
        } catch (Exception e) {
            logger.error("Error deleting trip with ID " + ctx.pathParam("id"), e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    public void addGuideToTrip(Context ctx) {
        try {
            int tripId = ctx.pathParamAsClass("tripId", Integer.class).get();
            int guideId = ctx.pathParamAsClass("guideId", Integer.class).get();
            dao.addGuideToTrip(tripId, guideId);
            ctx.status(204);
        } catch (Exception e) {
            logger.error("Error adding guide to trip", e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    public void getTripsByGuide(Context ctx) {
        try {
            int guideId = ctx.pathParamAsClass("guideId", Integer.class).get();
            ctx.json(dao.getTripsByGuide(guideId)).status(200);
        } catch (Exception e) {
            logger.error("Error getting trips by guide", e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    public void filterByCategory(Context ctx) {
        try {
            String category = ctx.pathParam("category");
            List<TripDTO> trips = dao.getAll().stream()
                    .filter(trip -> trip.getCategory().toString().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
            ctx.json(trips).status(200);
        } catch (Exception e) {
            logger.error("Error filtering trips by category", e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    public void getTotalPriceByGuide(Context ctx) {
        try {
            List<Map<String, Object>> result = dao.getTotalPriceByGuide();
            ctx.json(result).status(200);
        } catch (Exception e) {
            logger.error("Error getting total price by guide", e);
            throw new ApiException(500, "Internal Server Error");
        }
    }
}