# Customer Dashboard - Food Delivery Platform

Dashboard frontend untuk pelanggan dalam sistem Food Delivery Platform. Interface ini membolehkan pelanggan browse restoran, buat pesanan, track delivery, dan manage profile mereka.

## 🚀 Ciri-Ciri Utama

### 1. **Customer Dashboard**
- Statistik peribadi (total orders, amount spent, favorite restaurants)
- Recent orders overview
- Welcome banner dengan quick actions

### 2. **Restaurant Discovery**
- Browse semua restoran yang tersedia
- Search restaurants by name atau description
- View restaurant details (hours, delivery radius, rating)
- Click restaurant untuk view menu

### 3. **Menu & Ordering**
- View restaurant menu dengan categories
- Add items to shopping cart
- Manage cart (update quantities, remove items)
- Secure checkout process dengan multiple payment options

### 4. **Order Management**
- View all personal orders dengan status tracking
- Filter orders by status (Pending, Delivered, etc.)
- Track order progress in real-time
- Reorder favorite meals

### 5. **Shopping Cart**
- Add/remove items from cart
- Update item quantities
- View order summary dengan delivery fees
- Persistent cart (saved in localStorage)

### 6. **Customer Profile**
- Update personal information
- Manage delivery addresses
- View account statistics
- Secure logout functionality

## 📁 Struktur File

```
customer/
├── index.html          # Main customer dashboard
├── login.html          # Customer login page
├── styles.css          # Modern CSS styling
├── script.js           # JavaScript functionality
└── README.md           # Documentation ini
```

## 🛠️ Teknologi Yang Digunakan

- **HTML5** - Struktur halaman yang semantic
- **CSS3** - Modern styling dengan Flexbox & Grid
- **Vanilla JavaScript** - Functionality dan API integration
- **Google Fonts (Inter)** - Typography yang clean
- **LocalStorage** - Cart persistence dan user data

## 🎨 Design Features

- **Modern UI/UX** - Clean dan user-friendly design
- **Responsive Layout** - Berfungsi di semua device sizes
- **Sidebar Navigation** - Easy access ke semua sections
- **Modal Windows** - Restaurant menus dan checkout
- **Real-time Updates** - Cart dan order status updates
- **Loading States** - Better user experience
- **Alert System** - User feedback untuk actions

## 🔧 Setup & Usage

### 1. **Login Process**
```
1. Buka customer/login.html
2. Gunakan demo credentials:
   - Email: customer@demo.com
   - Password: demo123
3. Click "Login to Dashboard"
```

### 2. **Main Features Flow**
```
Dashboard → View Stats & Recent Orders
Restaurants → Browse & Select Restaurant → View Menu → Add to Cart
Cart → Review Items → Checkout → Place Order
Orders → Track Status → Reorder
Profile → Update Information
```

### 3. **API Integration**
Dashboard ini berinteraksi dengan backend Java melalui REST endpoints:
- `GET /api/restaurants` - List semua restoran
- `GET /api/restaurants/{id}/menu` - Menu untuk restoran specific
- `POST /api/orders` - Create new order
- `GET /api/orders` - Get customer orders
- `PUT /api/users/{id}` - Update customer profile

## 📱 Responsive Design

- **Desktop** (>1024px): Full sidebar dengan grid layouts
- **Tablet** (768px-1024px): Collapsible sidebar
- **Mobile** (<768px): Stack layouts dengan touch-friendly buttons

## 🛒 Shopping Cart Features

### Cart Management
- **Add Items**: Click "Add to Cart" dari restaurant menu
- **Update Quantities**: Use +/- buttons dalam cart
- **Remove Items**: Click "Remove" button
- **Clear Cart**: Clear semua items sekaligus
- **Persistent Storage**: Cart saved dalam localStorage

### Checkout Process
1. Review cart items dan total
2. Enter delivery address
3. Select payment method (Card/PayPal/Cash)
4. Add special instructions (optional)
5. Place order

## 📊 Order Status Flow

```
PENDING → ACCEPTED → PREPARING → READY_FOR_DELIVERY → ON_THE_WAY → DELIVERED
```

