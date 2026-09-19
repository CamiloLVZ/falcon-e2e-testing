package config;

import java.util.List;

public class TestConfig {

    private static final String environment = System.getProperty("env");

    static{
        List<String> validEnvironments = List.of("prod", "local");
        if(environment == null){
            throw new IllegalStateException("Property 'env' is not set");
        }else if(!validEnvironments.contains(environment)){
            throw new IllegalStateException("'"+environment+"' is not a valid value for environment");
        }
    }

    public static String baseUrl(){
        if (environment.equals("prod")) {
            return "https://falconbooking.org";
        }
        return "http://localhost:5173";
    }

    public static String apiBaseUrl(){
        if (environment.equals("prod")) {
            return "https://api.falconbooking.org/api";
        }
        return "http://localhost:8080/api";
    }

    public static boolean isProdActive(){
        return environment.equals("prod");
    }

    public static boolean isLocalActive(){
        return environment.equals("local");
    }
}
