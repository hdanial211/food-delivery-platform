// Global variables
let currentCustomer = null;
let currentRestaurants = [];
let currentOrders = [];
let cart = JSON.parse(localStorage.getItem('cart')) || [];
let currentRestaurantMenu = [];

// API Base URL - adjust this to match your backend server
const API_BASE_URL = 'http://localhost:8080/api';

// Authentication token (in real app, this would be stored securely)
let authToken = localStorage.getItem('authToken') || '';

// DOM Elements
const navItems = document.querySelectorAll('.nav-item');
const contentSections = document.querySelectorAll('.content-section');
const pageTitle = document.getElementById('page-title');

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupEventListeners();
    loadDashboardData();
});

// Initialize application
function initializeApp() {
    // Check if user is authenticated
    if (!authToken) {
        console.warn('No auth token found. User should be redirected to login.');
    }
    
    // Load initial data
    loadCustomerProfile();
    loadRestaurants();
    loadCustomerOrders();
    updateCartDisplay();
}

// Setup event listeners
function setupEventListeners() {
    // Navigation
    navItems.forEach(item => {
        item.addEventListener('click', handleNavigation);
    });
    
    // Modal controls
    const menuModal = document.getElementById('menu-modal');
    const checkoutModal = document.getElementById('checkout-modal');
    const closeButtons = document.querySelectorAll('.close');
    
    closeButtons.forEach(btn => {
        btn.addEventListener('click', closeModals);
    });
    
    // Click outside modal to close
    window.addEventListener('click', (e) => {
        if (e.target === menuModal || e.target === checkoutModal) {
            closeModals();
        }
    });
    
    // Forms
    document.getElementById('customer-form').addEventListener('submit', handleCustomerProfileSubmit);
    document.getElementById('checkout-form').addEventListener('submit', handleCheckoutSubmit);
    
    // Search
    document.getElementById('restaurant-search').addEventListener('input', handleRestaurantSearch);
    
    // Filters
    document.getElementById('order-status-filter').addEventListener('change', filterOrders);
    
    // Cart actions
    document.getElementById('clear-cart-btn').addEventListener('click', clearCart);
    document.getElementById('checkout-btn').addEventListener('click', openCheckoutModal);
    document.getElementById('cancel-checkout').addEventListener('click', closeModals);
    
    // Logout
    document.querySelector('.logout-btn').addEventListener('click', handleLogout);
}

// Navigation handler
function handleNavigation(e) {
    e.preventDefault();
    
    const targetSection = e.currentTarget.dataset.section;
    
    // Update active nav item
    navItems.forEach(item => item.classList.remove('active'));
    e.currentTarget.classList.add('active');
    
    // Show target section
    contentSections.forEach(section => section.classList.remove('active'));
    document.getElementById(targetSection).classList.add('active');
    
    // Update page title
    const titles = {
        'dashboard': 'Dashboard',
        'restaurants': 'Restaurants',
        'orders': 'My Orders',
        'cart': 'Shopping Cart',
        'profile': 'Profile'
    };
    pageTitle.textContent = titles[targetSection] || 'Dashboard';
    
    // Load section-specific data
    switch(targetSection) {
        case 'dashboard':
            loadDashboardData();
            break;
        case 'restaurants':
            loadRestaurants();
            break;
        case 'orders':
            loadCustomerOrders();
            break;
        case 'cart':
            updateCartDisplay();
            break;
        case 'profile':
            loadCustomerProfile();
            break;
    }
}

