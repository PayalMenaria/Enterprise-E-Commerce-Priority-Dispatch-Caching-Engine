import java.util.*;

// Order Model Class
class Order implements Comparable<Order> {
    private String orderId;
    private String customerName;
    private double amount;
    private boolean isExpress; // High Priority Flag
    private long timestamp;

    public Order(String orderId, String customerName, double amount, boolean isExpress) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.amount = amount;
        this.isExpress = isExpress;
        this.timestamp = System.currentTimeMillis();
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public boolean isExpress() { return isExpress; }

    // DSA Logic: Priority Queue me Priority define karne ke liye
    @Override
    public int compareTo(Order other) {
        // Express Delivery Orders ko pehle priority do
        if (this.isExpress != other.isExpress) {
            return this.isExpress ? -1 : 1; 
        }
        // Agar dono Express ya Normal hain, toh jo pehle aaya (FIFO) usko priority
        return Long.compare(this.timestamp, other.timestamp);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - ₹%.2f %s", 
            orderId, customerName, amount, (isExpress ? "(⚡ EXPRESS VIP)" : "(STANDARD)"));
    }
}

// Order Processing System
public class ECommerceEngine {
    // DSA Data Structure: Heap-based PriorityQueue
    private PriorityQueue<Order> orderProcessingQueue;
    
    // Fast Search Caching (HashMap - O(1) Lookup)
    private Map<String, String> productCatalogCache;

    public ECommerceEngine() {
        orderProcessingQueue = new PriorityQueue<>();
        productCatalogCache = new HashMap<>();

        // Cache me Kuch Products populate karte hain
        productCatalogCache.put("PROD_101", "MacBook Pro M3 - ₹1,80,000");
        productCatalogCache.put("PROD_102", "iPhone 15 Pro - ₹1,20,000");
        productCatalogCache.put("PROD_103", "Sony WH-1000XM5 - ₹29,900");
    }

    // Order Place Karna
    public void placeOrder(Order order) {
        orderProcessingQueue.add(order);
        System.out.println("✅ Order Placed: " + order);
    }

    // High-Priority Order Dispatch Engine
    public void processNextOrder() {
        if (orderProcessingQueue.isEmpty()) {
            System.out.println("⚠️ Queue me koi order baki nahi hai.");
            return;
        }
        Order processedOrder = orderProcessingQueue.poll();
        System.out.println("🚀 Processing & Shipping: " + processedOrder);
    }

    // Product Fast Cache Lookup (O(1) Time Complexity)
    public void searchProduct(String productId) {
        System.out.println("\n🔍 Searching Product ID: " + productId);
        if (productCatalogCache.containsKey(productId)) {
            System.out.println("⚡ Cache Hit! Product Details: " + productCatalogCache.get(productId));
        } else {
            System.out.println("❌ Cache Miss! Product catalog me nahi mila.");
        }
    }

    public static void main(String[] args) {
        System.out.println("====== 🛍️ ENTERPRISE E-COMMERCE ENGINE STARTED ======\n");

        ECommerceEngine engine = new ECommerceEngine();

        // 1. Fast Cache Search Test
        engine.searchProduct("PROD_101");
        engine.searchProduct("PROD_999");

        System.out.println("\n--- 📦 Incoming Orders (DSA Priority Queue Test) ---");

        // Normal Orders pehle aate hain
        engine.placeOrder(new Order("ORD_001", "Ramesh", 1500, false));
        engine.placeOrder(new Order("ORD_002", "Suresh", 3200, false));

        // VIP/Express Order baad me aata hai
        engine.placeOrder(new Order("ORD_003", "Payal (VIP)", 85000, true)); // Express!
        engine.placeOrder(new Order("ORD_004", "Priya", 800, false));

        System.out.println("\n--- ⚡ Dispatching Orders Priority Wise ---");
        // Priority Queue auto-sort karega (Express Order Pehle Jayega!)
        engine.processNextOrder(); // Should be Payal (Express)
        engine.processNextOrder(); // Should be Ramesh (First Normal)
        engine.processNextOrder(); // Should be Suresh
        engine.processNextOrder(); // Should be Priya
    }
}