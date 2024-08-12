package com.maxchauo.springserver.dao.order;

import java.util.ArrayList;
import java.util.List;



import com.maxchauo.springserver.dto.order.ListDto;
import com.maxchauo.springserver.dto.order.OrderDto;
import com.maxchauo.springserver.dto.order.RecipientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;



@Repository
public class OrderDaoImpl implements OrderDao {


    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private SqlInitializationAutoConfiguration sqlInitializationAutoConfiguration;

    @Override
    public boolean isStockAvailable(OrderDto orderDto) {
        List<ListDto> listDtos = orderDto.getList();
        for (ListDto listDto : listDtos) {

            String sql = "SELECT SUM(stock) AS total_stock FROM variant WHERE " +
                    "product_id = :productId AND color_code = :colorCode AND size = :size";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("productId", listDto.getId());
            params.addValue("colorCode", listDto.getColor().getCode());
            params.addValue("size", listDto.getSize());
            try {
                Long totalStock = template.queryForObject(sql, params, Long.class);
                if (totalStock == null || totalStock < listDto.getQty()) {
                    return false;
                }
                return true;
            } catch (DataAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Long> getVariantId(OrderDto orderDto, Long productId) {
        List<ListDto> listDtos = orderDto.getList();
        List<Long> variantIds = new ArrayList<>();
        for (ListDto listDto : listDtos) {
            String listColorCode = listDto.getColor().getCode();
            String listSize = listDto.getSize();

            String sql = "SELECT id FROM variant WHERE color_code = :colorCode" +
                    " AND size = :size AND product_id = :productId";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("colorCode", listColorCode);
            params.addValue("size", listSize);
            params.addValue("productId", productId);
            try {
                Long variantId = template.queryForObject(sql, params, Long.class);
                variantIds.add(variantId);
            } catch (EmptyResultDataAccessException e) {
                e.printStackTrace();
            }
        }
        return variantIds;
    }

    @Override
    public List<Long> getProductIdByName(OrderDto orderDto) {
        List<ListDto> listDtos = orderDto.getList();
        List<Long> productIds = new ArrayList<>();
        String sql = "SELECT id FROM product WHERE title = :title";
        for (ListDto listDto : listDtos) {
            String listName = listDto.getName();
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("title", listName);
            try {
                Long productId = template.queryForObject(sql, params, Long.class);
                if (productId != null) {
                    productIds.add(productId);
                }
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        }
        return productIds;
    }

    @Override
    public Long insertOrder(OrderDto orderDto, Long userId) {
        String orderSql = "INSERT INTO `order` (user_id, status, shipping,payment, subtotal, freight, total)" +
                "VALUES (:userId,:status,:shipping,:payment,:subtotal,:freight,:total)";
        MapSqlParameterSource orderParams = new MapSqlParameterSource();
        orderParams.addValue("userId", userId);
        orderParams.addValue("status", 0);
        orderParams.addValue("shipping", orderDto.getShipping());
        orderParams.addValue("payment", orderDto.getPayment());
        orderParams.addValue("subtotal", orderDto.getSubtotal());
        orderParams.addValue("freight", orderDto.getFreight());
        orderParams.addValue("total", orderDto.getTotal());
        KeyHolder orderKeyHolder = new GeneratedKeyHolder();
        template.update(orderSql, orderParams, orderKeyHolder, new String[]{"id"});
        return orderKeyHolder.getKey().longValue();
    }

    @Override
    public List<Long> insertList(OrderDto orderDto, Long productId, Long variantId, Long orderId) {
        String listSql = "INSERT INTO `list` (order_id,product_id,variant_Id,name, price, color_name, color_code, size, qty) " +
                "VALUES (:orderId, :productId,:variantId,:productName, :productPrice, :colorName, :colorCode, :size, :qty)";
        List<Long> generateIds = new ArrayList<>();
        List<ListDto> listDtos = orderDto.getList();
        for (ListDto listDto : listDtos) {
            MapSqlParameterSource listParams = new MapSqlParameterSource();
            listParams.addValue("orderId", orderId);
            listParams.addValue("productId", productId);
            listParams.addValue("variantId", variantId);
            listParams.addValue("productName", listDto.getName());
            listParams.addValue("productPrice", listDto.getPrice());
            listParams.addValue("colorName", listDto.getColor().getName());
            listParams.addValue("colorCode", listDto.getColor().getCode());
            listParams.addValue("size", listDto.getSize());
            listParams.addValue("qty", listDto.getQty());
            KeyHolder listKeyHolder = new GeneratedKeyHolder();
            template.update(listSql, listParams, listKeyHolder, new String[]{"id"});
            generateIds.add(listKeyHolder.getKey().longValue());
        }
        return generateIds;
    }


    @Override
    public Long insertRecipient(OrderDto orderDto, Long orderId) {
        String recipientSql = "INSERT INTO `recipient` (order_id, name, phone, email, address, time) " +
                "VALUES (:orderId, :name, :phone, :email, :address, :deliveryTime)";

        RecipientDto recipient = orderDto.getRecipient();
        MapSqlParameterSource recipientParams = new MapSqlParameterSource();
        recipientParams.addValue("orderId", orderId);
        recipientParams.addValue("name", recipient.getName());
        recipientParams.addValue("phone", recipient.getPhone());
        recipientParams.addValue("email", recipient.getEmail());
        recipientParams.addValue("address", recipient.getAddress());
        recipientParams.addValue("deliveryTime", recipient.getTime());
        KeyHolder recipientKeyHolder = new GeneratedKeyHolder();
        template.update(recipientSql, recipientParams, recipientKeyHolder, new String[]{"id"});
        return recipientKeyHolder.getKey().longValue();

    }


    @Override
    public boolean updateOrderStock(OrderDto orderDto) {
        List<ListDto> listDtos = orderDto.getList();
        for (ListDto listDto : listDtos) {
            try {
                String checkStockSql = "SELECT stock FROM variant WHERE product_id = :productId AND color_code = :colorCode AND size = :size";
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue("productId", listDto.getId());
                params.addValue("colorCode", listDto.getColor().getCode());
                params.addValue("size", listDto.getSize());

                Long currentStock = template.queryForObject(checkStockSql, params, Long.class);
                if (currentStock == null || currentStock < listDto.getQty()) {
                    System.err.println("Insufficient stock for product ID: " + listDto.getProductId());
                    return false;
                }
                String updateStockSql = "UPDATE variant SET stock = stock - :qty WHERE product_id = :productId AND color_code = :colorCode AND size = :size";
                params.addValue("qty", listDto.getQty());
                int rowsAffected = template.update(updateStockSql, params);

                if (rowsAffected != 1) {
                    System.err.println("Failed to update stock for product ID: " + listDto.getProductId());
                    return false;
                }
            } catch (DataAccessException e) {
                System.err.println("Database error while updating stock: " + e.getMessage());
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean updateOrderStatus(Long orderId, Long newStatus) {
        String updateSql = "UPDATE `order` SET status = :status WHERE id = :orderId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("status", newStatus);
        params.addValue("orderId", orderId);

        try {
            long updatedRows = template.update(updateSql, params);
            return updatedRows == 1;
        } catch (EmptyResultDataAccessException e) {
            System.err.println("No order found with ID: " + orderId);
            e.printStackTrace();
            throw e;
        } catch (DataAccessException e) {
            System.err.println("Error accessing the database: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