// API Helper functions
async function apiRequest(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    const config = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${authToken}`,
            ...options.headers
        },
        ...options
    };
    
    try {
        const response = await fetch(url, config);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('API request failed:', error);
        showAlert('API request failed: ' + error.message, 'error');
        throw error;
    }
}

// Load dashboard data
async function loadDashboardData() {
    try {
        // Load orders for stats
        const orders = await apiRequest('/orders');
        const customerOrders = orders.filter(order => order.customerId === getCurrentCustomerId());
        
        const totalSpent = customerOrders.reduce((sum, order) => sum + order.totalAmount, 0);
        
        // Update stats
        document.getElementById('total-orders').textContent = customerOrders.length;
        document.getElementById('total-spent').textContent = `RM ${totalSpent.toFixed(2)}`;
        document.getElementById('favorite-restaurants').textContent = '3'; // Mock data
        document.getElementById('cart-items').textContent = cart.length;
        
        // Load recent orders
        loadRecentOrders(customerOrders.slice(0, 5));
        
    } catch (error) {
        console.error('Failed to load dashboard data:', error);
        // Use mock data if API fails
        loadMockDashboardData();
    }
}

// Load mock dashboard data
function loadMockDashboardData() {
    document.getElementById('total-orders').textContent = '8';
    document.getElementById('total-spent').textContent = 'RM 156.50';
    document.getElementById('favorite-restaurants').textContent = '3';
    document.getElementById('cart-items').textContent = cart.length;
    
    // Mock recent orders
    const mockOrders = [
        { orderId: 1001, restaurantName: 'Demo Restaurant', totalAmount: 25.50, status: 'DELIVERED', createdAt: '2024-07-18T10:30:00Z' },
        { orderId: 1002, restaurantName: 'Pizza Palace', totalAmount: 35.00, status: 'ON_THE_WAY', createdAt: '2024-07-18T11:15:00Z' }
    ];
    loadRecentOrders(mockOrders);
}

// Load recent orders for dashboard
function loadRecentOrders(orders) {
    const tbody = document.getElementById('recent-orders-tbody');
    tbody.innerHTML = '';
    
    if (orders.length === 0) {
        const row = document.createElement('tr');
        row.innerHTML = '<td colspan="6" class="empty-state">No orders yet. Start ordering from your favorite restaurants!</td>';
        tbody.appendChild(row);
        return;
    }
    
    orders.forEach(order => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>#${order.orderId}</td>
            <td>${order.restaurantName || 'Restaurant'}</td>
            <td>${order.items ? order.items.length : 1} items</td>
            <td>RM ${order.totalAmount.toFixed(2)}</td>
            <td><span class="order-status status-${order.status.toLowerCase()}">${order.status}</span></td>
            <td>${formatDateTime(order.createdAt)}</td>
        `;
        tbody.appendChild(row);
    });
}

// Load restaurants
async function loadRestaurants() {
    try {
        const restaurants = await apiRequest('/restaurants');
        currentRestaurants = restaurants;
        displayRestaurants(restaurants);
    } catch (error) {
        console.error('Failed to load restaurants:', error);
        // Use mock data if API fails
        loadMockRestaurants();
    }
}

// Load mock restaurants
function loadMockRestaurants() {
    const mockRestaurants = [
        {
            restaurantId: 1,
            name: 'Demo Restaurant',
            description: 'Best Malaysian food in town',
            logoUrl: 'https://via.placeholder.com/300x200?text=Demo+Restaurant',
            openingTime: '08:00',
            closingTime: '22:00',
            deliveryRadius: 10,
            isActive: true
        },
        {
            restaurantId: 2,
            name: 'Pizza Palace',
            description: 'Authentic Italian pizzas',
            logoUrl: 'https://via.placeholder.com/300x200?text=Pizza+Palace',
            openingTime: '11:00',
            closingTime: '23:00',
            deliveryRadius: 15,
            isActive: true
        },
        {
            restaurantId: 3,
            name: 'Burger House',
            description: 'Juicy burgers and fries',
            logoUrl: 'https://via.placeholder.com/300x200?text=Burger+House',
            openingTime: '10:00',
            closingTime: '24:00',
            deliveryRadius: 12,
            isActive: true
        }
    ];
    currentRestaurants = mockRestaurants;
    displayRestaurants(mockRestaurants);
}

