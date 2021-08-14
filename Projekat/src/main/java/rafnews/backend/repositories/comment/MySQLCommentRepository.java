package rafnews.backend.repositories.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rafnews.backend.model.Comment;
import rafnews.backend.model.News;
import rafnews.backend.repositories.MySqlAbstractRepository;

public class MySQLCommentRepository extends MySqlAbstractRepository implements ICommentRepository{

	public Comment addComment(Comment comment) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();
            java.util.Date date = new java.util.Date();
            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO comments (author, text, date, news_id) VALUES (?,?,?,?)", generatedColumns);
            java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
            preparedStatement.setString(1, comment.getCreator());
            preparedStatement.setString(2, comment.getText());
            preparedStatement.setDate(3, sqlDate);
            preparedStatement.setInt(4, comment.getNewsId());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                comment.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return comment;
	}

	public Comment findComment(Integer id) {
		Comment komentar = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM comments where id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                String author = resultSet.getString("author");
                String content = resultSet.getString("text");
                Date createdAt = resultSet.getDate("date");

                komentar = new Comment();
                komentar.setCreationDate(createdAt);
                komentar.setCreator(author);
                komentar.setText(content);
                komentar.setId(id);
                komentar.setNewsId(resultSet.getInt("news_id"));
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

        return komentar;
	}

	public void deleteComment(Integer id) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM comments where id = ?");
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
	

	@Override
	public List<Comment> allComments(int page, int perPage) {
		List<Comment> commentList = new ArrayList<Comment>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from comments order by date desc limit "+(page-1)*perPage + " , " + perPage +  ";" );
            System.out.println(resultSet);
            while (resultSet.next()) {
            	 String author = resultSet.getString("author");
                 String content = resultSet.getString("text");
                 Date createdAt = resultSet.getDate("date");
            	Comment comment = new Comment();
            	comment.setCreationDate(createdAt);
            	comment.setCreator(author);
            	comment.setText(content);
            	comment.setId(resultSet.getInt("id"));
            	comment.setNewsId(resultSet.getInt("news_id"));

                commentList.add(comment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return commentList;
	}

	@Override
	public void dislikeComment(Integer id, String id2) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM comments_likes where comment_id = ? and session_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, id2);
           	resultSet = preparedStatement.executeQuery();
            if (!(resultSet.next())) {
            	preparedStatement = connection.prepareStatement("INSERT INTO comments_likes (comment_id, value, session_id) VALUES (?,?,?)");
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
	public void likeComment(Integer id, String id2) {
		Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM comments_likes where comment_id = ? and session_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, id2);
           	resultSet = preparedStatement.executeQuery();
            if (!(resultSet.next())) {
            	preparedStatement = connection.prepareStatement("INSERT INTO comments_likes (comment_id, value, session_id) VALUES (?,?,?)");
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
	public Integer karma(Integer id) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {
			connection = this.newConnection();

			preparedStatement = connection
					.prepareStatement("SELECT COUNT(*) as total FROM comments_likes where comment_id = ?");
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
	public Integer reactions(Integer id) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		int count = 0;
		try {
			connection = this.newConnection();

			preparedStatement = connection
					.prepareStatement("SELECT SUM(value) as total FROM comments_likes where comment_id = ?");
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

}
