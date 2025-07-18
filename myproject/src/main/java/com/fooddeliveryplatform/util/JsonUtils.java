package com.fooddeliveryplatform.util;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class JsonUtils {
    // Parse JSON string to JsonObject
    public static JsonObject parseJson(String jsonString) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            return reader.readObject();
        }
    }
    
    // Convert JsonObject to String
    public static String jsonToString(JsonObject jsonObject) {
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter writer = Json.createWriter(stringWriter)) {
            writer.writeObject(jsonObject);
        }
        return stringWriter.toString();
    }

    public static JsonObject parseRequest(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             JsonReader reader = Json.createReader(is)) {
            return reader.readObject();
        }
    }
}
