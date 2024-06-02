let allUsers = [];
let filteredUsers = [];
const pageSize = 7; // Número de usuarios por página
let currentPage = 1;

document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        fetchUserData(token);
        fetchRoles(token).then(() => {
            fetchUsers(token);
        });
    } else {
        redirectToLogin();
    }
});

async function fetchRoles(token) {
    try {
        const response = await fetch('/users/info/roles', {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });
        if (!response.ok) {
            throw new Error(`Error fetching roles: ${response.status} ${response.statusText}`);
        }
        const roles = await response.json();
        window.roles = roles;
    } catch (error) {
        console.error('Error loading roles:', error);
    }
}

async function fetchUserData(token) {
    const userId = getUserIdFromToken(token);
    if (userId) {
        try {
            const response = await fetch(`/users/auth/${userId}`, {
                method: 'GET',
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            });
            if (response.ok) {
                const data = await response.json();
                document.getElementById('userName').textContent = data.firstName + ' ' + data.lastName;
                document.getElementById('userImage').src = data.imageUrl || 'assets/img/default.png';
                document.getElementById('userRole').textContent = data.roleName || 'Role';
            } else if (response.status === 401) {
                redirectToLogin();
            } else {
                throw new Error(`Failed to fetch user data: ${response.status} ${response.statusText}`);
            }
        } catch (error) {
            console.error('Error fetching user data:', error);
            document.getElementById('userName').textContent = "Error loading data";
            redirectToLogin();
        }
    } else {
        console.error('No user ID found in token');
        document.getElementById('userName').textContent = "Error loading data";
    }
}

async function fetchUsers(token) {
    try {
        const response = await fetch('/users/table/list?page=0&pageSize=1000', {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });
        if (response.ok) {
            const users = await response.json();
            console.log(users); // Verifica que los usuarios tienen un id
            allUsers = users;
            filteredUsers = users; // Inicialmente, sin filtro
            updatePagination();
            updateTable();
            updateTableInfo();
        } else if (response.status === 401) {
            redirectToLogin();
        } else {
            throw new Error(`Failed to fetch user data: ${response.status} ${response.statusText}`);
        }
    } catch (error) {
        console.error('Error fetching user data:', error);
    }
}


function handleSearch() {
    const searchInput = document.getElementById('searchInput').value.toLowerCase();
    filteredUsers = allUsers.filter(user => {
        return (user.firstName || "").toLowerCase().includes(searchInput) ||
                (user.lastName || "").toLowerCase().includes(searchInput) ||
                (user.firstName + " " + user.lastName || "").toLowerCase().includes(searchInput) ||
                (user.username || "").toLowerCase().includes(searchInput) ||
                (user.centro || "").toLowerCase().includes(searchInput) ||
                (user.email || "").toLowerCase().includes(searchInput) ||
                (user.roleName || "").toLowerCase().includes(searchInput) ||
                (user.curso || "").toLowerCase().includes(searchInput);
    });
    currentPage = 1; // Reiniciar a la primera página en cada búsqueda
    updatePagination();
    updateTable();
    updateTableInfo();
}

function updateTable() {
    const tableBody = document.getElementById('dataTable').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = ''; // Limpiar las filas existentes

    const start = (currentPage - 1) * pageSize;
    const end = start + pageSize;
    const usersToShow = filteredUsers.slice(start, end);

    usersToShow.forEach(user => {
        console.log('User data:', user); // Verifica que el usuario tiene un id

        let row = tableBody.insertRow();

        let cell1 = row.insertCell(0);
        let imageUrl = user.imageUrl || 'assets/img/default.png';
        cell1.innerHTML = `<img class="rounded-circle me-2" height="30" src="${imageUrl}" width="30">${user.firstName} ${user.lastName}`;

        let cell2 = row.insertCell(1);
        cell2.textContent = user.username || 'No disponible';

        let cell3 = row.insertCell(2);
        cell3.textContent = user.centro || 'No disponible';

        let cell4 = row.insertCell(3);
        cell4.textContent = user.email || 'No disponible';

        let cell5 = row.insertCell(4);
        cell5.innerHTML = renderRoleSelect(user.roleName, user.id); // Aquí deberíamos tener user.id

        let cell6 = row.insertCell(5);
        cell6.textContent = user.curso || 'No disponible';
    });
}

function renderRoleSelect(currentRoleName, userId) {
    if (!userId) {
        console.error('Invalid userId:', userId);
        return '<select disabled><option value="">Invalid User</option></select>';
    }

    let options = window.roles.map(role => {
        return `<option value="${role.nombre}" ${role.nombre === currentRoleName ? 'selected' : ''}>${role.nombre}</option>`;
    }).join('');

    return `<select onchange="handleRoleChange(event, ${userId})">${options}</select>`;
}

async function handleRoleChange(event, userId) {
    const newRoleName = event.target.value;
    const token = localStorage.getItem('jwtToken');

    if (!userId || userId === 'undefined') {
        console.error('Invalid userId:', userId);
        return;
    }

    try {
        const response = await fetch(`/users/role/sw?userId=${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ roleName: newRoleName })
        });

        if (response.ok) {
            console.log('Role updated successfully');
        } else {
            console.error('Error updating role:', response.statusText);
        }
    } catch (error) {
        console.error('Error updating role:', error);
    }
}

function updatePagination() {
    const paginationElement = document.getElementById('pagination');
    paginationElement.innerHTML = ''; // Limpiar la paginación existente

    const pageCount = Math.ceil(filteredUsers.length / pageSize);

    if (pageCount <= 1) {
        return; // No mostrar paginación si no hay más de una página
    }

    for (let i = 1; i <= pageCount; i++) {
        const pageItem = document.createElement('li');
        pageItem.classList.add('page-item');
        if (i === currentPage) {
            pageItem.classList.add('active');
        }
        const pageLink = document.createElement('a');
        pageLink.classList.add('page-link');
        pageLink.href = '#';
        pageLink.textContent = i;
        pageLink.onclick = function () {
            currentPage = i;
            updateTable();
            updateTableInfo();
        };
        pageItem.appendChild(pageLink);
        paginationElement.appendChild(pageItem);
    }
}

function updateTableInfo() {
    const infoElement = document.getElementById('dataTable_info');
    if (infoElement) {
        const startIndex = (currentPage - 1) * pageSize + 1;
        const endIndex = Math.min(startIndex + pageSize - 1, filteredUsers.length);
        infoElement.textContent = `Mostrando ${startIndex} a ${endIndex} de ${filteredUsers.length}`;
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
        return decodedToken.sub;
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