// Display restaurants
function displayRestaurants(restaurants) {
    const restaurantsGrid = document.getElementById('restaurants-grid');
    restaurantsGrid.innerHTML = '';
    
    if (restaurants.length === 0) {
        restaurantsGrid.innerHTML = '<div class="empty-state"><h3>No restaurants available</h3><p>Check back later for more options!</p></div>';
        return;
    }
    
    restaurants.forEach(restaurant => {
        const restaurantCard = createRestaurantCard(restaurant);
        restaurantsGrid.appendChild(restaurantCard);
    });
}

// Create restaurant card
function createRestaurantCard(restaurant) {
    const card = document.createElement('div');
    card.className = 'restaurant-card';
    card.onclick = () => openRestaurantMenu(restaurant);
    
    card.innerHTML = `
        <img src="${restaurant.logoUrl || 'https://via.placeholder.com/300x200?text=Restaurant'}" 
             alt="${restaurant.name}" class="restaurant-image" 
             onerror="this.src='https://via.placeholder.com/300x200?text=Restaurant'">
        <div class="restaurant-content">
            <div class="restaurant-header">
                <h4 class="restaurant-name">${restaurant.name}</h4>
                <div class="restaurant-rating">
                    <span>⭐</span>
                    <span>4.5</span>
                </div>
            </div>
            <p class="restaurant-description">${restaurant.description || 'Delicious food awaits!'}</p>
            <div class="restaurant-info">
                <span>🕒 ${restaurant.openingTime} - ${restaurant.closingTime}</span>
                <span>🚚 ${restaurant.deliveryRadius}km radius</span>
            </div>
        </div>
    `;
    
    return card;
}

// Open restaurant menu modal
async function openRestaurantMenu(restaurant) {
    const modal = document.getElementById('menu-modal');
    const title = document.getElementById('restaurant-modal-title');
    const restaurantInfo = document.getElementById('restaurant-info');
    const menuItems = document.getElementById('modal-menu-items');
    
    title.textContent = restaurant.name;
    restaurantInfo.innerHTML = `
        <div class="restaurant-info">
            <h4>${restaurant.name}</h4>
            <p>${restaurant.description}</p>
            <p>🕒 ${restaurant.openingTime} - ${restaurant.closingTime} | 🚚 ${restaurant.deliveryRadius}km delivery</p>
        </div>
    `;
    
    try {
        // Load menu items for this restaurant
        const items = await apiRequest(`/restaurants/${restaurant.restaurantId}/menu`);
        currentRestaurantMenu = items;
        displayMenuItems(items, menuItems);
    } catch (error) {
        console.error('Failed to load menu:', error);
        // Use mock menu data
        const mockMenu = [
            { foodId: 1, name: 'Nasi Lemak', description: 'Traditional coconut rice', price: 8.50, category: 'Main Course', restaurantId: restaurant.restaurantId },
            { foodId: 2, name: 'Chicken Rice', description: 'Steamed chicken with rice', price: 7.00, category: 'Main Course', restaurantId: restaurant.restaurantId },
            { foodId: 3, name: 'Roti Canai', description: 'Flaky flatbread', price: 3.50, category: 'Appetizer', restaurantId: restaurant.restaurantId }
        ];
        displayMenuItems(mockMenu, menuItems);
    }
    
    modal.style.display = 'block';
}

// Display menu items
function displayMenuItems(items, container) {
    container.innerHTML = '';
    
    if (items.length === 0) {
        container.innerHTML = '<div class="empty-state"><h3>No menu items available</h3></div>';
        return;
    }
    
    items.forEach(item => {
        const menuItem = document.createElement('div');
        menuItem.className = 'menu-item';
        menuItem.innerHTML = `
            <div class="menu-item-header">
                <h5 class="menu-item-name">${item.name}</h5>
                <span class="menu-item-price">RM ${item.price.toFixed(2)}</span>
            </div>
            <p class="menu-item-description">${item.description || 'Delicious food item'}</p>
            <button class="btn btn-primary add-to-cart-btn" onclick="addToCart(${JSON.stringify(item).replace(/"/g, '"')})">
                Add to Cart
            </button>
        `;
        container.appendChild(menuItem);
    });
}

