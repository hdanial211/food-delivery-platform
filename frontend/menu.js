document.addEventListener('DOMContentLoaded', () => {
    const menuList = document.getElementById('menu-list');
    const foods = [
        { name: "Nasi Lemak", price: "RM 3.00" },
        { name: "Roti Canai", price: "RM 2.00" },
        { name: "Mee Goreng", price: "RM 4.00" },
        { name: "Teh Tarik", price: "RM 1.50" },
        { name: "Kopi O", price: "RM 1.00" },
        { name: "Rendang Ayam", price: "RM 5.00" },
        { name: "Sate", price: "RM 6.00" },
        { name: "Laksa", price: "RM 4.50" },
        { name: "Cendol", price: "RM 2.50" },
        { name: "Rojak", price: "RM 3.50" },
        { name: "Char Kway Teow", price: "RM 5.50" },
        { name: "Nasi Goreng Kampung", price: "RM 4.00" },
        { name: "Ayam Penyet", price: "RM 7.00" },
        { name: "Bubur Ayam", price: "RM 3.50" },
        { name: "Mee Rebus", price: "RM 4.00" },
        { name: "Nasi Kandar", price: "RM 6.00" },
        { name: "Kway Teow Soup", price: "RM 4.50" },
        { name: "Popiah", price: "RM 2.50" }
    ];
    foods.forEach(food => {
        const div = document.createElement('div');
        div.className = 'menu-item';
        div.innerHTML = `<strong>${food.name}</strong> - <span>${food.price}</span>`;
        menuList.appendChild(div);
    });
});