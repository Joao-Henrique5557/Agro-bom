package br.com.agrobombackend.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fabrica de conexoes com o MySQL.
 *
 * As credenciais podem ser sobrescritas por variaveis de ambiente, o que
 * permite que o mesmo artefato rode tanto localmente (Eclipse/Tomcat)
 * quanto dentro de um container Docker, apenas variando o docker-compose.yml.
 *
 * Variaveis de ambiente suportadas:
 *   DB_HOST      (padrao: localhost)
 *   DB_PORT      (padrao: 3306)
 *   DB_NAME      (padrao: db_agro_bom)
 *   DB_USER      (padrao: root)
 *   DB_PASSWORD  (padrao: 1234)
 */
public class ConnectionFactory {

    private static final String DB_HOST     = env("DB_HOST", "localhost");
    private static final String DB_PORT     = env("DB_PORT", "3306");
    private static final String DB_NAME     = env("DB_NAME", "db_agro_bom");
    private static final String DB_USER     = env("DB_USER", "root");
    private static final String DB_PASSWORD = env("DB_PASSWORD", "1234");

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    /**
     * Abre uma nova conexao. Cada chamador e responsavel por fecha-la
     * (idealmente via try-with-resources) assim que terminar de usa-la --
     * conexoes nunca devem ser mantidas como estado de longa duracao
     * (ex.: campo de instancia de DAO ou Servlet).
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
                    + "?useTimezone=true&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";

            return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Falha ao conectar ao banco de dados", e);
        }
    }
}
