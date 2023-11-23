package com.ecommerce.backend.service;


import com.ecommerce.backend.configuration.JwtRequestFilter;
import com.ecommerce.backend.dao.CartDao;
import com.ecommerce.backend.dao.OrderDetailDao;
import com.ecommerce.backend.dao.ProductDao;
import com.ecommerce.backend.dao.UserDao;
import com.ecommerce.backend.entity.Cart432;
import com.ecommerce.backend.entity.OrderDetail432;
import com.ecommerce.backend.entity.OrderInput432;
import com.ecommerce.backend.entity.OrderProductQuantity432;
import com.ecommerce.backend.entity.Product432;
import com.ecommerce.backend.entity.TransactionDetails432;
import com.ecommerce.backend.entity.User432;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

@Service
public class OrderDetailService {

    private static final String ORDER_PLACED = "Placed";

    private static final String KEY = "rzp_test_AXBzvN2fkD4ESK";
    private static final String KEY_SECRET = "bsZmiVD7p1GMo6hAWiy4SHSH";
    private static final String CURRENCY = "INR";

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CartDao cartDao;

    public List<OrderDetail432> getAllOrderDetails(String status) {
        List<OrderDetail432> orderDetails = new ArrayList<>();

        if(status.equals("All")) {
            orderDetailDao.findAll().forEach(
                    x -> orderDetails.add(x)
            );
        } else {
            orderDetailDao.findByOrderStatus(status).forEach(
                    x -> orderDetails.add(x)
            );
        }


         return orderDetails;
    }

    public List<OrderDetail432> getOrderDetails() {
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User432 user = userDao.findById(currentUser).get();

        return orderDetailDao.findByUser(user);
    }

    public void placeOrder(OrderInput432 orderInput, boolean isSingleProductCheckout) {
        List<OrderProductQuantity432> productQuantityList = orderInput.getOrderProductQuantityList();

        for (OrderProductQuantity432 o: productQuantityList) {
            Product432 product = productDao.findById(o.getProductId()).get();

            String currentUser = JwtRequestFilter.CURRENT_USER;
            User432 user = userDao.findById(currentUser).get();

            OrderDetail432 orderDetail = new OrderDetail432(
                  orderInput.getFullName(),
                  orderInput.getFullAddress(),
                  orderInput.getContactNumber(),
                  orderInput.getAlternateContactNumber(),
                    ORDER_PLACED,
                    product.getProductDiscountedPrice() * o.getQuantity(),
                    product,
                    user,
                    orderInput.getTransactionId()
            );

            // empty the cart.
            if(!isSingleProductCheckout) {
                List<Cart432> carts = cartDao.findByUser(user);
                carts.stream().forEach(x -> cartDao.deleteById(x.getCartId()));
            }

            orderDetailDao.save(orderDetail);
        }
    }

    public void markOrderAsDelivered(Integer orderId) {
        OrderDetail432 orderDetail = orderDetailDao.findById(orderId).get();

        if(orderDetail != null) {
            orderDetail.setOrderStatus("Delivered");
            orderDetailDao.save(orderDetail);
        }

    }

    public TransactionDetails432 createTransaction(Double amount) throws RazorpayException {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", (amount * 100) );
            jsonObject.put("currency", CURRENCY);

            RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);

            Order order = razorpayClient.orders.create(jsonObject);

            TransactionDetails432 transactionDetails =  prepareTransactionDetails(order);
            return transactionDetails;
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private TransactionDetails432 prepareTransactionDetails(Order order) {
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");

        TransactionDetails432 transactionDetails = new TransactionDetails432(orderId, currency, amount, KEY);
        return transactionDetails;
    }
}
