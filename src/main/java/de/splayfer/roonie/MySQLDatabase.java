package de.splayfer.roonie;

import de.splayfer.roonie.giveaway.Giveaway;
import de.splayfer.roonie.response.ResponseManager;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.*;
import java.util.*;

public class MySQLDatabase {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    @Getter
    private Connection connection;


    public MySQLDatabase(String database){
        this.host = "45.134.39.40";
        this.port = 3306;
        this.database = database;
        this.username = "jdbc";
        this.password = "NFC_King10";

        try {
            connect();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isConnected() {

        return (connection != null);

    }

    public void connect() throws ClassNotFoundException {

        if (!isConnected()) {

            try {

            Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);

                System.out.println("[MySQL] Verbunden!");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();

                System.out.println("[MySQL] Verbindung geschlossen!");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

    }

    public void update(String qry, Object... params) {

        try {

            PreparedStatement ps = connection.prepareStatement(qry);

            for(int i = 0; i < params.length; i++){
                Object param = params[i];

                ps.setObject(i+1, param);

            }
            ps.execute();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public <T> T select(Class<T> classType, String qry, String column, Object... params) throws NullPointerException {
        try {

            PreparedStatement ps = connection.prepareStatement(qry);

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];

                ps.setObject(i + 1, param);
            }

            ResultSet resultSet = ps.executeQuery();


            T result = null;

            if(resultSet.next()) {
                result = resultSet.getObject(column, classType);
            }

            ps.close();
            resultSet.close();

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Map<Integer, String> top(String qry, String column, Object... params) throws NullPointerException {
        try {

            PreparedStatement ps = connection.prepareStatement(qry);

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];


                ps.setObject(i + 1, param);
            }

            ResultSet resultSet = ps.executeQuery();

            Map<Integer, String> map = new TreeMap<>(Collections.reverseOrder());
            while(resultSet.next()) {

                map.put(resultSet.getInt("amount"), resultSet.getString("guildMember"));

            }

            ps.close();
            resultSet.close();

            return map;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseManager.Response selectResponse(String qry, Object... params) throws NullPointerException {
        try {

            PreparedStatement ps = connection.prepareStatement(qry);

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];


                ps.setObject(i + 1, param);
            }

            ResultSet resultSet = ps.executeQuery();

            ResponseManager.Response response = null;

            while(resultSet.next()) {

                response = new ResponseManager.Response(resultSet.getString("message"), Roonie.mainGuild.getMemberById(resultSet.getString("creator")), resultSet.getString("type"), resultSet.getString("value"));

            }

            ps.close();
            resultSet.close();

            return response;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> selectTemplates(String qry, Object... params) throws NullPointerException {
        try {

            PreparedStatement ps = connection.prepareStatement(qry);

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];


                ps.setObject(i + 1, param);
            }

            ResultSet resultSet = ps.executeQuery();

            List<String> templates = new ArrayList<>();

            while(resultSet.next()) {

                templates.add(resultSet.getString("url"));

            }

            ps.close();
            resultSet.close();

            return templates;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Giveaway selectGiveaway(String qry, Object... params) throws NullPointerException {
        try {

            PreparedStatement ps = connection.prepareStatement(qry);

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];


                ps.setObject(i + 1, param);
            }

            ResultSet resultSet = ps.executeQuery();

            Giveaway giveaway = null;

            while(resultSet.next()) {

                giveaway = new Giveaway((MessageChannel) Roonie.mainGuild.getGuildChannelById(resultSet.getString("channel")), resultSet.getString("prize"), resultSet.getLong("duration"), resultSet.getString("timeFormat"), new HashMap<>(){{put(resultSet.getString("requirement"), resultSet.getString("value"));}}, resultSet.getInt("amount"), resultSet.getString("picture"), ((MessageChannel) Roonie.mainGuild.getGuildChannelById(resultSet.getString("channel"))).retrieveMessageById(resultSet.getString("message")).complete());

            }

            ps.close();
            resultSet.close();

            return giveaway;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> getGiveawayEntrys(String qry, Object... params) throws NullPointerException {

        try {

            PreparedStatement ps = connection.prepareStatement(qry);

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];


                ps.setObject(i + 1, param);
            }

            ResultSet resultSet = ps.executeQuery();

            List<String> entrys = new ArrayList<>();

            while(resultSet.next()) {

                entrys.add(resultSet.getString("guildMember"));

            }

            ps.close();
            resultSet.close();

            return entrys;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Giveaway> selectAllGiveaways(String qry) throws NullPointerException {
        try {

            PreparedStatement ps = connection.prepareStatement(qry);

            ResultSet resultSet = ps.executeQuery();

            List<Giveaway> giveawayList = new ArrayList<>();

            while(resultSet.next()) {

                giveawayList.add(new Giveaway((MessageChannel) Roonie.mainGuild.getGuildChannelById(resultSet.getString("channel")), resultSet.getString("prize"), resultSet.getLong("duration"), resultSet.getString("timeFormat"), new HashMap<>(){{put(resultSet.getString("requirement"), resultSet.getString("value"));}}, resultSet.getInt("amount"), resultSet.getString("picture"), ((MessageChannel) Roonie.mainGuild.getGuildChannelById(resultSet.getString("channel"))).retrieveMessageById(resultSet.getString("message")).complete()));

            }

            ps.close();
            resultSet.close();

            return giveawayList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean existsEntry(String table, String whereStatement, Object... params) {

        try {

            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM "+ table +" WHERE " + whereStatement);
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];

                ps.setObject(i + 1, param);
            }

            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            boolean exists = resultSet.getInt(1) >= 1;

            ps.close();
            resultSet.close();

            return exists;
        } catch (Exception ex) {

        }

        return false;
    }


    public int insert(String table, String[] tableValues, Object... params) {
        try {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("INSERT INTO ");
            stringBuilder.append(table);
            stringBuilder.append("(" + String.join(",", tableValues) + ")");
            stringBuilder.append(" VALUES (");

            for (int i = 0; i < params.length; i++) {
                stringBuilder.append("?");

                if(i != (params.length-1))
                    stringBuilder.append(", ");

            }

            stringBuilder.append(")");

            PreparedStatement ps = connection.prepareStatement(stringBuilder.toString());

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];

                ps.setObject(i + 1, param);
            }

            ps.executeUpdate();

            ResultSet resultSet = ps.getGeneratedKeys();

            int id = -1;

            if(resultSet.next())
                id = resultSet.getInt(1);

            resultSet.close();
            ps.close();

            return id;


        } catch (Exception ex) {

        }
        return -1;
    }


}
