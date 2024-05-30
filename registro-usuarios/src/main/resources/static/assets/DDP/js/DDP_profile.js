document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        fetchUserData(token);
    } else {
        redirectToLogin();
    }
});

function showOrHideNavItems(role) {
    const tableNavItem = document.getElementById('tableNavItem');
    const tableNavItem2 = document.getElementById('tableNavItem2');
    const alertmsgbutt = document.getElementById('alertmsg');
    const alertgenbutt = document.getElementById('alertgen');
    if (role === 'Director') {
        tableNavItem.style.display = 'block';
    } else if (role === 'Alumno') {
        tableNavItem.style.display = 'none';
    } else if (role === null) {
        alertmsgbutt.style.display = 'none';
        alertgenbutt.style.display = 'none';
        tableNavItem.style.display = 'none';
        tableNavItem2.style.display = 'none';
    }
}

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
                document.getElementById('userRole').textContent = data.roleName || 'Role';
                // Actualizando los datos de usuario
                document.getElementById('username').textContent = data.username || 'No disponible';
                document.getElementById('email').textContent = data.email || 'No disponible';
                document.getElementById('first_name').textContent = data.firstName || 'No disponible';
                document.getElementById('last_name').textContent = data.lastName || 'No disponible';
                // Actualizando info de contacto
                document.getElementById('city').textContent = data.city || 'No disponible';
                document.getElementById('centroe').textContent = data.centro || 'No disponible';
                document.getElementById('address').textContent = data.address || 'No disponible';
                // Imagen
                document.getElementById('profileImage').src = data.imageUrl || 'assets/img/default.png';

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
    location.reload(true);
}

document.getElementById('imageInput').addEventListener('change', function(event) {
    const file = event.target.files[0];
    if (file) {
        const formData = new FormData();
        formData.append('image', file);

        fetch(`/users/auth/update-image/${getUserIdFromToken(localStorage.getItem('jwtToken'))}`, {
            method: 'POST',
            body: formData,
            headers: {
                "Authorization": `Bearer ${localStorage.getItem('jwtToken')}`,
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.imageUrl) {
                location.reload(true);
                //alert('Imagen actualizada correctamente.');
            }
        })
        .catch((error) => {
        console.error("Error:", error);
        document.getElementById('error').textContent = error.message;
        document.getElementById('error').style.display= "block";
    });
    }
});