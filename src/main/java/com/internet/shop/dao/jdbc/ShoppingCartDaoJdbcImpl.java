package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.exceptions.DataProcessException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
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
public class ShoppingCartDaoJdbcImpl implements ShoppingCartDao {

    @Override
    public ShoppingCart create(ShoppingCart cart) {
        String query = "INSERT INTO shopping_carts (user_id) VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, cart.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                cart.setId(resultSet.getLong(1));
            }
            return cart;
        } catch (SQLException e) {
            throw new DataProcessException("Can't create cart for user " + cart.getUserId(), e);
        }
    }

    @Override
    public Optional<ShoppingCart> get(Long cartId) {
        String query = "SELECT * FROM shopping_carts WHERE cart_id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ShoppingCart cart = getCartFromSet(resultSet, connection);
                return Optional.of(cart);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessException("Can't find cart with id " + cartId, e);
        }
    }

    @Override
    public Optional<ShoppingCart> getByUserId(Long userId) {
        String query = "SELECT * FROM shopping_carts WHERE user_id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ShoppingCart cart = getCartFromSet(resultSet, connection);
                return Optional.of(cart);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessException("Can't find cart with for user id " + userId, e);
        }
    }

    @Override
    public ShoppingCart update(ShoppingCart cart) {
        String query = "DELETE FROM shopping_carts_products WHERE cart_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, cart.getId());
            statement.executeUpdate();
            addProductsToCart(cart, connection);
            return cart;
        } catch (SQLException e) {
            throw new DataProcessException("Can't update cart with id " + cart.getId(), e);
        }
    }

    @Override
    public boolean delete(Long cartId) {
        String query = "UPDATE shopping_carts SET is_deleted = TRUE WHERE cart_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, cartId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessException("Can't execute update cart " + cartId, e);
        }
    }

    @Override
    public List<ShoppingCart> getAll() {
        String query = "SELECT * FROM shopping_carts WHERE is_deleted = FALSE";
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                shoppingCarts.add(getCartFromSet(resultSet, connection));
            }
            return shoppingCarts;
        } catch (SQLException e) {
            throw new DataProcessException("Can't get all carts!", e);
        }
    }

    private void addProductsToCart(ShoppingCart cart, Connection connection) throws SQLException {
        String query = "INSERT INTO shopping_carts_products (cart_id, product_id)"
                + " VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        for (Product product : cart.getProducts()) {
            statement.setLong(1, cart.getId());
            statement.setLong(2, product.getId());
            statement.executeUpdate();
        }
        statement.close();
    }

    private ShoppingCart getCartFromSet(ResultSet resultSet, Connection connection)
            throws SQLException {
        Long cartId = resultSet.getLong("cart_id");
        Long userId = resultSet.getLong("user_id");
        List<Product> products = getProductsByCart(cartId, connection);
        return new ShoppingCart(cartId, products, userId);
    }

    private List<Product> getProductsByCart(Long cartId, Connection connection)
            throws SQLException {
        String query = "SELECT p.product_id, product_name, product_price "
                + "FROM products p "
                + "INNER JOIN shopping_carts_products scp "
                + "ON scp.product_id = p.product_id "
                + "WHERE scp.cart_id = ?";
        List<Product> products = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, cartId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            products.add(new Product(resultSet.getLong("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getDouble("product_price")));
        }
        statement.close();
        return products;
    }
}
