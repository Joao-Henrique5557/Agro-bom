package br.com.agrobombackend.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utilitarios compartilhados pelos endpoints REST (pacote api.*) usados pelo
 * aplicativo React Native. Os endpoints reaproveitam os DAOs ja existentes
 * (usados pelas telas JSP) e apenas serializam/desserializam o resultado como JSON.
 */
public final class ApiUtils {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, type, ctx) ->
                    src == null ? null : new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, ctx) ->
                    json == null || json.isJsonNull() ? null : LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .registerTypeAdapter(java.sql.Date.class, (JsonSerializer<java.sql.Date>) (src, type, ctx) ->
                    src == null ? null : new JsonPrimitive(src.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .create();

    private ApiUtils() { }

    public static Gson gson() {
        return GSON;
    }

    public static void writeJson(HttpServletResponse response, Object body) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(GSON.toJson(body));
    }

    public static void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(GSON.toJson(new ErrorBody(message)));
    }

    public static <T> T readJson(HttpServletRequest request, Class<T> clazz) throws IOException {
        return GSON.fromJson(request.getReader(), clazz);
    }

    public static <T> T readJson(HttpServletRequest request, Type type) throws IOException {
        return GSON.fromJson(request.getReader(), type);
    }

    public static class ErrorBody {
        public final String erro;
        public ErrorBody(String erro) { this.erro = erro; }
    }
}