// Add item to cart
function addToCart(item) {
    const existingItem = cart.find(cartItem => cartItem.foodId === item.foodId);
    
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({
            ...item,
            quantity: 1
        });
    }
    
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartDisplay();
    showAlert(`${item.name} added to cart!`, 'success');
    
    // Update cart count in stats
    document.getElementById('cart-items').textContent = cart.length;
}

// Update cart display
function updateCartDisplay() {
    const cartItems = document.getElementById('cart-items');
    const cartSubtotal = document.getElementById('cart-subtotal');
    const cartTotal = document.getElementById('cart-total');
    const checkoutTotal = document.getElementById('checkout-total');
    
    cartItems.innerHTML = '';
    
    if (cart.length === 0) {
        cartItems.innerHTML = '<div class="empty-state"><h3>Your cart is empty</h3><p>Add some delicious items from restaurants!</p></div>';
        cartSubtotal.textContent = 'RM 0.00';
        cartTotal.textContent = 'RM 0.00';
        if (checkoutTotal) checkoutTotal.textContent = 'RM 0.00';
        return;
    }
    
    let subtotal = 0;
    
    cart.forEach((item, index) => {
        const itemTotal = item.price * item.quantity;
        subtotal += itemTotal;
        
        const cartItem = document.createElement('div');
        cartItem.className = 'cart-item';
        cartItem.innerHTML = `
            <div class="item-info">
                <div class="item-name">${item.name}</div>
                <div class="item-restaurant">Restaurant ${item.restaurantId}</div>
            </div>
            <div class="item-controls">
                <div class="quantity-controls">
                    <button class="quantity-btn" onclick="updateQuantity(${index}, -1)">-</button>
                    <span>${item.quantity}</span>
                    <button class="quantity-btn" onclick="updateQuantity(${index}, 1)">+</button>
                </div>
                <div class="item-price">RM ${itemTotal.toFixed(2)}</div>
                <button class="btn btn-secondary" onclick="removeFromCart(${index})">Remove</button>
            </div>
        `;
        cartItems.appendChild(cartItem);
    });
    
    const deliveryFee = 5.00;
    const total = subtotal + deliveryFee;
    
    cartSubtotal.textContent = `RM ${subtotal.toFixed(2)}`;
    cartTotal.textContent = `RM ${total.toFixed(2)}`;
    if (checkoutTotal) checkoutTotal.textContent = `RM ${total.toFixed(2)}`;
}

// Update item quantity in cart
function updateQuantity(index, change) {
    if (cart[index]) {
        cart[index].quantity += change;
        
        if (cart[index].quantity <= 0) {
            cart.splice(index, 1);
        }
        
        localStorage.setItem('cart', JSON.stringify(cart));
        updateCartDisplay();
        document.getElementById('cart-items').textContent = cart.length;
    }
}

// Remove item from cart
function removeFromCart(index) {
    if (cart[index]) {
        cart.splice(index, 1);
        localStorage.setItem('cart', JSON.stringify(cart));
        updateCartDisplay();
        document.getElementById('cart-items').textContent = cart.length;
        showAlert('Item removed from cart', 'success');
    }
}

// Clear entire cart
function clearCart() {
    if (confirm('Are you sure you want to clear your cart?')) {
        cart = [];
        localStorage.setItem('cart', JSON.stringify(cart));
        updateCartDisplay();
        document.getElementById('cart-items').textContent = '0';
        showAlert('Cart cleared', 'success');
    }
}

// Open checkout modal
function openCheckoutModal() {
    if (cart.length === 0) {
        showAlert('Your cart is empty!', 'warning');
        return;
    }
    
    const modal = document.getElementById('checkout-modal');
    const deliveryAddress = document.getElementById('delivery-address');
    
    // Pre-fill with customer address if available
    if (currentCustomer && currentCustomer.address) {
        deliveryAddress.value = currentCustomer.address;
    }
    
    modal.style.display = 'block';
}

