package pothole.management.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pothole.management.api.domain.Pothole;
import restx.server.JettyWebServer;
import restx.server.WebServer;

import java.util.List;

public class PotholeResourceSpecTest {
    public static final String WEB_INF_LOCATION = "src/main/webapp/WEB-INF/web.xml";
    public static final String WEB_APP_LOCATION = "src/main/webapp";
    public static final String TEST_PORT = "5050";
    public static final String API_BASE_URL = "http://localhost:" + TEST_PORT + "/api";

    CloseableHttpClient client;

    @BeforeClass
    public static void startUp() throws Exception {
        startServer();
    }

    @Before
    public void setUpHttpClient(){
        client = HttpClients.createDefault();
    }

    @Test
    public void can_hit_potholes_endpoint_and_get_correct_status() throws Exception {
        HttpGet httpGet = new HttpGet(API_BASE_URL + "/potholes");
        CloseableHttpResponse response = client.execute(httpGet);

        try {
            assert (response.getStatusLine().getStatusCode() == 200);
        } finally {
            response.close();
        }
    }

    @Test
    public void can_add_a_new_pothole() throws Exception {
        HttpPost httpPost = new HttpPost(API_BASE_URL + "/potholes");
        StringEntity json = new StringEntity("{\"location\":\"testlocation\"}");
        httpPost.addHeader("content-type", "application/json");
        httpPost.setEntity(json);
        CloseableHttpResponse response = client.execute(httpPost);

        try {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            assert (response.getStatusLine().getStatusCode() == 200);
            assert (responseString.contains("\"location\" : \"testlocation\","));
        } finally {
            response.close();
        }
    }

    @Test
    public void can_search_for_test_location_and_get_results() throws Exception {
        HttpGet httpGet = new HttpGet(API_BASE_URL + "/potholes?location=testlocation");
        CloseableHttpResponse response = client.execute(httpGet);

        try {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            assert (response.getStatusLine().getStatusCode() == 200);
            assert (responseString.contains("\"location\" : \"testlocation\","));
        } finally {
            response.close();
        }
    }

    @Test
    public void can_search_for_non_existant_location_and_get_no_results() throws Exception {
        HttpGet httpGet = new HttpGet(API_BASE_URL + "/potholes?location=emptylocation");
        CloseableHttpResponse response = client.execute(httpGet);

        try {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            assert (response.getStatusLine().getStatusCode() == 200);
            assert (responseString.equals("[ ]"));
        } finally {
            response.close();
        }
    }

    @Test
    public void can_search_for_pothole_by_id() throws Exception {
        HttpGet httpGet = new HttpGet(API_BASE_URL + "/potholes?location=testlocation");
        CloseableHttpResponse response = client.execute(httpGet);

        try {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            //Turn into an object
            ObjectMapper om = new ObjectMapper();
            TypeFactory typeFactory = om.getTypeFactory();
            List<Pothole> pothole = om.readValue(responseString, typeFactory.constructCollectionType(List.class, Pothole.class));

            String keyString = pothole.get(0).getKey().replace("\"", "");
            httpGet = new HttpGet(API_BASE_URL + "/potholes/" + keyString);
            response = client.execute(httpGet);

            entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");

            assert (responseString.contains(keyString));
            assert (response.getStatusLine().getStatusCode() == 200);
        } finally {
            response.close();
        }
    }

    public static void startServer() throws Exception {
        int port = Integer.valueOf(Optional.fromNullable(System.getenv("PORT")).or(TEST_PORT));
        WebServer server = new JettyWebServer(WEB_INF_LOCATION, WEB_APP_LOCATION, port, "0.0.0.0");

        /*
         * load mode from system property if defined, or default to dev
         * be careful with that setting, if you use this class to launch your server in production, make sure to launch
         * it with -Drestx.mode=prod or change the default here
         */
        System.setProperty("restx.mode", System.getProperty("restx.mode", "dev"));
        System.setProperty("restx.app.package", "pothole.management.api");

        server.start();
    }
}