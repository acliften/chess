package client;

import com.google.gson.Gson;
import websocket.ResponseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.util.Map;

public class ClientCommunicator {

    private HttpClient httpClient = HttpClient.newHttpClient();
    private String serverURL;

    public ClientCommunicator(String url){
        this.serverURL = url;
    }

    public <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (request != null){
                http.setDoOutput(true);
            }
            if (authToken !=null){
                http.setRequestProperty("Authorization", authToken);
            }


            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            if (responseClass != null){
                return readBody(http, responseClass);
            }
            return null;
        } catch (ResponseException e){
            throw e;
        } catch (Exception e){
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null){
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try(OutputStream reqBody = http.getOutputStream()){
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws ResponseException, IOException {
        var status = http.getResponseCode();
        InputStream err = http.getErrorStream();
        if (err != null) {
            InputStreamReader reader = new InputStreamReader(err);
            var msg = new Gson().fromJson(reader, Map.class);
            throw new ResponseException(ResponseException.fromHttpStatusCode(status), (String) msg.get("message"));
        }
        if(!isSuccessful(status)){
            throw new ResponseException(ResponseException.fromHttpStatusCode(status), http.getResponseMessage());
        }

    }

    private boolean isSuccessful(int status) {return status/100 == 2;}

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException{
        T response = null;
        try (InputStream respBody = http.getInputStream()){
            InputStreamReader reader = new InputStreamReader(respBody);
            if (responseClass != null){
                response = new Gson().fromJson(reader, responseClass);
            }
        }

        return response;
    }


}
