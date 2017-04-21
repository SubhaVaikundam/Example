package utils;


import java.util.HashMap;
import java.util.Map;

public class Configuration
{
    public final static String MOCK_HOST = "localhost";
    public final static int MOCK_PORT = 1234;
    public static final String SERVICE_URL = "http://" + MOCK_HOST + ":" + MOCK_PORT;

    public static final String PROVIDER_NAME = "banner-provider";
    public static final String CONSUMER_NAME = "banner-consumer";

    public final static Map<String, String> HEADER =  new HashMap<String, String>();
    static {
    	HEADER.put("Content-Type", "application/json;charset=UTF-8");
    }

}
