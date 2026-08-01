import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;

// Order Model
class Order implements Comparable<Order> {
    private String orderId;
    private String customerName;
    private double amount;
    private boolean isExpress;
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

    @Override
    public int compareTo(Order other) {
        if (this.isExpress != other.isExpress) {
            return this.isExpress ? -1 : 1; 
        }
        return Long.compare(this.timestamp, other.timestamp);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - ₹%.2f %s", 
            orderId, customerName, amount, (isExpress ? "⚡ VIP EXPRESS" : "STANDARD"));
    }
}

public class ECommerceGUI extends JFrame {
    private PriorityQueue<Order> orderQueue;
    private Map<String, String> productCatalogCache;
    private int orderCounter = 1;

    private JTextField nameField, amountField, searchField;
    private JCheckBox expressCheckBox;
    private DefaultListModel<String> queueListModel;
    private JTextArea logArea;

    public ECommerceGUI() {
        orderQueue = new PriorityQueue<>();
        productCatalogCache = new HashMap<>();

        // Populate Product Cache (Redis Simulation)
        productCatalogCache.put("PROD_101", "MacBook Pro M3 - ₹1,80,000");
        productCatalogCache.put("PROD_102", "iPhone 15 Pro - ₹1,20,000");
        productCatalogCache.put("PROD_103", "Sony WH-1000XM5 - ₹29,900");

        setTitle("🛍️ Enterprise E-Commerce Engine (DSA Powered)");
        setSize(880, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 116, 240)); // Flipkart Blue
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("⚡ High-Priority Order Dispatch & Caching System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Main Center Panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // --- LEFT PANEL ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("🛒 Place New Order & Cache Search"));

        // Search Box
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search Cache 🔍");
        searchBox.add(new JLabel("Product ID:"));
        searchBox.add(searchField);
        searchBox.add(searchBtn);

        // Form Box (Fixed Input Sizes)
        JPanel formBox = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(15);
        amountField = new JTextField(15);
        expressCheckBox = new JCheckBox("VIP Express Delivery (Priority)");
        expressCheckBox.setFont(new Font("SansSerif", Font.BOLD, 11));
        expressCheckBox.setForeground(new Color(220, 53, 69));

        gbc.gridx = 0; gbc.gridy = 0;
        formBox.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        formBox.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formBox.add(new JLabel("Order Amount (₹):"), gbc);
        gbc.gridx = 1;
        formBox.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formBox.add(new JLabel("Priority Tag:"), gbc);
        gbc.gridx = 1;
        formBox.add(expressCheckBox, gbc);

        // Place Order Button
        JButton placeOrderBtn = new JButton("Place Order 📦");
        placeOrderBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        placeOrderBtn.setBackground(new Color(40, 116, 240));
        placeOrderBtn.setForeground(Color.WHITE);
        placeOrderBtn.setOpaque(true);
        placeOrderBtn.setBorderPainted(false);
        placeOrderBtn.setFocusPainted(false);
        placeOrderBtn.setPreferredSize(new Dimension(200, 35));

        leftPanel.add(searchBox);
        leftPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        leftPanel.add(formBox);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(placeOrderBtn);

        // --- RIGHT PANEL ---
        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setBorder(BorderFactory.createTitledBorder("🚀 Priority Queue & Dispatch"));

        queueListModel = new DefaultListModel<>();
        JList<String> queueList = new JList<>(queueListModel);
        queueList.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane queueScroll = new JScrollPane(queueList);
        queueScroll.setPreferredSize(new Dimension(300, 160));

        JButton processNextBtn = new JButton("Process Highest Priority Order ⚡");
        processNextBtn.setBackground(new Color(40, 167, 69)); // Success Green
        processNextBtn.setForeground(Color.WHITE);
        processNextBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        processNextBtn.setOpaque(true);
        processNextBtn.setBorderPainted(false);
        processNextBtn.setFocusPainted(false);
        processNextBtn.setPreferredSize(new Dimension(300, 35));

        logArea = new JTextArea(8, 20);
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane logScroll = new JScrollPane(logArea);

        JPanel rightTop = new JPanel(new BorderLayout(5, 5));
        rightTop.add(new JLabel("Current Queue Status (Sorted by Priority):"), BorderLayout.NORTH);
        rightTop.add(queueScroll, BorderLayout.CENTER);
        rightTop.add(processNextBtn, BorderLayout.SOUTH);

        rightPanel.add(rightTop, BorderLayout.NORTH);
        rightPanel.add(logScroll, BorderLayout.CENTER);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel, BorderLayout.CENTER);

        // Action Listeners
        searchBtn.addActionListener(e -> searchProduct());
        placeOrderBtn.addActionListener(e -> placeOrder());
        processNextBtn.addActionListener(e -> processOrder());
    }

    private void searchProduct() {
        String pId = searchField.getText().trim().toUpperCase();
        if (pId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product ID daliye! (e.g. PROD_101)");
            return;
        }
        if (productCatalogCache.containsKey(pId)) {
            log("⚡ CACHE HIT [O(1)]: Found " + productCatalogCache.get(pId));
        } else {
            log("❌ CACHE MISS: Product ID " + pId + " not in Redis/Cache");
        }
    }

    private void placeOrder() {
        String name = nameField.getText().trim();
        String amtStr = amountField.getText().trim();
        boolean isExpress = expressCheckBox.isSelected();

        if (name.isEmpty() || amtStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kripya Naam aur Amount bharein!");
            return;
        }

        try {
            double amt = Double.parseDouble(amtStr);
            String oId = String.format("ORD_%03d", orderCounter++);
            Order order = new Order(oId, name, amt, isExpress);

            orderQueue.add(order);
            log("✅ ORDER PLACED: " + order);

            updateQueueUI();
            nameField.setText("");
            amountField.setText("");
            expressCheckBox.setSelected(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount me sirf numbers daalein!");
        }
    }

    private void processOrder() {
        if (orderQueue.isEmpty()) {
            log("⚠️ Queue is Empty! No orders left to dispatch.");
            return;
        }

        Order processed = orderQueue.poll();
        log("🚀 DISPATCHED: " + processed);
        updateQueueUI();
    }

    private void updateQueueUI() {
        queueListModel.clear();
        Order[] arr = orderQueue.toArray(new Order[0]);
        Arrays.sort(arr);
        for (Order o : arr) {
            queueListModel.addElement(o.toString());
        }
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ECommerceGUI().setVisible(true));
    }
}