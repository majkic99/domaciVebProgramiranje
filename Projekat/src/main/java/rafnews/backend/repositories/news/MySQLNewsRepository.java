package rafnews.backend.repositories.news;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rafnews.backend.model.Category;
import rafnews.backend.model.Comment;
import rafnews.backend.model.Keyword;
import rafnews.backend.model.News;
import rafnews.backend.model.Role;
import rafnews.backend.model.Status;
import rafnews.backend.model.User;
import rafnews.backend.repositories.MySqlAbstractRepository;

public class MySQLNewsRepository extends MySqlAbstractRepository implements INewsRepository {

	public News addNews(News news) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSetCategory = null;

		try {
			connection = this.newConnection();

			String[] generatedColumns = { "id" };

			preparedStatement = connection.prepareStatement(
					"INSERT INTO news (title, text, date, author, visits, category_id) VALUES(?, ?,?, ?,?,?)",
					generatedColumns);
			java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
			preparedStatement.setString(1, news.getTitle());
			preparedStatement.setString(2, news.getText());
			preparedStatement.setDate(3, sqlDate);
			preparedStatement.setString(4, news.getCreator().getEmail());
			preparedStatement.setInt(5, 0);
			preparedStatement.setInt(6, news.getCategory().getId());

			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();

			if (resultSet.next()) {
				news.setId(resultSet.getInt(1));
			}

			for (Keyword kw : news.getKeywords()) {
				preparedStatement = connection
						.prepareStatement("Insert into news_keywords (news_id, keyword_id) VALUES (?,?)");
				preparedStatement.setInt(1, news.getId());
				preparedStatement.setInt(2, kw.getId());
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(preparedStatement);
			if (resultSet != null) {
				this.closeResultSet(resultSet);
			}
			this.closeConnection(connection);
		}

		return news;
	}

	public News updateNews(News news) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSetCategory = null;

		try {
			connection = this.newConnection();

			String[] generatedColumns = { "id" };
			preparedStatement = connection.prepareStatement(
					"update news as n set n.title = ?, n.text = ?, n.date = ?, n.author = ?, n.visits = ?, n.category_id = ?");
			java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
			preparedStatement.setString(1, news.getTitle());
			preparedStatement.setString(2, news.getText());
			preparedStatement.setDate(3, sqlDate);
			preparedStatement.setString(4, news.getCreator().getEmail());
			preparedStatement.setInt(5, 0);
			preparedStatement.setInt(6, news.getCategory().getId());

			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();

			if (resultSet.next()) {
				news.setId(resultSet.getInt(1));
			}

			for (Keyword kw : news.getKeywords()) {
				preparedStatement = connection
						.prepareStatement("Insert into news_keywords (news_id, keyword_id) VALUES (?,?)");
				preparedStatement.setInt(1, news.getId());
				preparedStatement.setInt(2, kw.getId());
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(preparedStatement);
			if (resultSet != null) {
				this.closeResultSet(resultSet);
			}
			this.closeConnection(connection);
		}

		return news;
	}

