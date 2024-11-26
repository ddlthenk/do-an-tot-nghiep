package com.datn.commonbase.service;

import com.datn.commonbase.entity.Cart;
import com.datn.commonbase.entity.Order;
import com.datn.commonbase.entity.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GHNService {
    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;
    private static Logger _log = LogManager.getLogger(GHNService.class);

    public boolean sendPostRequest2(int paymentTypeId, Order order) {
        boolean result = false;
        try {
            int codAmount = 0;
            if (paymentTypeId == 2) {
                codAmount = order.getTotal() - order.getDeliveryCharges();
            }
            int weight = 0;
            Map<Long, Product> productMap = new HashMap<>();
            JSONArray itemsArray = new JSONArray();
            List<Cart> cartList = cartService.getCartListByOrder(order.getOrderId());
            for (Cart cart : cartList) {
                if (productMap.containsKey(cart.getProductId())) {
                    weight += productMap.get(cart.getProductId()).getWeight();
                } else {
                    Product product = productService.getProduct(cart.getProductId());
                    productMap.put(product.getProductId(), product);
                    weight += product.getWeight();
                }
                JSONObject item = new JSONObject();
                item.put("name", productMap.get(cart.getProductId()).getProductTitle());
                item.put("quantity", cart.getQuantity());
                itemsArray.put(item);
            }

            JSONObject requestBody = new JSONObject();
            requestBody.put("payment_type_id", paymentTypeId);
            requestBody.put("required_note", "CHOXEMHANGKHONGTHU");
            requestBody.put("from_name", "Nguyen Trong Tam");
            requestBody.put("from_phone", "0328419491");
            requestBody.put("from_address", "72 Thành Thái, Phường 14, Quận 10, Hồ Chí Minh, Vietnam");
            requestBody.put("from_ward_name", "Phường 14");
            requestBody.put("from_district_name", "Quận 10");
            requestBody.put("from_province_name", "HCM");
            requestBody.put("to_name", order.getCustomerName());
            requestBody.put("to_phone", order.getCustomerPhone());
            requestBody.put("to_address", "72 Thành Thái, Phường 14, Quận 10, Hồ Chí Minh, Vietnam");
            requestBody.put("to_ward_name", "Phường 14");
            requestBody.put("to_district_name", "Quận 10");
            requestBody.put("to_province_name", "HCM");
            requestBody.put("cod_amount", codAmount);
            requestBody.put("weight", weight);
            requestBody.put("length", 10);
            requestBody.put("width", 20);
            requestBody.put("height", 10);
            requestBody.put("insurance_value", codAmount);
            requestBody.put("service_id", 0);
            requestBody.put("service_type_id", 2);
            requestBody.put("items", itemsArray);
            System.out.println(requestBody.toString());
            URL urlObj = new URL("https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create");
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("ShopId", "190992");
            con.setRequestProperty("Token", "5692b5e6-de9c-11ee-8bfa-8a2dda8ec551");
            con.setDoOutput(true);

            // Write data to request body
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JSONObject jsonObject = new JSONObject(response.toString());

                    // Get the value of the "code" field
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        // Get the value of the "order_code" field in the "data" section
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        String orderCode = dataObject.getString("order_code");
                        order.setOrderCode(orderCode);
                        orderService.saveOrder(order);
                        result = true;
                    }
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.err.println("Error response: " + response.toString());
                }
            }
            con.disconnect();
        } catch (Exception e) {
            _log.error(e.getMessage());
            return false;
        }
        return result;
    }

    public Map<String, Object> getOrderInfo(String orderCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";
            String requestBody = "{\"order_code\": \"" + orderCode + "\"}";
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Token", "5692b5e6-de9c-11ee-8bfa-8a2dda8ec551");
            con.setDoOutput(true);

            // Ghi dữ liệu vào request body
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Đọc phản hồi từ máy chủ
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                JsonObject dataObject = jsonObject.getAsJsonObject("data");
                String status = dataObject.get("status").getAsString();
                result.put("status", status);
            }
            con.disconnect();
        } catch (Exception e) {
            _log.error(e);
            result.put("status", "null");
        }
        return result;
    }

    public String cancelOrder(String orderCode) {
        try {
            URL url = new URL("https://dev-online-gateway.ghn.vn/shiip/public-api/v2/switch-status/cancel");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("ShopId", "190992");
            con.setRequestProperty("Token", "5692b5e6-de9c-11ee-8bfa-8a2dda8ec551");

            // Enable sending data
            con.setDoOutput(true);

            // Data to send
            String jsonInputString = "{\"order_codes\":[\"" + orderCode + "\"]}";

            // Send data
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Check response
            String responseCode = String.valueOf(con.getResponseCode());
            System.out.println("POST Response Code :: " + responseCode);
            return responseCode;

        } catch (Exception e) {
            _log.error(e.getMessage());
            return "";
        }
    }
}
