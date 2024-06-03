
var usertag = "";
document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        fetchUserData(token);
    } else {
         redirectToLogin();
    }

    // Cargar datos de transporte al abrir el contenido
    fetchTransportData(5085, 'transport-info-5085', 'stopName-5085');
    fetchTransportData(4834, 'transport-info-4834', 'stopName-4834');
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
                    var element = document.getElementById('userName');
                    if (element && element.tagName === 'A') {
                        element.href = "#";
                    }
                    element.textContent = data.firstName + ' ' + data.lastName;
                    document.getElementById('userImage').src = data.imageUrl || 'assets/img/default.png';
                    document.getElementById('userRole').textContent = data.roleName || 'Role';
                    showOrHideNavItems(data.roleName);
                    usertag = data.username;
                    console.error(usertag);
                    initializeWebSocket(usertag);
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

function logout() {
    localStorage.removeItem('jwtToken');
    redirectToLogin();
}

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

// Código para manejar las solicitudes a la API y mostrar la información del transporte
document.addEventListener('DOMContentLoaded', function () {
    const authUrl = '/users/auth/emt';

    // Obtener las credenciales desde tu API
    fetch(authUrl)
        .then(response => response.json())
        .then(authData => {
            const email = authData.email;
            const password = authData.password;

            // URL de login de EMT Madrid
            const loginUrl = 'https://openapi.emtmadrid.es/v2/mobilitylabs/user/login/';

            // Login y obtener el accessToken
            fetch(loginUrl, {
                    method: 'GET',
                    headers: {
                        'email': email,
                        'password': password
                    }
                })
                .then(response => response.json())
                .then(data => {
                    const accessToken = data.data[0].accessToken;
                    console.log('AccessToken:', accessToken); // Para verificar que obtuviste el token

                    // Guardar el token en localStorage
                    localStorage.setItem('emtAccessToken', accessToken);

                    // Obtener los datos del transporte para ambas paradas
                    fetchTransportData(5085, 'transport-info-5085', 'stopName-5085');
                    fetchTransportData(4834, 'transport-info-4834', 'stopName-4834');

                    // Realizar solicitudes de incidencias para las líneas
                    fetchIncidentsData(123);
                    fetchIncidentsData(85);
                    fetchIncidentsData(130);
                })
                .catch(error => console.error('Error during login:', error));
        })
        .catch(error => console.error('Error fetching auth data:', error));
});

function redirectToLogin() {
    window.location.href = '/login.html';
}

function fetchTransportData(stopId, elementId, stopNameElementId) {
    const accessToken = localStorage.getItem('emtAccessToken');

    if (!accessToken) {
        console.error('No access token found');
        return;
    }

    // URL para obtener la información del transporte
    const transportUrl = `https://openapi.emtmadrid.es/v2/transport/busemtmad/stops/${stopId}/arrives/`;

    // JSON para el body de la solicitud POST
    const bodyData = {
        "cultureInfo": "ES",
        "Text_StopRequired_YN": "Y",
        "Text_EstimationsRequired_YN": "Y",
        "Text_IncidencesRequired_YN": "N"
    };

    // Hacer la solicitud POST con el accessToken
    fetch(transportUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'accessToken': accessToken
            },
            body: JSON.stringify(bodyData)
        })
        .then(response => response.json())
        .then(data => {
            console.log('Transport Data:', data); // Para verificar los datos recibidos

            // Mostrar el nombre de la parada y añadir el enlace a Google Maps
            const stopInfo = data.data[0].StopInfo[0];
            const stopNameElement = document.getElementById(stopNameElementId);
            stopNameElement.innerHTML = `${stopInfo.stopName} <a href="https://www.google.com/maps?q=${stopInfo.geometry.coordinates[1]},${stopInfo.geometry.coordinates[0]}" target="_blank"><i class="fas fa-map-marker-alt location-icon"></i></a>`;

            // Procesar los datos y mostrarlos en el HTML
            const transportInfoDiv = document.getElementById(elementId);
            transportInfoDiv.innerHTML = ''; // Limpiar contenido anterior

            const arrivalsByLine = {};
            const lineInfo = {}; // Nuevo objeto para almacenar la información de las líneas

            // Agrupar las llegadas por línea y almacenar la información de las líneas
            data.data[0].Arrive.forEach(arrival => {
                const line = arrival.line;
                const seconds = arrival.estimateArrive;
                const minutes = Math.floor(seconds / 60);

                if (minutes <= 200) {
                    if (!arrivalsByLine[line]) {
                        arrivalsByLine[line] = [];
                    }
                    arrivalsByLine[line].push(seconds);

                    // Almacenar la información de la línea (nameA y nameB)
                    const lineInfoData = data.data[0].StopInfo[0].lines.find(l => l.label === line);
                    if (lineInfoData) {
                        lineInfo[line] = lineInfoData;
                    }
                }
            });

            // Mostrar las llegadas agrupadas por línea
            for (const [line, times] of Object.entries(arrivalsByLine)) {
                const infoElement = document.createElement('p');
                const lineDetails = lineInfo[line];
                if (lineDetails) {
                    infoElement.textContent = `${line} (${lineDetails.nameA} - ${lineDetails.nameB}) .. `;
                } else {
                    infoElement.textContent = `${line} .. `;
                }
                times.forEach((time, index) => {
                    const minutes = Math.floor(time / 60);
                    const timeSpan = document.createElement('span');
                    if (minutes <= 1) {
                        timeSpan.textContent = `AHORA`;
                        timeSpan.classList.add('highlight-red');
                    } else {
                        timeSpan.textContent = `${minutes} minutos`;
                        if (minutes < 5) {
                            timeSpan.classList.add('highlight-red');
                        } else if (minutes < 10) {
                            timeSpan.classList.add('highlight-orange');
                        } else {
                            timeSpan.classList.add('highlight-green');
                        }
                    }
                    infoElement.appendChild(timeSpan);
                    if (index < times.length - 1) {
                        infoElement.appendChild(document.createTextNode(', '));
                    }
                });
                transportInfoDiv.appendChild(infoElement);
            }
        })
        .catch(error => console.error('Error fetching transport data:', error));
}