	public List<News> allNews(int page, int perPage) {
		List<News> vestiList = new ArrayList<News>();

		Connection connection = null;
		Statement statement = null;

		ResultSet resultSet = null;
		ResultSet resultSetUser = null;
		ResultSet resultSetCategory = null;

		PreparedStatement preparedStatement = null;
		int i = 0;

		try {
			connection = this.newConnection();

			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"select * from news order by date desc limit " + (page - 1) * perPage + " , " + perPage + ";");

			while (resultSet.next()) {
				News news = new News();
				news.setId(resultSet.getInt("id"));
				news.setTitle(resultSet.getString("title"));
				news.setText(resultSet.getString("text"));
				news.setVisitNumber(resultSet.getInt("visits"));
				news.setCreationTime(resultSet.getDate("date"));

				preparedStatement = connection.prepareStatement("select * from users where email = ?");
				preparedStatement.setString(1, resultSet.getString("author"));
				ResultSet resultSetAuthor = preparedStatement.executeQuery();
				resultSetAuthor.next();
				User user = new User();
				user.setId(resultSetAuthor.getInt("id"));
				user.setEmail(resultSetAuthor.getString("email"));
				user.setName(resultSetAuthor.getString("name"));
				user.setSurname(resultSetAuthor.getString("surname"));
				Role role = resultSetAuthor.getInt("type") == 0 ? Role.ADMIN : Role.CONTENTWRITER;
				user.setRole(role);
				Status status = resultSetAuthor.getInt("status") == 0 ? Status.INACTIVE : Status.ACTIVE;
				user.setStatus(status);

				preparedStatement = connection.prepareStatement("select * from categories where id = ?");
				preparedStatement.setInt(1, resultSet.getInt("category_id"));
				resultSetCategory = preparedStatement.executeQuery();

				while (resultSetCategory.next()) {
					Category category = new Category();
					category.setName(resultSetCategory.getString("name"));
					category.setDescription(resultSetCategory.getString("description"));
					category.setId(resultSet.getInt("category_id"));
					synchronized (this) {
						news.setCategory(category);
					}
				}
				preparedStatement = connection.prepareStatement("select * from news_keywords where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetKeywords = preparedStatement.executeQuery();
				news.setKeywords(new ArrayList<Keyword>());
				while (resultSetKeywords.next()) {
					Keyword keyword = new Keyword();
					keyword.setId(resultSetKeywords.getInt("keyword_id"));
					news.getKeywords().add(keyword);
					preparedStatement = connection.prepareStatement("select * from keywords where id = ?");
					preparedStatement.setInt(1, keyword.getId());
					ResultSet resultSetOneKeyword = preparedStatement.executeQuery();
					resultSetOneKeyword.next();
					keyword.setName(resultSetOneKeyword.getString("word"));
				}
				preparedStatement = connection.prepareStatement("select * from comments where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetComments = preparedStatement.executeQuery();
				news.setComments(new ArrayList<Comment>());
				while (resultSetComments.next()) {
					Comment comment = new Comment();
					comment.setId(resultSetComments.getInt("id"));
					comment.setNewsId(news.getId());
					comment.setCreator(resultSetComments.getString("author"));
					comment.setText(resultSetComments.getString("text"));
					comment.setCreationDate(resultSetComments.getDate("date"));
					news.getComments().add(comment);
				}

				vestiList.add(news);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(statement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}

		return vestiList;
	}

	public List<News> allNewsByVisits(int page, int perPage) {
		List<News> vestiList = new ArrayList<News>();

		Connection connection = null;
		Statement statement = null;

		ResultSet resultSet = null;
		ResultSet resultSetUser = null;
		ResultSet resultSetCategory = null;

		PreparedStatement preparedStatement = null;
		int i = 0;

		try {
			connection = this.newConnection();

			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"select * from news order by visits desc limit " + (page - 1) * perPage + " , " + perPage + ";");

			while (resultSet.next()) {
				News news = new News();
				news.setId(resultSet.getInt("id"));
				news.setTitle(resultSet.getString("title"));
				news.setText(resultSet.getString("text"));
				news.setVisitNumber(resultSet.getInt("visits"));
				news.setCreationTime(resultSet.getDate("date"));

				preparedStatement = connection.prepareStatement("select * from users where email = ?");
				preparedStatement.setString(1, resultSet.getString("author"));
				ResultSet resultSetAuthor = preparedStatement.executeQuery();
				resultSetAuthor.next();
				User user = new User();
				user.setId(resultSetAuthor.getInt("id"));
				user.setEmail(resultSetAuthor.getString("email"));
				user.setName(resultSetAuthor.getString("name"));
				user.setSurname(resultSetAuthor.getString("surname"));
				Role role = resultSetAuthor.getInt("type") == 0 ? Role.ADMIN : Role.CONTENTWRITER;
				user.setRole(role);
				Status status = resultSetAuthor.getInt("status") == 0 ? Status.INACTIVE : Status.ACTIVE;
				user.setStatus(status);

				preparedStatement = connection.prepareStatement("select * from categories where id = ?");
				preparedStatement.setInt(1, resultSet.getInt("category_id"));
				resultSetCategory = preparedStatement.executeQuery();

				while (resultSetCategory.next()) {
					Category category = new Category();
					category.setName(resultSetCategory.getString("name"));
					category.setDescription(resultSetCategory.getString("description"));
					category.setId(resultSet.getInt("category_id"));
					synchronized (this) {
						news.setCategory(category);
					}
				}
				preparedStatement = connection.prepareStatement("select * from news_keywords where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetKeywords = preparedStatement.executeQuery();
				news.setKeywords(new ArrayList<Keyword>());
				while (resultSetKeywords.next()) {
					Keyword keyword = new Keyword();
					keyword.setId(resultSetKeywords.getInt("keyword_id"));
					news.getKeywords().add(keyword);
					preparedStatement = connection.prepareStatement("select * from keywords where id = ?");
					preparedStatement.setInt(1, keyword.getId());
					ResultSet resultSetOneKeyword = preparedStatement.executeQuery();
					resultSetOneKeyword.next();
					keyword.setName(resultSetOneKeyword.getString("word"));
				}
				preparedStatement = connection.prepareStatement("select * from comments where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetComments = preparedStatement.executeQuery();
				news.setComments(new ArrayList<Comment>());
				while (resultSetComments.next()) {
					Comment comment = new Comment();
					comment.setId(resultSetComments.getInt("id"));
					comment.setNewsId(news.getId());
					comment.setCreator(resultSetComments.getString("author"));
					comment.setText(resultSetComments.getString("text"));
					comment.setCreationDate(resultSetComments.getDate("date"));
					news.getComments().add(comment);
				}

				vestiList.add(news);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(statement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}

		return vestiList;
	}

	public News findNews(Integer id) {
		List<News> vestiList = new ArrayList<News>();

		Connection connection = null;
		Statement statement = null;

		ResultSet resultSet = null;
		ResultSet resultSetUser = null;
		ResultSet resultSetCategory = null;

		PreparedStatement preparedStatement = null;
		int i = 0;

		try {
			connection = this.newConnection();

			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from news where id = " + id);

			while (resultSet.next()) {
				News news = new News();
				news.setId(resultSet.getInt("id"));
				news.setTitle(resultSet.getString("title"));
				news.setText(resultSet.getString("text"));
				news.setVisitNumber(resultSet.getInt("visits"));
				news.setCreationTime(resultSet.getDate("date"));

				preparedStatement = connection.prepareStatement("select * from users where email = ?");
				preparedStatement.setString(1, resultSet.getString("author"));
				ResultSet resultSetAuthor = preparedStatement.executeQuery();
				resultSetAuthor.next();
				User user = new User();
				user.setId(resultSetAuthor.getInt("id"));
				user.setEmail(resultSetAuthor.getString("email"));
				user.setName(resultSetAuthor.getString("name"));
				user.setSurname(resultSetAuthor.getString("surname"));
				Role role = resultSetAuthor.getInt("type") == 0 ? Role.ADMIN : Role.CONTENTWRITER;
				user.setRole(role);
				Status status = resultSetAuthor.getInt("status") == 0 ? Status.INACTIVE : Status.ACTIVE;
				user.setStatus(status);

				preparedStatement = connection.prepareStatement("select * from categories where id = ?");
				preparedStatement.setInt(1, resultSet.getInt("category_id"));
				resultSetCategory = preparedStatement.executeQuery();

				while (resultSetCategory.next()) {
					Category category = new Category();
					category.setName(resultSetCategory.getString("name"));
					category.setDescription(resultSetCategory.getString("description"));
					category.setId(resultSet.getInt("category_id"));
					synchronized (this) {
						news.setCategory(category);
					}
				}

				preparedStatement = connection.prepareStatement("select * from news_keywords where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetKeywords = preparedStatement.executeQuery();
				news.setKeywords(new ArrayList<Keyword>());
				while (resultSetKeywords.next()) {
					Keyword keyword = new Keyword();
					keyword.setId(resultSetKeywords.getInt("keyword_id"));
					news.getKeywords().add(keyword);
					preparedStatement = connection.prepareStatement("select * from keywords where id = ?");
					preparedStatement.setInt(1, keyword.getId());
					ResultSet resultSetOneKeyword = preparedStatement.executeQuery();
					resultSetOneKeyword.next();
					keyword.setName(resultSetOneKeyword.getString("word"));
				}
				preparedStatement = connection.prepareStatement("select * from comments where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetComments = preparedStatement.executeQuery();
				news.setComments(new ArrayList<Comment>());
				while (resultSetComments.next()) {
					Comment comment = new Comment();
					comment.setId(resultSetComments.getInt("id"));
					comment.setNewsId(news.getId());
					comment.setCreator(resultSetComments.getString("author"));
					comment.setText(resultSetComments.getString("text"));
					comment.setCreationDate(resultSetComments.getDate("date"));
					news.getComments().add(comment);
				}

				vestiList.add(news);

				statement.executeQuery("update news as n set n.visits = n.visits + 1 where n.id = " + news.getId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(statement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}
		
		return vestiList.get(0);
	}

	public void deleteNews(Integer id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.newConnection();

			preparedStatement = connection.prepareStatement("DELETE FROM news where id = ?");
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

	public List<News> allByCategory(String name, int page, int perPage) {
		List<News> vestiList = new ArrayList<News>();

		Connection connection = null;
		Statement statement = null;

		ResultSet resultSet = null;
		ResultSet resultSetUser = null;
		ResultSet resultSetCategory = null;

		PreparedStatement preparedStatement = null;
		int i = 0;

		try {
			connection = this.newConnection();

			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"select * from news order by date desc limit " + (page - 1) * perPage + " , " + perPage + ";");

			while (resultSet.next()) {
				News news = new News();
				news.setId(resultSet.getInt("id"));
				news.setTitle(resultSet.getString("title"));
				news.setText(resultSet.getString("text"));
				news.setVisitNumber(resultSet.getInt("visits"));
				news.setCreationTime(resultSet.getDate("date"));

				preparedStatement = connection.prepareStatement("select * from users where email = ?");
				preparedStatement.setString(1, resultSet.getString("author"));
				ResultSet resultSetAuthor = preparedStatement.executeQuery();
				resultSetAuthor.next();
				User user = new User();
				user.setId(resultSetAuthor.getInt("id"));
				user.setEmail(resultSetAuthor.getString("email"));
				user.setName(resultSetAuthor.getString("name"));
				user.setSurname(resultSetAuthor.getString("surname"));
				Role role = resultSetAuthor.getInt("type") == 0 ? Role.ADMIN : Role.CONTENTWRITER;
				user.setRole(role);
				Status status = resultSetAuthor.getInt("status") == 0 ? Status.INACTIVE : Status.ACTIVE;
				user.setStatus(status);

				preparedStatement = connection.prepareStatement("select * from categories where id = ?");
				preparedStatement.setInt(1, resultSet.getInt("category_id"));
				resultSetCategory = preparedStatement.executeQuery();

				while (resultSetCategory.next()) {
					Category category = new Category();
					category.setName(resultSetCategory.getString("name"));
					category.setDescription(resultSetCategory.getString("description"));
					category.setId(resultSet.getInt("category_id"));
					synchronized (this) {
						news.setCategory(category);
					}
				}
				if (!(news.getCategory().getName().equalsIgnoreCase(name))) {
					continue;
				}
				preparedStatement = connection.prepareStatement("select * from news_keywords where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetKeywords = preparedStatement.executeQuery();
				news.setKeywords(new ArrayList<Keyword>());
				while (resultSetKeywords.next()) {
					Keyword keyword = new Keyword();
					keyword.setId(resultSetKeywords.getInt("keyword_id"));
					news.getKeywords().add(keyword);
					preparedStatement = connection.prepareStatement("select * from keywords where id = ?");
					preparedStatement.setInt(1, keyword.getId());
					ResultSet resultSetOneKeyword = preparedStatement.executeQuery();
					resultSetOneKeyword.next();
					keyword.setName(resultSetOneKeyword.getString("word"));
				}
				preparedStatement = connection.prepareStatement("select * from comments where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetComments = preparedStatement.executeQuery();
				news.setComments(new ArrayList<Comment>());
				while (resultSetComments.next()) {
					Comment comment = new Comment();
					comment.setId(resultSetComments.getInt("id"));
					comment.setNewsId(news.getId());
					comment.setCreator(resultSetComments.getString("author"));
					comment.setText(resultSetComments.getString("text"));
					comment.setCreationDate(resultSetComments.getDate("date"));
					news.getComments().add(comment);
				}

				vestiList.add(news);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(statement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}

		return vestiList;
	}

	public List<News> allByKeyword(Integer id, int page, int perPage) {
		List<News> vestiList = new ArrayList<News>();

		Connection connection = null;
		Statement statement = null;

		ResultSet resultSet = null;
		ResultSet resultSetUser = null;
		ResultSet resultSetCategory = null;

		PreparedStatement preparedStatement = null;
		int i = 0;

		try {
			connection = this.newConnection();

			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"select * from news order by date desc limit " + (page - 1) * perPage + " , " + perPage + ";");

			while (resultSet.next()) {
				News news = new News();
				news.setId(resultSet.getInt("id"));
				news.setTitle(resultSet.getString("title"));
				news.setText(resultSet.getString("text"));
				news.setVisitNumber(resultSet.getInt("visits"));
				news.setCreationTime(resultSet.getDate("date"));

				preparedStatement = connection.prepareStatement("select * from users where email = ?");
				preparedStatement.setString(1, resultSet.getString("author"));
				ResultSet resultSetAuthor = preparedStatement.executeQuery();
				resultSetAuthor.next();
				User user = new User();
				user.setId(resultSetAuthor.getInt("id"));
				user.setEmail(resultSetAuthor.getString("email"));
				user.setName(resultSetAuthor.getString("name"));
				user.setSurname(resultSetAuthor.getString("surname"));
				Role role = resultSetAuthor.getInt("type") == 0 ? Role.ADMIN : Role.CONTENTWRITER;
				user.setRole(role);
				Status status = resultSetAuthor.getInt("status") == 0 ? Status.INACTIVE : Status.ACTIVE;
				user.setStatus(status);

				preparedStatement = connection.prepareStatement("select * from categories where id = ?");
				preparedStatement.setInt(1, resultSet.getInt("category_id"));
				resultSetCategory = preparedStatement.executeQuery();

				while (resultSetCategory.next()) {
					Category category = new Category();
					category.setName(resultSetCategory.getString("name"));
					category.setDescription(resultSetCategory.getString("description"));
					category.setId(resultSet.getInt("category_id"));
					synchronized (this) {
						news.setCategory(category);
					}
				}

				preparedStatement = connection.prepareStatement("select * from news_keywords where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetKeywords = preparedStatement.executeQuery();
				news.setKeywords(new ArrayList<Keyword>());
				while (resultSetKeywords.next()) {
					Keyword keyword = new Keyword();
					keyword.setId(resultSetKeywords.getInt("keyword_id"));
					news.getKeywords().add(keyword);
					preparedStatement = connection.prepareStatement("select * from keywords where id = ?");
					preparedStatement.setInt(1, keyword.getId());
					ResultSet resultSetOneKeyword = preparedStatement.executeQuery();
					resultSetOneKeyword.next();
					keyword.setName(resultSetOneKeyword.getString("word"));
				}
				boolean hasKwId = false;
				for (Keyword kw : news.getKeywords()) {
					if (kw.getId() == id) {
						hasKwId = true;
					}
				}
				if (!hasKwId) {
					continue;
				}
				preparedStatement = connection.prepareStatement("select * from comments where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetComments = preparedStatement.executeQuery();
				news.setComments(new ArrayList<Comment>());
				while (resultSetComments.next()) {
					Comment comment = new Comment();
					comment.setId(resultSetComments.getInt("id"));
					comment.setNewsId(news.getId());
					comment.setCreator(resultSetComments.getString("author"));
					comment.setText(resultSetComments.getString("text"));
					comment.setCreationDate(resultSetComments.getDate("date"));
					news.getComments().add(comment);
				}

				vestiList.add(news);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(statement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}

		return vestiList;
	}

	@Override
	public List<Keyword> allKeywordByNews(Integer id) {
		News news = findNews(id);
		return news.getKeywords();
	}

	@Override
	public List<Comment> allCommentsByNews(Integer id) {
		News news = findNews(id);
		return news.getComments();
	}

	@Override
	public void dislikeNews(Integer id, String id2) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.newConnection();

			preparedStatement = connection
					.prepareStatement("SELECT * FROM news_likes where news_id = ? and session_id = ?");
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, id2);
			resultSet = preparedStatement.executeQuery();
			if (!(resultSet.next())) {
				preparedStatement = connection
						.prepareStatement("INSERT INTO news_likes (news_id, value, session_id) VALUES (?,?,?)");
				preparedStatement.setInt(1, id);
				preparedStatement.setInt(2, -1);
				preparedStatement.setString(3, id2);
				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(preparedStatement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}

	}

	@Override
	public void likeNews(Integer id, String id2) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.newConnection();

			preparedStatement = connection
					.prepareStatement("SELECT * FROM news_likes where news_id = ? and session_id = ?");
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, id2);
			resultSet = preparedStatement.executeQuery();
			if (!(resultSet.next())) {
				preparedStatement = connection
						.prepareStatement("INSERT INTO news_likes (news_id, value, session_id) VALUES (?,?,?)");
				preparedStatement.setInt(1, id);
				preparedStatement.setInt(2, 1);
				preparedStatement.setString(3, id2);
				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(preparedStatement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}
	}

	@Override
	public Integer reactions(Integer id) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {
			connection = this.newConnection();

			preparedStatement = connection
					.prepareStatement("SELECT COUNT(*) as total FROM news_likes where news_id = ?");
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(preparedStatement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}
		return count;
	}

	@Override
	public Integer karma(Integer id) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {
			connection = this.newConnection();

			preparedStatement = connection
					.prepareStatement("SELECT SUM(value) as total FROM news_likes where news_id = ?");
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(preparedStatement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}
		return count;
	}

	@Override
	public List<News> advancedNewsSearch(String result, Integer page, Integer perPage) {
		List<News> vestiList = new ArrayList<News>();

		Connection connection = null;
		Statement statement = null;

		ResultSet resultSet = null;
		ResultSet resultSetUser = null;
		ResultSet resultSetCategory = null;

		PreparedStatement preparedStatement = null;
		int i = 0;

		try {
			connection = this.newConnection();

			statement = connection.createStatement();
			resultSet = statement
					.executeQuery("select * from news WHERE title LIKE '%" + result + "%' or text LIKE '%" + result
							+ "%' limit " + (page - 1) * perPage + " , " + perPage + ";");

			while (resultSet.next()) {
				News news = new News();
				news.setId(resultSet.getInt("id"));
				news.setTitle(resultSet.getString("title"));
				news.setText(resultSet.getString("text"));
				news.setVisitNumber(resultSet.getInt("visits"));
				news.setCreationTime(resultSet.getDate("date"));

				preparedStatement = connection.prepareStatement("select * from users where email = ?");
				preparedStatement.setString(1, resultSet.getString("author"));
				ResultSet resultSetAuthor = preparedStatement.executeQuery();
				resultSetAuthor.next();
				User user = new User();
				user.setId(resultSetAuthor.getInt("id"));
				user.setEmail(resultSetAuthor.getString("email"));
				user.setName(resultSetAuthor.getString("name"));
				user.setSurname(resultSetAuthor.getString("surname"));
				Role role = resultSetAuthor.getInt("type") == 0 ? Role.ADMIN : Role.CONTENTWRITER;
				user.setRole(role);
				Status status = resultSetAuthor.getInt("status") == 0 ? Status.INACTIVE : Status.ACTIVE;
				user.setStatus(status);

				preparedStatement = connection.prepareStatement("select * from categories where id = ?");
				preparedStatement.setInt(1, resultSet.getInt("category_id"));
				resultSetCategory = preparedStatement.executeQuery();

				while (resultSetCategory.next()) {
					Category category = new Category();
					category.setName(resultSetCategory.getString("name"));
					category.setDescription(resultSetCategory.getString("description"));
					category.setId(resultSet.getInt("category_id"));
					synchronized (this) {
						news.setCategory(category);
					}
				}
				preparedStatement = connection.prepareStatement("select * from news_keywords where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetKeywords = preparedStatement.executeQuery();
				news.setKeywords(new ArrayList<Keyword>());
				while (resultSetKeywords.next()) {
					Keyword keyword = new Keyword();
					keyword.setId(resultSetKeywords.getInt("keyword_id"));
					news.getKeywords().add(keyword);
					preparedStatement = connection.prepareStatement("select * from keywords where id = ?");
					preparedStatement.setInt(1, keyword.getId());
					ResultSet resultSetOneKeyword = preparedStatement.executeQuery();
					resultSetOneKeyword.next();
					keyword.setName(resultSetOneKeyword.getString("word"));
				}
				preparedStatement = connection.prepareStatement("select * from comments where news_id = ?");
				preparedStatement.setInt(1, news.getId());
				ResultSet resultSetComments = preparedStatement.executeQuery();
				news.setComments(new ArrayList<Comment>());
				while (resultSetComments.next()) {
					Comment comment = new Comment();
					comment.setId(resultSetComments.getInt("id"));
					comment.setNewsId(news.getId());
					comment.setCreator(resultSetComments.getString("author"));
					comment.setText(resultSetComments.getString("text"));
					comment.setCreationDate(resultSetComments.getDate("date"));
					news.getComments().add(comment);
				}

				vestiList.add(news);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(statement);
			this.closeResultSet(resultSet);
			this.closeConnection(connection);
		}

		return vestiList;
	}
}
