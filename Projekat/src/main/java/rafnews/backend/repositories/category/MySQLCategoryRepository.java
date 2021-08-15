package rafnews.backend.repositories.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rafnews.backend.model.Category;
import rafnews.backend.repositories.MySqlAbstractRepository;

public class MySQLCategoryRepository extends MySqlAbstractRepository implements ICategoryRepository {

	public Category addCategory(Category category) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = this.newConnection();

			String[] generatedColumns = { "name" };
			preparedStatement = connection.prepareStatement("select * from categories as k where k.name = ? ");
			preparedStatement.setString(1, category.getName());
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {

				preparedStatement = connection
						.prepareStatement("INSERT INTO categories (name, description) VALUES (?,?)", generatedColumns);
				preparedStatement.setString(1, category.getName());
				preparedStatement.setString(2, category.getDescription());

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

		return category;
	}

	public List<Category> allCategory(int page, int perPage) {
		List<Category> categoryList = new ArrayList<Category>();

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = this.newConnection();

			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from categories limit " + (page-1)*perPage + " , " + perPage +  ";");
			while (resultSet.next()) {
				Category kategorija = new Category(resultSet.getInt("id"), resultSet.getString("name"),
						resultSet.getString("description"));
				categoryList.add(kategorija);
			}

			resultSet.close();
			statement.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(statement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}

		return categoryList;
	}

	public Category findCategory(String name) {

		Category category = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM categories as k where k.name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                String description = resultSet.getString("description");
                category = new Category(resultSet.getInt("id"), name, description);
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

        return category;
	}

	public void deleteCategory(String name) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM categories where name = ?");
            preparedStatement.setString(1, name);
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

	public Category updateCategory(Category category, String name) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();


            if (!(name.equals(category.getName()))) {
                preparedStatement = connection.prepareStatement("select * from categories where name = ? ");
                preparedStatement.setString(1, category.getName());
                resultSet = preparedStatement.executeQuery();
            }
            if(resultSet == null || !resultSet.next() || name.equals(category.getName())) {
                preparedStatement = connection.prepareStatement("update categories as k set k.name = ?, k.description = ? where k.name = ?");
                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                preparedStatement.setString(3, name);
                preparedStatement.executeUpdate();


                preparedStatement.close();
                connection.close();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                this.closeResultSet(resultSet);
            }
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return category;
	}

}
