package rafnews.backend.repositories.keyword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rafnews.backend.model.Keyword;
import rafnews.backend.repositories.MySqlAbstractRepository;

public class MySQLKeywordRepository extends MySqlAbstractRepository implements IKeywordRepository{

	public Keyword addKeyword(Keyword keyword) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if (findKeyword(keyword.getName()) == null){
        	return keyword;
        }
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO keywords (word) VALUES(?)", generatedColumns);
            preparedStatement.setString(1, keyword.getName());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
            	keyword.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return keyword;
	}

	public List<Keyword> allKeywords(int page, int perPage) {
		List<Keyword> kwList = new ArrayList<Keyword>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from keywords limit" + (page-1)*perPage + " , " + perPage +  ";");
            while (resultSet.next()) {

            	Keyword kw = new Keyword(resultSet.getInt("id"), resultSet.getString("word"));
            	kwList.add(kw);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return kwList;
	}

	public Keyword findKeyword(String id) {
		Keyword kw = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM keywords as k where k.word = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

              Integer idKw = resultSet.getInt("id");

              kw = new Keyword(idKw, id);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return kw;
	}

	public Keyword findKeywordById(Integer id) {
		Keyword kw = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM keywords as k where k.id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                String mainWord = resultSet.getString("word");

                kw = new Keyword(id,mainWord);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return kw;
	}

	public void deleteKeyword(Integer id) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM keywords where id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
	}

}
