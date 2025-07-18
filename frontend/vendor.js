document.addEventListener('DOMContentLoaded', () => {
    const orderReceived = document.getElementById('order-received');
    const orderDetails = JSON.parse(localStorage.getItem('orderDetails'));
    if (orderDetails) {
        orderReceived.innerHTML = `
            <h2>Order Received</h2>
            <p>Thank you for your order!</p>
            <p><strong>Order ID:</strong> ${orderDetails.orderId}</p>
            <p><strong>Total Amount:</strong> RM ${orderDetails.totalAmount}</p>
            <p><strong>Items:</strong></p>
            <ul>
                ${orderDetails.items.map(item => `<li>${item.name} - RM ${item.price}</li>`).join('')}
            </ul>
        `;
        }
    });