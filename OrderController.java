import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.PriorityQueue;

// Standalone REST API Controller for E-Commerce Engine
public class OrderController {

    // Internal Priority Queue Engine
    private static PriorityQueue<String> orderQueue = new PriorityQueue<>();

    public static void main(String[] args) throws IOException {
        // Create HTTP Server running on Port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Dummy Priority Data
        orderQueue.add("⚡ [VIP EXPRESS] ORD_003 - Payal - ₹85000");
        orderQueue.add("📦 [STANDARD] ORD_001 - Ramesh - ₹1500");
        orderQueue.add("📦 [STANDARD] ORD_002 - Suresh - ₹3200");

        // REST Endpoint: GET /api/v1/orders
        server.createContext("/api/v1/orders", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                StringBuilder response = new StringBuilder("=== 🛍️ E-COMMERCE PRIORITY QUEUE REST API RESPONSE ===\n\n");
                
                if (orderQueue.isEmpty()) {
                    response.append("⚠️ Queue is currently empty.");
                } else {
                    response.append("Current Highest Priority Order at Head:\n");
                    response.append("👉 ").append(orderQueue.peek()).append("\n\n");
                    response.append("All Pending Orders in Priority Queue:\n");
                    for (String order : orderQueue) {
                        response.append("• ").append(order).append("\n");
                    }
                }

                byte[] responseBytes = response.toString().getBytes();
                exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);
                
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
            }
        });

        // REST Endpoint: GET /api/v1/orders/dispatch
        server.createContext("/api/v1/orders/dispatch", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response;
                if (orderQueue.isEmpty()) {
                    response = "⚠️ No orders left to dispatch!";
                } else {
                    String dispatched = orderQueue.poll();
                    response = "🚀 DISPATCH SUCCESSFUL!\nProcessed Order: " + dispatched;
                }

                byte[] responseBytes = response.getBytes();
                exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);
                
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
            }
        });

        server.setExecutor(null); // Default executor
        System.out.println("=================================================");
        System.out.println("🚀 REST API Microservice Server Started on Port 8080!");
        System.out.println("🔗 Order Service Endpoint: http://localhost:8080/api/v1/orders");
        System.out.println("🔗 Dispatch Endpoint:      http://localhost:8080/api/v1/orders/dispatch");
        System.out.println("=================================================");
        server.start();
    }
}