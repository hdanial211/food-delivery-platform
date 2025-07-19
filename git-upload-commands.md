# Git Commands untuk Upload ke GitHub

## Langkah-langkah untuk upload restaurant frontend ke GitHub repo anda:

### 1. Clone repo dan switch ke branch yang betul
```bash
git clone https://github.com/MuhdAminRahman/BITP3123-Project-FoodDeliveryPlatform.git
cd BITP3123-Project-FoodDeliveryPlatform
git checkout frontedhakim
```

### 2. Copy semua file restaurant yang telah dibuat
```bash
# Copy folder restaurant
cp -r ../restaurant ./

# Copy file-file setup
cp ../setup-database.sh ./
cp ../init-demo-data.sql ./
cp ../run-servers.sh ./
cp ../INTEGRATION_GUIDE.md ./

# Copy config file yang telah dikemaskini
cp ../myproject/config.json ./myproject/
```

### 3. Add semua file baru
```bash
git add .
```

### 4. Commit dengan message yang sesuai
```bash
git commit -m "Add restaurant frontend dashboard with backend integration

- Complete restaurant dashboard with login, menu management, order management
- Backend integration with Java API endpoints
- Demo mode for testing without database
- Setup scripts for MySQL database
- Integration guide and documentation
- Responsive design with modern UI/UX"
```

### 5. Push ke GitHub
```bash
git push origin frontedhakim
```

## File-file yang akan diupload:

### Restaurant Frontend:
- `restaurant/index.html` - Main dashboard
- `restaurant/login.html` - Login page
- `restaurant/styles.css` - Styling
- `restaurant/script.js` - JavaScript functionality
- `restaurant/simple-demo.html` - Demo mode tanpa backend
- `restaurant/README.md` - Documentation

### Setup & Integration:
- `setup-database.sh` - MySQL setup script
- `init-demo-data.sql` - Demo data untuk testing
- `run-servers.sh` - One-command startup
- `INTEGRATION_GUIDE.md` - Complete integration guide
- `myproject/config.json` - Backend configuration

## Jika ada masalah:

### Authentication error:
```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

### Permission denied:
- Pastikan anda mempunyai akses push ke repo
- Gunakan personal access token jika diperlukan

### Branch tidak wujud:
```bash
git checkout -b frontedhakim
```

Selepas upload berjaya, anda boleh akses restaurant dashboard di:
`https://github.com/MuhdAminRahman/BITP3123-Project-FoodDeliveryPlatform/tree/frontedhakim/restaurant`
