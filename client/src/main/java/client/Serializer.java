package client;

import com.google.gson.Gson;

public class Serializer {
    Gson gson = new Gson();

    public String toJson(Object obj){
        return gson.toJson(obj);
    }

//    public <T> T fromJson(String str){
//        return gson.fromJson(str, T);
//    }
}
