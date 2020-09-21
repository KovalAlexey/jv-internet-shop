package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.ProductDao;
import com.internet.shop.exceptions.DataProcessException;
import com.internet.shop.lib.Dao;
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
public class ProductDaoJdbcImpl implements ProductDao {
    @Override
    public Product create(Product item) {
        String query = "INSERT INTO products (productName, price) VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection()) {

            PreparedStatement statement = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, item.getName());
            statement.setDouble(2, item.getPrice());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                item.setId(resultSet.getLong("product_id"));
            }
            return item;
        } catch (SQLException e) {
            throw new DataProcessException("Can't create product!", e);

        }
    }

    @Override
    public Optional<Product> get(Long itemId) {
        String query = "SELECT * FROM products WHERE product_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, itemId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Product product = productFromSet(resultSet);
                product.setId(itemId);
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new DataProcessException("Can't find product with id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT * FROM products";
        List<Product> allProducts = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Product product = productFromSet(resultSet);
                product.setId(resultSet.getLong("product_id"));
                allProducts.add(product);
            }
        } catch (SQLException e) {
            throw new DataProcessException("Can't execute query!", e);
        }
        return allProducts;
    }

    @Override
    public Product update(Product item) {
        String query = "UPDATE products SET productName = ?, price = ? WHERE product_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, item.getName());
            statement.setDouble(2, item.getPrice());
            statement.setLong(3, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessException("Can't update product", e);
        }
        return item;
    }

    @Override
    public boolean delete(Long itemId) {
        String query = "UPDATE products SET deleted = true WHERE product_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, itemId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DataProcessException("Can't execute update query!", e);
        }
    }

    private Product productFromSet(ResultSet resultSet) {
        try {
            String productName = resultSet.getString("productName");
            double price = resultSet.getDouble("price");
            return new Product(productName, price);
        } catch (SQLException e) {
            throw new DataProcessException("Can't generate product...", e);
        }
    }
}