// Handle checkout form submission
async function handleCheckoutSubmit(e) {
    e.preventDefault();
    
    const deliveryAddress = document.getElementById('delivery-address').value;
    const paymentMethod = document.getElementById('payment-method').value;
    const specialInstructions = document.getElementById('special-instructions').value;
    
    const orderData = {
        customerId: getCurrentCustomerId(),
        restaurantId: cart[0].restaurantId, // Assuming all items from same restaurant
        items: cart.map(item => ({
            foodId: item.foodId,
            quantity: item.quantity,
            pricePerUnit: item.price,
            specialInstructions: specialInstructions
        })),
        totalAmount: calculateCartTotal(),
        deliveryAddress: deliveryAddress,
        paymentMethod: paymentMethod,
        status: 'PENDING'
    };
    
    try {
        const order = await apiRequest('/orders', {
            method: 'POST',
            body: JSON.stringify(orderData)
        });
        
        // Clear cart after successful order
        cart = [];
        localStorage.setItem('cart', JSON.stringify(cart));
        updateCartDisplay();
        
        closeModals();
        showAlert('Order placed successfully!', 'success');
        
        // Switch to orders section
        document.querySelector('[data-section="orders"]').click();
        
    } catch (error) {
        console.error('Failed to place order:', error);
        showAlert('Failed to place order. Please try again.', 'error');
    }
}

// Calculate cart total
function calculateCartTotal() {
    const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    return subtotal + 5.00; // Add delivery fee
}

// Load customer orders
async function loadCustomerOrders() {
    try {
        const orders = await apiRequest('/orders');
        const customerOrders = orders.filter(order => order.customerId === getCurrentCustomerId());
        currentOrders = customerOrders;
        displayCustomerOrders(customerOrders);
    } catch (error) {
        console.error('Failed to load orders:', error);
        // Use mock data
        const mockOrders = [
            { orderId: 1001, restaurantName: 'Demo Restaurant', totalAmount: 25.50, status: 'DELIVERED', createdAt: '2024-07-18T10:30:00Z', deliveryAddress: '123 Main St' },
            { orderId: 1002, restaurantName: 'Pizza Palace', totalAmount: 35.00, status: 'ON_THE_WAY', createdAt: '2024-07-18T11:15:00Z', deliveryAddress: '456 Oak Ave' }
        ];
        displayCustomerOrders(mockOrders);
    }
}

// Display customer orders
function displayCustomerOrders(orders) {
    const ordersGrid = document.getElementById('customer-orders-grid');
    ordersGrid.innerHTML = '';
    
    if (orders.length === 0) {
        ordersGrid.innerHTML = '<div class="empty-state"><h3>No orders yet</h3><p>Start ordering from your favorite restaurants!</p></div>';
        return;
    }
    
    orders.forEach(order => {
        const orderCard = createOrderCard(order);
        ordersGrid.appendChild(orderCard);
    });
}

// Create order card
function createOrderCard(order) {
    const card = document.createElement('div');
    card.className = 'order-card';
    
    card.innerHTML = `
        <div class="order-header">
            <span class="order-id">Order #${order.orderId}</span>
            <span class="order-status status-${order.status.toLowerCase()}">${order.status}</span>
        </div>
        <div class="order-details">
            <p><strong>Restaurant:</strong> ${order.restaurantName || 'Restaurant'}</p>
            <p><strong>Total:</strong> RM ${order.totalAmount.toFixed(2)}</p>
            <p><strong>Address:</strong> ${order.deliveryAddress}</p>
            <p><strong>Date:</strong> ${formatDateTime(order.createdAt)}</p>
        </div>
        <div class="order-actions">
            <button class="btn btn-primary" onclick="trackOrder(${order.orderId})">Track Order</button>
            ${order.status === 'DELIVERED' ? '<button class="btn btn-secondary" onclick="reorder(' + order.orderId + ')">Reorder</button>' : ''}
        </div>
    `;
    
    return card;
}

