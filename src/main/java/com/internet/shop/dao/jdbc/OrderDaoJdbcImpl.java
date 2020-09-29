package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.OrderDao;
import com.internet.shop.exceptions.DataProcessException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Order;
import com.internet.shop.model.Product;
import com.internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class OrderDaoJdbcImpl implements OrderDao {

    @Override
    public List<Order> getUsersOrders(Long userId) {
        String query = "SELECT * FROM orders WHERE user_id = ? AND is_deleted = FALSE";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(getOrderFromSet(resultSet, connection));
            }
            return orders;
        } catch (SQLException e) {
            throw new DataProcessException("Can't get orders for user with id " + userId, e);
        }
    }

    @Override
    public List<Order> getAll() {
        String query = "SELECT * FROM orders WHERE is_deleted = FALSE";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(getOrderFromSet(resultSet, connection));
            }
            return orders;
        } catch (SQLException e) {
            throw new DataProcessException("Can't get all orders!", e);
        }
    }

    @Override
    public Order create(Order order) {
        String query = "INSERT INTO orders (user_id) VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, order.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setId(resultSet.getLong(1));
            }
            statement.close();
            addProductsToOrder(order, connection);
            return order;
        } catch (SQLException e) {
            throw new DataProcessException("Can't create order for user " + order.getUserId(), e);
        }
    }

    @Override
    public Optional<Order> get(Long orderId) {
        String query = "SELECT * FROM orders WHERE order_id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getOrderFromSet(resultSet, connection));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessException("Can't get order with id " + orderId, e);
        }
    }

    @Override
    public Order update(Order order) {
        String query = "DELETE FROM orders_products WHERE order_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, order.getId());
            statement.close();
            addProductsToOrder(order, connection);
            return order;
        } catch (SQLException e) {
            throw new DataProcessException("Can't update order with id " + order.getId(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE orders SET is_deleted = TRUE WHERE order_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessException("Can't delete order with id = " + id, e);
        }
    }

    private List<Product> getProductByOrder(Long orderId, Connection connection)
            throws SQLException {
        String query = "SELECT p.product_id, product_name, product_price"
                + " FROM products p"
                + " JOIN orders_products op ON p.product_id = op.product_id"
                + " WHERE op.order_id = ?;";
        List<Product> products = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, orderId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            products.add(new Product(resultSet.getLong("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getDouble("product_price")));
        }
        statement.close();
        return products;

    }

    private Order getOrderFromSet(ResultSet resultSet, Connection connection) throws SQLException {
        Long orderId = resultSet.getLong("order_id");
        Long userId = resultSet.getLong("user_id");
        List<Product> products = getProductByOrder(orderId, connection);
        return new Order(orderId, userId, products);
    }

    private void addProductsToOrder(Order order, Connection connection) throws SQLException {
        String query = "INSERT INTO orders_products (order_id, product_id)"
                + " VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        for (Product product : order.getProducts()) {
            statement.setLong(1, order.getId());
            statement.setLong(2, product.getId());
            statement.executeUpdate();
        }
        statement.close();
    }
}
