package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String TABLE_NAME = "myusers";
    private static final String CREATE_USER_SQL = "INSERT INTO " + TABLE_NAME + " (firstname, lastname, age) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_SQL = "UPDATE " + TABLE_NAME + " SET firstname = ?, lastname = ?, age = ? WHERE id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String FIND_USER_BY_ID_SQL = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String FIND_USER_BY_NAME_SQL = "SELECT * FROM " + TABLE_NAME + " WHERE firstname = ? OR lastname = ?";
    private static final String FIND_ALL_USERS_SQL = "SELECT * FROM " + TABLE_NAME;

    public Long createUser(User user) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public User findUserById(Long userId) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_ID_SQL)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUserByName(String userName) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_NAME_SQL)) {
            ps.setString(1, userName);
            ps.setString(2, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(FIND_ALL_USERS_SQL)) {
            while (rs.next()) {
                users.add(getUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public User updateUser(User user)  {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_USER_SQL)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.setLong(4, user.getId());
            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteUser(Long userId) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL)) {
            ps.setLong(1, userId);
            int deletedRows = ps.executeUpdate();
            if (deletedRows == 0) {
                throw new SQLException("Deleting user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User getUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFirstName(rs.getString("firstname"));
        user.setLastName(rs.getString("lastname"));
        user.setAge(rs.getInt("age"));
        return user;
    }

}
