package com.rebel.youtube.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rebel.youtube.model.PersonModel;


class ConnectionString {
	private int port;
	private String jdbcPath;
	private String username;
	private String password;
	private String Database;
	private String host;

	public ConnectionString() {
		this.setPort(3306);
		this.setHost("localhost");
	
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the userName
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the userName to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return Database;
	}

	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {
		Database = database;
	}

	public void setJdbcUrlPath() {
		this.jdbcPath = "jdbc:mariadb://"+this.getHost()+":3306/" + this.getDatabase() + "?user=" + this.getUsername()
				+ "&password="+this.getPassword();
	}

	public String getJdbcUrlPath() {
		return this.jdbcPath;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
}

public class PersonRepository extends ConnectionString {
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	protected Connection getConnection() throws Exception {
		Connection connection = null;
		
		this.setUsername("youtuber");
		this.setPassword("123456");
		this.setDatabase("youtuber");
			
		this.setJdbcUrlPath();
		Class.forName ("org.mariadb.jdbc.Driver");
		// override 
	
		
		try {
			logger.log(Level.INFO,this.getJdbcUrlPath().toString());
			connection = DriverManager.getConnection(this.getJdbcUrlPath(), this.getUsername(), this.getPassword());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public int create(String name, int age) {
		int lastInsertId = 0;
		try {
			Connection connection = this.getConnection();
			String sql = "INSERT INTO person (name,age) values (?,?);";
			PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			prepareStatement.setString(1, name);
			prepareStatement.setInt(2, age);
			int affectedRows = prepareStatement.executeUpdate();

			if (affectedRows == 0) {
				logger.log(Level.INFO, "no record inserted");
			}
			try (ResultSet generatedKeys = prepareStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					lastInsertId = (int) generatedKeys.getLong(1);
				} else {
					logger.log(Level.INFO, "no record inserted ? weird here double query");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastInsertId;
	}

	public List<PersonModel> read() {
		List<PersonModel> personModels = new ArrayList<PersonModel>();
		
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT * FROM person ORDER BY personId DESC ";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				
				PersonModel personModel = new PersonModel();
				personModel.setPersonId(resultSet.getInt("personId"));
				personModel.setName(resultSet.getString("name"));
				personModel.setAge(resultSet.getInt("age"));
				
				personModels.add(personModel);
				
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return personModels;
	}
	public List<PersonModel> search(String search){
		List<PersonModel> personModels = new ArrayList<PersonModel>();
		
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT * FROM person WHERE name like concat('%',?,'%') or age like concat('%',?,'%');";
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setString(1, search);
			prepareStatement.setString(2, search);

			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()) {
				
				PersonModel personModel = new PersonModel();
				personModel.setPersonId(resultSet.getInt("personId"));
				personModel.setName(resultSet.getString("name"));
				personModel.setAge(resultSet.getInt("age"));
				personModels.add(personModel);
				
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return personModels;

	}

	public void update(String name, int age, int personId) {
		try {
			Connection connection = this.getConnection();
			String sql = "UPDATE person SET name=? , age=? WHERE personId = ? ";
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setString(1, name);
			prepareStatement.setInt(2, age);
			prepareStatement.setInt(3,personId);
			int affectedRows = prepareStatement.executeUpdate();

			if (affectedRows == 0) {
				logger.log(Level.INFO, "no record updated");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(int personId) {
		try {
			Connection connection = this.getConnection();
			String sql = "DELETE FROM person WHERE personId=?";
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setInt(1, personId);
			int affectedRows = prepareStatement.executeUpdate();

			if (affectedRows == 0) {
				logger.log(Level.INFO, "no record deleted");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