Customers boleh track orders dalam real-time dan receive updates.

## 🔐 Authentication & Security

- **JWT Token Authentication** - Secure API calls
- **LocalStorage Management** - Token dan cart persistence
- **Auto-redirect** - Redirect ke login jika tidak authenticated
- **Secure Logout** - Clear semua stored data

## 🎯 User Experience Features

### Dashboard Statistics
- **Total Orders**: Jumlah orders yang pernah dibuat
- **Total Spent**: Amount yang telah dibelanjakan
- **Favorite Restaurants**: Number of preferred restaurants
- **Cart Items**: Current items dalam shopping cart

### Search & Filter
- **Restaurant Search**: Search by name atau description
- **Order Filtering**: Filter by order status
- **Real-time Results**: Instant search results

### Notifications
- **Success Messages**: Order placed, profile updated
- **Error Handling**: API failures, validation errors
- **Loading States**: Visual feedback untuk async operations

## 🚀 Advanced Features

### 1. **Smart Cart Management**
- Automatic cart total calculation
- Delivery fee calculation
- Item quantity validation
- Cross-session cart persistence

### 2. **Order Tracking**
- Real-time status updates
- Estimated delivery time
- Order history dengan details
- Reorder functionality

### 3. **Restaurant Discovery**
- Rating system display
- Operating hours validation
- Delivery radius checking
- Menu categorization

## 🔄 Integration dengan Backend

### API Endpoints Used
```javascript
// Restaurants
GET /api/restaurants - List all restaurants
GET /api/restaurants/{id}/menu - Get restaurant menu

// Orders
POST /api/orders - Create new order
GET /api/orders - Get customer orders
PUT /api/orders/{id} - Update order (for tracking)

// Users
GET /api/users/{id} - Get customer profile
PUT /api/users/{id} - Update customer profile

// Authentication
POST /api/auth/login - Customer login
```

### Data Flow
1. **Login** → Store JWT token
2. **Browse** → Load restaurants dari API
3. **Order** → Send order data ke backend
4. **Track** → Poll order status updates
5. **Profile** → Sync customer data

## 🐛 Error Handling

### Common Scenarios
- **API Unavailable**: Fallback ke mock data
- **Network Errors**: User-friendly error messages
- **Invalid Input**: Form validation dengan feedback
- **Empty States**: Helpful messages untuk empty cart/orders

### Fallback Mechanisms
- Mock data untuk offline testing
- LocalStorage backup untuk cart
- Graceful degradation untuk missing features

## 🎨 Customization

### Color Scheme
```css
Primary: #3b82f6 (Blue)
Secondary: #6b7280 (Gray)
Success: #10b981 (Green)
Warning: #f59e0b (Orange)
Error: #ef4444 (Red)
Background: #f8fafc (Light Gray)
```

### Typography
- **Font Family**: Inter (Google Fonts)
- **Headings**: 600-700 weight
- **Body**: 400-500 weight
- **Small Text**: 300-400 weight

## 🚀 Future Enhancements

1. **Real-time Notifications** - WebSocket integration
2. **Advanced Filtering** - Cuisine type, price range, ratings
3. **Favorites System** - Save favorite restaurants dan items
4. **Order Scheduling** - Schedule orders untuk later
5. **Social Features** - Reviews, ratings, sharing
6. **Loyalty Program** - Points dan rewards system
7. **Multi-address Management** - Multiple delivery addresses
8. **Payment History** - Transaction history dan receipts

## 📞 Support & Troubleshooting

### Common Issues
1. **Login Failed**: Check demo credentials atau network
2. **Cart Not Saving**: Check localStorage permissions
3. **Orders Not Loading**: Verify backend API connection
4. **Responsive Issues**: Clear browser cache

### Debug Mode
Enable console logging untuk debugging:
```javascript
localStorage.setItem('debug', 'true');
```

## 📄 License

Projek ini adalah sebahagian daripada BITP3123 coursework untuk Universiti Teknikal Malaysia Melaka (UTeM).

---

**Dicipta untuk Customer Experience yang Terbaik - UTeM FTKK**