// Track order
function trackOrder(orderId) {
    showAlert(`Tracking order #${orderId}. Feature coming soon!`, 'info');
}

// Reorder
function reorder(orderId) {
    showAlert(`Reorder feature for order #${orderId} coming soon!`, 'info');
}

// Handle restaurant search
function handleRestaurantSearch(e) {
    const searchTerm = e.target.value.toLowerCase();
    const filteredRestaurants = currentRestaurants.filter(restaurant =>
        restaurant.name.toLowerCase().includes(searchTerm) ||
        restaurant.description.toLowerCase().includes(searchTerm)
    );
    displayRestaurants(filteredRestaurants);
}

// Filter orders
function filterOrders() {
    const statusFilter = document.getElementById('order-status-filter').value;
    
    let filteredOrders = currentOrders;
    if (statusFilter) {
        filteredOrders = currentOrders.filter(order => order.status === statusFilter);
    }
    
    displayCustomerOrders(filteredOrders);
}

// Load customer profile
async function loadCustomerProfile() {
    try {
        const users = await apiRequest('/users');
        const customer = users.find(user => user.role === 'CUSTOMER');
        if (customer) {
            currentCustomer = customer;
            populateCustomerForm(customer);
            document.getElementById('customer-name').textContent = `Welcome, ${customer.fullName}!`;
        }
    } catch (error) {
        console.error('Failed to load customer profile:', error);
    }
}

// Populate customer form
function populateCustomerForm(customer) {
    document.getElementById('customer-name-input').value = customer.fullName || '';
    document.getElementById('customer-email').value = customer.email || '';
    document.getElementById('customer-phone').value = customer.phone || '';
    document.getElementById('customer-address').value = customer.address || '';
}

// Handle customer profile form submission
async function handleCustomerProfileSubmit(e) {
    e.preventDefault();
    
    const formData = {
        fullName: document.getElementById('customer-name-input').value,
        email: document.getElementById('customer-email').value,
        phone: document.getElementById('customer-phone').value,
        address: document.getElementById('customer-address').value
    };
    
    try {
        if (currentCustomer && currentCustomer.userId) {
            await apiRequest(`/users/${currentCustomer.userId}`, {
                method: 'PUT',
                body: JSON.stringify({...currentCustomer, ...formData})
            });
        }
        
        showAlert('Profile updated successfully!', 'success');
        document.getElementById('customer-name').textContent = `Welcome, ${formData.fullName}!`;
        
    } catch (error) {
        console.error('Failed to update profile:', error);
        showAlert('Failed to update profile', 'error');
    }
}

// Close modals
function closeModals() {
    document.getElementById('menu-modal').style.display = 'none';
    document.getElementById('checkout-modal').style.display = 'none';
}

// Get current customer ID (mock implementation)
function getCurrentCustomerId() {
    return currentCustomer ? currentCustomer.userId : 1;
}

// Utility functions
function formatDateTime(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString();
}

function showAlert(message, type = 'info') {
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.textContent = message;
    
    const mainContent = document.querySelector('.main-content');
    const header = document.querySelector('.header');
    mainContent.insertBefore(alert, header.nextSibling);
    
    setTimeout(() => {
        if (alert.parentNode) {
            alert.parentNode.removeChild(alert);
        }
    }, 5000);
}

// Handle logout
function handleLogout() {
    if (confirm('Are you sure you want to logout?')) {
        localStorage.removeItem('authToken');
        localStorage.removeItem('cart');
        window.location.href = 'login.html';
    }
}

// Make functions globally available for onclick handlers
window.addToCart = addToCart;
window.updateQuantity = updateQuantity;
window.removeFromCart = removeFromCart;
window.trackOrder = trackOrder;
window.reorder = reorder;
