 package rafnews.backend.repositories.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rafnews.backend.model.Role;
import rafnews.backend.model.Status;
import rafnews.backend.model.User;
import rafnews.backend.repositories.MySqlAbstractRepository;

public class MySQLUserRepository extends MySqlAbstractRepository implements IUserRepository{

	public User findUser(String email) {
		User user = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM users where email = ?");
            preparedStatement.setString(1, email);
            System.out.println(email);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
            	user = new User();
            	user.setId(resultSet.getInt("id"));
            	user.setEmail(resultSet.getString("email"));
            	user.setName(resultSet.getString("name"));
            	user.setSurname(resultSet.getString("surname"));
            	Role role = resultSet.getInt("type") == 0 ? Role.ADMIN : Role.CONTENTWRITER;
            	user.setRole(role);
            	Status status = resultSet.getInt("status") == 0 ? Status.INACTIVE : Status.ACTIVE;
            	user.setStatus(status);
            	user.setPassword(resultSet.getString("password"));
            }

            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
	}

	public List<User> allUser(int page, int perPage) {
        List<User> userList = new ArrayList<User>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from users limit "+ (page-1)*perPage + " , " + perPage +  ";");
            while (resultSet.next()) {
            	User user = new User();
            	user.setId(resultSet.getInt("id"));
            	user.setEmail(resultSet.getString("email"));
            	user.setName(resultSet.getString("name"));
            	user.setSurname(resultSet.getString("surname"));
            	Role role = resultSet.getInt("type") == 0 ? Role.ADMIN : Role.CONTENTWRITER;
            	user.setRole(role);
            	Status status = resultSet.getInt("status") == 0 ? Status.INACTIVE : Status.ACTIVE;
            	user.setStatus(status);
            	user.setPassword(resultSet.getString("password"));
                userList.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return userList;
	}

	public User addUser(User user) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns={"email"};
            preparedStatement = connection.prepareStatement("select * from users as u where u.email = ? ");
            preparedStatement.setString(1, user.getEmail());
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                preparedStatement = connection.prepareStatement("INSERT INTO users (email,name,surname,type," +
                        "status, password) VALUES(?,?,?,?,?,?)", generatedColumns);
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getSurname());
                int role = 0;
                if (user.getRole() != Role.ADMIN) {
                	role = 1;
                }
                preparedStatement.setInt(4, role);
                int status = 0;
                if (user.getStatus() != Status.INACTIVE) {
                	status = 1;
                }
                preparedStatement.setInt(5, status);
                preparedStatement.setString(6, user.getPassword());
                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
	}

	public Integer userActivity(String email) {
		User user = findUser(email);
		if (user == null) return 0;
		int status = 0;
        if (user.getStatus() != Status.INACTIVE) {
        	status = 1;
        }
        return status;
	}

	public User updateUser(User user, String email) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();


            if (!(email.equals(user.getEmail()))) {
                preparedStatement = connection.prepareStatement("select * from users where email = ? ");
                preparedStatement.setString(1, user.getEmail());
                resultSet = preparedStatement.executeQuery();

            }
            if(resultSet == null || !resultSet.next() || email.equals(user.getEmail())) {

                preparedStatement = connection.prepareStatement("update users as u set u.email = ?, u.name = ?" +
                        ", u.surname = ?, u.type = ?, u.status = ?. u.password = ? where email = ?");
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getSurname());
                int role = 0;
                if (user.getRole() != Role.ADMIN) {
                	role = 1;
                }
                preparedStatement.setInt(4, role);
                int status = 0;
                if (user.getStatus() != Status.INACTIVE) {
                	status = 1;
                }
                preparedStatement.setInt(5, status);
                preparedStatement.setString(6, user.getPassword());
                preparedStatement.executeUpdate();


            }
            preparedStatement.close();
            connection.close();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            if (resultSet != null) {
                this.closeResultSet(resultSet);
            }
            this.closeConnection(connection);
        }

        return user;
	}

}
