package com.example.ecom.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final JdbcTemplate jdbc;
    private final RestTemplate http = new RestTemplate();

    public OrdersController(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    @Value("${app.functions.orderWebhook}")
    private String orderWebhook;

    record OrderRequest(String customerName, int productId, int quantity){}

    @PostMapping
    public Map<String,Object> create(@RequestBody OrderRequest req){
        // price demo only
        int price = switch (req.productId()){
            case 1 -> 499;
            case 2 -> 299;
            case 3 -> 899;
            default -> 100;
        };
        int total = price * Math.max(1, req.quantity());

        jdbc.update("INSERT INTO Orders(customerName, productId, quantity, totalAmount, status, createdAt) VALUES (?,?,?,?,?,SYSDATETIMEOFFSET())",
                req.customerName(), req.productId(), req.quantity(), total, "PLACED");

        // Notify Azure Function (HTTP Trigger)
        try{
            Map<String,Object> payload = new HashMap<>();
            payload.put("customerName", req.customerName());
            payload.put("productId", req.productId());
            payload.put("quantity", req.quantity());
            payload.put("totalAmount", total);
            payload.put("createdAt", OffsetDateTime.now().toString());
            http.postForEntity(orderWebhook, payload, String.class);
        }catch (Exception ex){
            // ignore in demo
        }

        Map<String,Object> resp = new HashMap<>();
        resp.put("message", "Order placed! Total ₹" + total);
        return resp;
    }
}
