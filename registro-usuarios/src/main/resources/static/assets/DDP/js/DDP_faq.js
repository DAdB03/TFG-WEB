document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        fetchUserData(token);
    } else {
        redirectToLogin();
    }
});

function fetchUserData(token) {
    const userId = getUserIdFromToken(token);
    console.log("UserID from token:", userId); // Debug: Verifica que el ID se extrae correctamente
    if (userId) {
        fetch(`/users/auth/${userId}`, {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                console.log("Server response:", response); // Debug: Muestra la respuesta del servidor
                if (response.ok) {
                    return response.json();
                } else if (response.status === 401) {
                    redirectToLogin(); // Redirigir al inicio de sesión si el token expiró
                } else {
                    throw new Error(`Failed to fetch user data: ${response.status} ${response.statusText}`);
                }
            })
            .then(data => {
                console.log("User data received:", data); // Debug: Muestra los datos recibidos
                if (data) {
                    document.getElementById('userName').textContent = data.firstName + ' ' + data.lastName;
                    document.getElementById('userImage').src = data.imageUrl || 'assets/img/default.png';
                    document.getElementById('userRole').textContent = data.roleName;      
                    showOrHideNavItems(data.roleName);
                }

            })
            .catch(error => {
                console.error('Error fetching user data:', error);
                document.getElementById('userName').textContent = "Error loading data";
                redirectToLogin();
            });
    } else {
        console.error('No user ID found in token');
        document.getElementById('userName').textContent = "Error loading data"; // Mostrar mensaje de error en la UI
    }
}

function getUserIdFromToken(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        const decodedToken = JSON.parse(jsonPayload);
        return decodedToken.sub; // Assuming 'sub' contains user ID
    } catch (e) {
        console.error('Error decoding token:', e);
        return null;
    }
}

function redirectToLogin() {
    window.location.href = '/login.html';
}

function logout() {
    localStorage.removeItem('jwtToken');
    redirectToLogin();
}

function showOrHideNavItems(role) {
    const tableNavItem = document.getElementById('tableNavItem');
    const tableNavItem2 = document.getElementById('tableNavItem2');
    const linkref = document.getElementById('link-ref');
    
    if (role === 'Director') {
        tableNavItem.style.display = 'block';
        linkref.setAttribute('href', 'table.html'); 
    } else if (role === 'Profesor') {
        tableNavItem.style.display = 'block';
        linkref.setAttribute('href', 'table2.html');
    } else if (role === 'Alumno') {
        tableNavItem.style.display = 'none';
    } else if (role === null) {
        tableNavItem.style.display = 'none';
        tableNavItem2.style.display = 'none';
    } else {
        tableNavItem.style.display = 'none';
        tableNavItem2.style.display = 'none';
    }
}