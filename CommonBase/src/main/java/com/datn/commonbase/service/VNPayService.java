package com.datn.commonbase.service;

import com.datn.commonbase.configuration.VnpayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {
    private static Logger _log = LogManager.getLogger(VNPayService.class);

    public String createOrder(int total, String returnUrl) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

//        String returnUrl = "http://localhost:8083/order-success/" + returnUrl;
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }

    public String refund(String url, String createBy) {
        Map<String, String> paramsMap = new HashMap<>();
        try {
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    if (VnpayConfig.requestRefund.contains(key)) {
                        String value = "";
                        if (pair.length > 1) {
                            value = URLDecoder.decode(pair[1], "UTF-8");
                        }
                        paramsMap.put(key, value);
                    }
                }
            }
            String vnp_RequestId = VnpayConfig.getRandomNumber(8);
            paramsMap.put("vnp_RequestId", vnp_RequestId);
            paramsMap.put("vnp_Version", VnpayConfig.vnp_Version);
            paramsMap.put("vnp_TransactionType", "02");
            paramsMap.put("vnp_Command", "refund");
            paramsMap.put("vnp_CreateBy", createBy);
            paramsMap.put("vnp_TmnCode", VnpayConfig.vnp_TmnCode);
            paramsMap.put("vnp_TransactionDate", paramsMap.get("vnp_CreateDate"));
//            paramsMap.put("vnp_TransactionNo", "");
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            paramsMap.put("vnp_CreateDate", vnp_CreateDate);

            String data = paramsMap.get("vnp_RequestId") + "|" + paramsMap.get("vnp_Version") + "|" + paramsMap.get("vnp_Command") + "|" + paramsMap.get("vnp_TmnCode") + "|"
                    + paramsMap.get("vnp_TransactionType") + "|" + paramsMap.get("vnp_TxnRef") + "|" + paramsMap.get("vnp_Amount") + "|"
                    + paramsMap.get("vnp_TransactionDate") + "|" + paramsMap.get("vnp_CreateBy") + "|" + paramsMap.get("vnp_CreateDate") + "|" + paramsMap.get("vnp_IpAddr") + "|"
                    + paramsMap.get("vnp_OrderInfo");
            String signValue = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, data);
            paramsMap.put("vnp_SecureHash", signValue);
            return sendQueryRequest(VnpayConfig.vnp_ApiUrl, paramsMap);
        } catch (Exception e) {
            _log.error(e.getMessage());
            return "";
        }
    }

    public String queryDr(String url) {
        Map<String, String> paramsMap = new HashMap<>();
        try {
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    if (VnpayConfig.requestQueryDr.contains(key)) {
                        String value = "";
                        if (pair.length > 1) {
                            value = URLDecoder.decode(pair[1], "UTF-8");
                        }
                        paramsMap.put(key, value);
                    }
                }
            }
            String vnp_RequestId = VnpayConfig.getRandomNumber(8);
            paramsMap.put("vnp_RequestId", vnp_RequestId);
            paramsMap.put("vnp_Version", VnpayConfig.vnp_Version);
            paramsMap.put("vnp_Command", "querydr");
            paramsMap.put("vnp_TmnCode", VnpayConfig.vnp_TmnCode);
            paramsMap.put("vnp_TransactionDate", paramsMap.get("vnp_CreateDate"));
            String data = "" + paramsMap.get("vnp_RequestId") + "|" + paramsMap.get("vnp_Version") + "|" + paramsMap.get("vnp_Command") + "|"
                    + paramsMap.get("vnp_TmnCode") + "|" + paramsMap.get("vnp_TxnRef") + "|" + paramsMap.get("vnp_TransactionDate") + "|"
                    + paramsMap.get("vnp_CreateDate") + "|" + paramsMap.get("vnp_IpAddr") + "|" + paramsMap.get("vnp_OrderInfo");
            String signValue = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, data);
            paramsMap.put("vnp_SecureHash", signValue);
            System.out.println(signValue);
            return sendQueryRequest(VnpayConfig.vnp_ApiUrl, paramsMap);
        } catch (Exception e) {
            _log.error(e.getMessage());
            return "";
        }
    }

    public String sendQueryRequest(String urlString, Map<String, String> params) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Convert params to JSON string
            StringBuilder jsonParams = new StringBuilder("{");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                jsonParams.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
            }
            // Remove the last comma
            jsonParams.setLength(jsonParams.length() - 1);
            jsonParams.append("}");

            // Write JSON data to request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParams.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            String result = "";
            // Read response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                String responseText = "\"vnp_ResponseCode\":\"";
                int start = response.indexOf(responseText) + responseText.length();
                result = response.substring(start, start + 2);
                System.out.println(response.toString());
            }
            connection.disconnect();
            return result;
        } catch (Exception e) {
            _log.error(e.getMessage());
            return "";
        }
    }

    public int orderReturn(HttpServletRequest request) {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VnpayConfig.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }
}
