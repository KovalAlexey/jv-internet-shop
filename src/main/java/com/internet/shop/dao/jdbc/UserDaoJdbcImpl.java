package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.UserDao;
import com.internet.shop.exceptions.DataProcessException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Dao
public class UserDaoJdbcImpl implements UserDao {
    @Override
    public Optional<User> findByLogin(String login) {
        String query = "SELECT * FROM users WHERE user_login = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getUserFromSet(resultSet, connection);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new DataProcessException("Can't find product with login " + login, e);
        }
        return Optional.empty();
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO users (user_name, password, user_login, user_salt) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getLogin());
            statement.setBytes(4, user.getSalt());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
            }
            statement.close();
            setUserRoles(user, connection);
            return user;
        } catch (SQLException e) {
            throw new DataProcessException("Can't create user " + user.getLogin(), e);
        }
    }

    @Override
    public Optional<User> get(Long userId) {
        String query = "SELECT * FROM users "
                + "JOIN users_roles ON users.user_id = users_roles.user_id "
                + "JOIN roles ON users_roles.role_id = roles.role_id "
                + "WHERE users.user_id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getUserFromSet(resultSet, connection);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new DataProcessException("Can't find user with id " + userId, e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM users WHERE is_deleted = FALSE";
        List<User> allUsers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = getUserFromSet(resultSet, connection);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new DataProcessException("Can't get all users!", e);
        }
        return allUsers;
    }

    @Override
    public User update(User user) {
        String query = "UPDATE users SET user_name = ?, user_login = ?, password = ?, user_salt = ? "
                + "WHERE user_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setBytes(4, user.getSalt());
            statement.setLong(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessException("Cant update user with login " + user.getLogin(), e);
        }
        return user;
    }

    @Override
    public boolean delete(Long userId) {
        String query = "UPDATE users "
                + "SET is_deleted = TRUE WHERE user_id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessException("Can't execute update user " + userId, e);
        }
    }

    private void setUserRoles(User user, Connection connection)
            throws SQLException {
        String queryForRoles = "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)";
        String queryForIds = "SELECT role_id FROM roles WHERE role_name=?";
        PreparedStatement statementForRoles = connection.prepareStatement(queryForRoles);
        PreparedStatement statementForIds = connection.prepareStatement(queryForIds);
        for (Role role : user.getRoles()) {
            statementForIds.setString(1, role.getRoleName().toString());
            ResultSet resultSet = statementForIds.executeQuery();
            while (resultSet.next()) {
                role.setId(resultSet.getLong("role_id"));
            }
            statementForRoles.setLong(1, user.getId());
            statementForRoles.setLong(2, role.getId());
            statementForRoles.executeUpdate();
        }
        statementForIds.close();
        statementForRoles.close();
    }

    private Set<Role> getUserRole(Long userId, Connection connection)
            throws SQLException {
        Set<Role> roles = new HashSet<>();
        String query = "SELECT role_name FROM users_roles "
                + "INNER JOIN roles ON  users_roles.role_id=roles.role_id "
                + "WHERE users_roles.user_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            roles.add(Role.of(resultSet.getString("role_name")));
        }
        statement.close();
        return roles;
    }

    private User getUserFromSet(ResultSet resultSet, Connection connection) throws SQLException {

        Long userId = resultSet.getLong("user_id");
        String userName = resultSet.getString("user_name");
        String userLogin = resultSet.getString("user_login");
        String userPassword = resultSet.getString("password");
        byte[] userSalt = resultSet.getBytes("user_salt");
        User user = new User(userName, userLogin, userPassword, getUserRole(userId, connection), userSalt);
        user.setId(userId);
        return user;
    }
}
