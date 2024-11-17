package com.datn.commonbase.service;

import com.datn.commonbase.entity.Cart;
import com.datn.commonbase.entity.Order;
import com.datn.commonbase.entity.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    public void sendPostRequest(int paymentTypeId, Order order) {
        try {
            int codAmount = 0;
            if (paymentTypeId == 2) {
                codAmount = order.getTotal();
            }
            int weight = 0;
            Map<Long, Product> productMap = new HashMap();
            StringBuilder item = new StringBuilder();
            List<Cart> cartList = cartService.getCartListByOrder(order.getOrderId());
            for (Cart cart : cartList) {
                if (productMap.containsKey(cart.getProductId())) {
                    weight += productMap.get(cart.getProductId()).getWeight();
                } else {
                    Product product = productService.getProduct(cart.getProductId());
                    productMap.put(product.getProductId(), product);
                    weight += product.getWeight();
                }
                item.append("         {\n" + "             \"name\":\"")
                        .append(productMap.get(cart.getProductId()).getProductTitle())
                        .append("\",\n").append("             \"quantity\": ")
                        .append(cart.getQuantity())
                        .append("\n")
                        .append("         },\n");
            }
            if (item.length() > 0) {
                item.deleteCharAt(item.length() - 1);
                item.deleteCharAt(item.length() - 1);
            }
            String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";
            String requestBody = "{\n" +
                    "    \"payment_type_id\": " + paymentTypeId + ",\n" +
                    "    \"required_note\": \"CHOXEMHANGKHONGTHU\",\n" +
                    "    \"to_name\": \"" + order.getCustomerName() + "\",\n" +
                    "    \"to_phone\": \"" + order.getCustomerPhone() + "\",\n" +
                    "    \"to_address\": \"" + order.getSpecificAddress() + "\",\n" +
                    "    \"to_ward_name\": \"" + order.getWardName() + "\",\n" +
                    "    \"to_ward_code\": \"" + order.getWardId() + "\",\n" +
                    "    \"to_district_name\": \"" + order.getDistrictName() + "\",\n" +
                    "    \"cod_amount\": " + codAmount + ",\n" +
                    "    \"weight\": " + weight + ",\n" +
                    "    \"length\": 10,\n" +
                    "    \"width\": 20,\n" +
                    "    \"height\": 10,\n" +
                    "    \"insurance_value\": " + order.getTotal() + ",\n" +
                    "    \"service_id\": 0,\n" +
                    "    \"service_type_id\":2,\n" +
                    "    \"items\": [\n" +
                    item.toString() +
                    "\n     ]\n" +
                    "}";
            System.out.println(requestBody);
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("ShopId", "190992");
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
                System.out.println(response.toString());
            }

            con.disconnect();
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
    }

    public boolean sendPostRequest2(int paymentTypeId, Order order) {
        boolean result = false;
        try {
            int codAmount = 0;
            if (paymentTypeId == 2) {
                codAmount = order.getTotal() - order.getDeliveryCharges();
            }
            int weight = 0;
            Map<Long, Product> productMap = new HashMap();
            StringBuilder item = new StringBuilder();
            List<Cart> cartList = cartService.getCartListByOrder(order.getOrderId());
            for (Cart cart : cartList) {
                if (productMap.containsKey(cart.getProductId())) {
                    weight += productMap.get(cart.getProductId()).getWeight();
                } else {
                    Product product = productService.getProduct(cart.getProductId());
                    productMap.put(product.getProductId(), product);
                    weight += product.getWeight();
                }
                item.append("         {\n" + "             \"name\":\"")
                        .append(productMap.get(cart.getProductId()).getProductTitle())
                        .append("\",\n").append("             \"quantity\": ")
                        .append(cart.getQuantity())
                        .append("\n")
                        .append("         },\n");
            }
            if (item.length() > 0) {
                item.deleteCharAt(item.length() - 1);
                item.deleteCharAt(item.length() - 1);
            }
            String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";
            String requestBody = "{\n" +
                    "    \"payment_type_id\": " + paymentTypeId + ",\n" +
                    "    \"required_note\": \"CHOXEMHANGKHONGTHU\",\n" +
                    "    \"from_name\": \"Nguyen Trong Tam\",\n" +
                    "    \"from_phone\": \"0328419491\",\n" +
                    "    \"from_address\": \"72 Thành Thái, Phường 14, Quận 10, Hồ Chí Minh, Vietnam\",\n" +
                    "    \"from_ward_name\": \"Phường 14\",\n" +
                    "    \"from_district_name\": \"Quận 10\",\n" +
                    "    \"from_province_name\": \"HCM\",\n" +
                    "    \"to_name\": \"" + order.getCustomerName() + "\",\n" +
                    "    \"to_phone\": \"" + order.getCustomerPhone() + "\",\n" +
                    "    \"to_address\": \"72 Thành Thái, Phường 14, Quận 10, Hồ Chí Minh, Vietnam\",\n" +
                    "    \"to_ward_name\": \"Phường 14\",\n" +
                    "    \"to_district_name\": \"Quận 10\",\n" +
                    "    \"to_province_name\": \"HCM\",\n" +
                    "    \"cod_amount\": " + codAmount + ",\n" +
                    "    \"weight\": " + weight + ",\n" +
                    "    \"length\": 10,\n" +
                    "    \"width\": 20,\n" +
                    "    \"height\": 10,\n" +
                    "    \"insurance_value\": " + codAmount + ",\n" +
                    "    \"service_id\": 0,\n" +
                    "    \"service_type_id\":2,\n" +
                    "    \"items\": [\n" +
                    item.toString() +
                    "\n     ]\n" +
                    "}";
            System.out.println(requestBody);
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("ShopId", "190992");
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

                // Lấy giá trị của trường "code"
                int code = jsonObject.get("code").getAsInt();
                if (code == 200) {
                    // Lấy giá trị của trường "order_code" trong phần "data"
                    JsonObject dataObject = jsonObject.getAsJsonObject("data");
                    String orderCode = dataObject.get("order_code").getAsString();
                    order.setOrderCode(orderCode);
                    orderService.saveOrder(order);
                    result = true;
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