function fetchIncidentsData(lineId) {
    const accessToken = localStorage.getItem('emtAccessToken');

    if (!accessToken) {
        console.error('No access token found');
        return;
    }

    // URL para obtener la información de las incidencias
    const incidentsUrl = `https://openapi.emtmadrid.es/v1/transport/busemtmad/lines/incidents/${lineId}/`;

    // Hacer la solicitud GET con el accessToken
    fetch(incidentsUrl, {
            method: 'GET',
            headers: {
                'accessToken': accessToken
            }
        })
        .then(response => response.json())
        .then(data => {
            console.log('Incidents Data:', data); // Para verificar los datos recibidos

            // Mostrar los datos recibidos en el div "center-events"
            const eventsDiv = document.getElementById('incidentsAccordion');
            data.data[0].item.forEach((item, index) => {
                const incidentElement = document.createElement('div');
                incidentElement.className = 'accordion-item mb-3';
                incidentElement.innerHTML = `
                    <h2 class="accordion-header" id="heading${lineId}-${index}">
                        <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${lineId}-${index}" aria-expanded="false" aria-controls="collapse${lineId}-${index}">
                            ${item.title}
                        </button>
                    </h2>
                    <div id="collapse${lineId}-${index}" class="accordion-collapse collapse" aria-labelledby="heading${lineId}-${index}" data-bs-parent="#incidentsAccordion">
                        <div class="accordion-body">
                            <p>${item.description}</p>
                            <p><strong>Fecha de publicación:</strong> ${item.pubDate}</p>
                            <p><strong>Duración:</strong> ${item.rssAfectaDesde} - ${item.rssAfectaHasta}</p>
                        </div>
                    </div>
                `;
                eventsDiv.appendChild(incidentElement);
            });
        })
        .catch(error => console.error('Error fetching incidents data:', error));
}

function initializeWebSocket(username) {
    const chatContent = document.getElementById('chat-content');
    const chatInput = document.getElementById('chat-input');
    const chatSendBtn = document.getElementById('chat-send-btn');
    const shownMessages = new Set(); // Conjunto para almacenar los mensajes mostrados

    // WebSocket connection
    let socket = new WebSocket("ws://snakernet.net:8080/ws/publicappchat");

    socket.onmessage = function(event) {
        // Assuming the server sends messages in the format: username: message [time]
        let messageData = event.data.split(': ');
        let user = messageData[0];
        let messageAndTime = messageData[1].split(' [');
        let message = messageAndTime[0];
        let time = messageAndTime[1].slice(0, -1); // Remove the closing bracket

        let fullMessage = user + ": " + message + " [" + time + "]";

        // Solo agregar y mostrar el mensaje si no se ha mostrado antes
        if (!shownMessages.has(fullMessage)) {
            shownMessages.add(fullMessage);
            addMessageToChat(message, user, time, user === username);
        }
    }

    chatSendBtn.addEventListener('click', sendMessage);
    chatInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });

    function sendMessage() {
        let user = username;
        let message = chatInput.value;

        if (message.trim() !== "") {
            let time = new Date().toLocaleTimeString();
            let fullMessage = user + ": " + message + " [" + time + "]";
            socket.send(fullMessage);
            chatInput.value = '';

            // Agregar y mostrar el mensaje inmediatamente
            shownMessages.add(fullMessage);
            addMessageToChat(message, user, time, true);
        }
    }

    // Function to add a message to the chat
    function addMessageToChat(message, user, time, isUser) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('p-2', 'my-2', 'rounded', isUser ? 'bg-primary' : 'bg-light', isUser ? 'text-white' : 'text-dark');
        messageElement.classList.add(isUser ? 'message-right' : 'message-left');
        messageElement.style.maxWidth = '70%';

        const timeElement = document.createElement('div');
        timeElement.textContent = time;
        timeElement.classList.add('small', 'text-muted');

        const userElement = document.createElement('div');
        userElement.textContent = user;
        userElement.classList.add('fw-bold');

        const textElement = document.createElement('div');
        textElement.textContent = message;

        messageElement.appendChild(userElement);
        messageElement.appendChild(textElement);
        messageElement.appendChild(timeElement);

        chatContent.appendChild(messageElement);
        chatContent.scrollTop = chatContent.scrollHeight; // Scroll down to the latest message
    }
}

// Manejar el envío de mensajes
document.addEventListener('DOMContentLoaded', function () {
    const chatSendBtn = document.getElementById('chat-send-btn');
    const chatInput = document.getElementById('chat-input');

    chatSendBtn.addEventListener('click', function () {
        const message = chatInput.value.trim();
        if (message) {
            const username = 'Tú'; // Aquí deberías obtener el nombre de usuario real
            addMessageToChat(message, username, new Date().toLocaleTimeString(), true);
            chatInput.value = '';
            // Aquí puedes agregar la lógica para enviar el mensaje al servidor si es necesario
        }
    });

    // Permitir enviar el mensaje al presionar Enter
    chatInput.addEventListener('keypress', function (event) {
        if (event.key === 'Enter') {
            chatSendBtn.click();
        }
    });
});