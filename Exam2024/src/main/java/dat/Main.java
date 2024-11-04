package dat;

import dat.config.ApplicationConfig;
import dat.routes.Routes;
import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {
    ApplicationConfig.startServer(7070);

    }
}