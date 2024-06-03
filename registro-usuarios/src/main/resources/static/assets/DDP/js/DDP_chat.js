document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        fetchUserData(token);
    } else {
        redirectToLogin();
    }

    let activeContact = null;
    const chatContent = document.getElementById('messagesContainer');
    const chatInput = document.getElementById('chat-input');
    const chatSendBtn = document.getElementById('chat-send-btn');
    const contactsList = document.getElementById('contactsList');
    const searchInput = document.getElementById('searchInput');
    let privateSocket = null;
    let currentUser = '';
    let allContacts = []; // Variable para almacenar todos los contactos

    searchInput.addEventListener('input', function() {
        filterContacts(searchInput.value);
    });

    chatInput.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            sendMessage();
        }
    });

    chatSendBtn.onclick = function() {
        sendMessage();
    };

    function sendMessage() {
        const message = chatInput.value.trim();
        if (message) {
            const messageData = {
                fromUser: currentUser,
                content: message,
                timestamp: new Date().toISOString()
            };
            privateSocket.send(JSON.stringify(messageData));
            chatInput.value = '';
            scrollToBottom(); // Asegurar que el scroll se realiza después del renderizado
        }
    }

    function filterContacts(query) {
        const filteredContacts = allContacts.filter(contact => 
            contact.name.toLowerCase().includes(query.toLowerCase())
        );
        renderContacts(filteredContacts);
    }

    function setActiveContact(contactElement) {
        if (activeContact) {
            activeContact.classList.remove('active');
        }
        activeContact = contactElement;
        activeContact.classList.add('active');
    }

    function addContactToList(contact) {
        allContacts.push(contact); // Añadir el contacto a la lista de todos los contactos
        renderContacts(allContacts); // Renderizar todos los contactos inicialmente
    }

    function renderContacts(contacts) {
        contactsList.innerHTML = '';
        contacts.forEach(contact => {
            const contactItem = document.createElement('div');
            contactItem.classList.add('contact-item');
            
            const imgElement = document.createElement('img');
            imgElement.src = contact.imageUrl;
            imgElement.alt = 'Contact Image';
            
            const spanElement = document.createElement('span');
            spanElement.textContent = contact.name;
            
            contactItem.appendChild(imgElement);
            contactItem.appendChild(spanElement);

            contactItem.addEventListener('click', () => {
                setActiveContact(contactItem);
                startPrivateChat(contact.username);
            });
            
            contactsList.appendChild(contactItem);
        });
    }

    function startPrivateChat(username) {
        if (privateSocket) {
            privateSocket.close();
        }

        chatContent.innerHTML = ''; // Limpiar el contenedor de mensajes

        privateSocket = new WebSocket(`ws://snakernet.net:8080/ws/privateappchat?fromUser=${currentUser}&toUser=${username}`);
        
        privateSocket.onmessage = function(event) {
            let messageData;
            try {
                messageData = JSON.parse(event.data);
                console.info(event.data);
            } catch (e) {
                console.error("Error al parsear el mensaje:", e);
                return;
            }

            if (messageData.error) {
                console.error("Error del servidor:", messageData.error);
                return;
            }

            const fromUser = messageData.fromUser;
            const content = messageData.content;
            const time = new Date(messageData.timestamp).toLocaleTimeString();

            addMessageToChat(content, fromUser, time, fromUser === currentUser);
        };
    }

    function fetchUserData(token) {
        const userId = getUserIdFromToken(token);
        if (userId) {
            fetch(`/users/auth/${userId}`, {
                method: 'GET',
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            })
            .then(response => response.json())
            .then(data => {
                currentUser = data.username; // Guardar el nombre de usuario actual
                document.getElementById('userName').textContent = data.firstName + ' ' + data.lastName;
                document.getElementById('userImage').src = data.imageUrl || 'assets/img/default.png';
                document.getElementById('userRole').textContent = data.roleName || 'Role';
                
                showOrHideNavItems(data.roleName);
                fetchContacts();
            });
        } else {
            redirectToLogin();
        }
    }

    function fetchContacts() {
        fetch('/users/contact/list')
            .then(response => response.json())
            .then(users => {
                console.log(users)
                contactsList.innerHTML = ''; // Limpiar la lista antes de agregar nuevos contactos
                allContacts = users
                    .filter(user => user.username !== currentUser) // Filtrar el usuario actual
                    .map(user => ({
                        name: user.firstName + ' ' + user.lastName,
                        imageUrl: user.imageUrl || 'assets/img/default.png',
                        username: user.username
                    }));
                renderContacts(allContacts);
            });
    }

    function addMessageToChat(message, user, time, isUser = true) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', isUser ? 'user' : 'other');

        const timeElement = document.createElement('div');
        timeElement.textContent = time;
        timeElement.classList.add('small', 'time');

        const userElement = document.createElement('div');
        userElement.textContent = user;
        userElement.classList.add('fw-bold');

        const textElement = document.createElement('div');
        textElement.textContent = message;

        messageElement.appendChild(userElement);
        messageElement.appendChild(textElement);
        messageElement.appendChild(timeElement);

        chatContent.appendChild(messageElement);

        // Asegurarse de que el contenedor de mensajes se desplace hacia abajo
        scrollToBottom(); // Asegurar que el scroll se realiza después del renderizado
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
            return null;
        }
    }

    function scrollToBottom() {
        chatContent.scrollTop = chatContent.scrollHeight;
    }

    // Inicializar con un scroll al final para asegurar que empiece desde el último mensaje
    scrollToBottom();
});

function logout() {
    localStorage.removeItem('jwtToken');
    redirectToLogin();
}

function redirectToLogin() {
    window.location.href = '/login.html';
}

function showOrHideNavItems(role) {
    const tableNavItem = document.getElementById('tableNavItem');
    const tableNavItem2 = document.getElementById('tableNavItem2');
    if (role === 'Director') {
        tableNavItem.style.display = 'block';
    } else if (role === 'Alumno') {
        tableNavItem.style.display = 'none';
    } else if (role === null) {
        tableNavItem.style.display = 'none';
        tableNavItem2.style.display = 'none';
    }
}