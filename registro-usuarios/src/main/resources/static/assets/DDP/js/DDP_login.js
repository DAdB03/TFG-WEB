document.addEventListener("DOMContentLoaded", function() {
    // Load credentials from localStorage if available
    const rememberedEmail = localStorage.getItem("rememberedEmail");
    const rememberedPassword = localStorage.getItem("rememberedPassword");

    if (rememberedEmail && rememberedPassword) {
        document.getElementById("exampleInputEmail").value = rememberedEmail;
        document.getElementById("exampleInputPassword").value = rememberedPassword;
        document.getElementById("formCheck-1").checked = true;
    }
});

function login(event) {
    event.preventDefault();
    var email = document.getElementById("exampleInputEmail").value;
    var password = document.getElementById("exampleInputPassword").value;
    var rememberMe = document.getElementById("formCheck-1").checked;

    fetch("/users/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({email: email, password: password}),
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.text().then(text => {
                throw new Error(text);
            });
        }
    })
    .then(data => {
        if (data && data.jwtToken) {
            localStorage.setItem('jwtToken', data.jwtToken);

            if (rememberMe) {
                localStorage.setItem("rememberedEmail", email);
                localStorage.setItem("rememberedPassword", password);
            } else {
                localStorage.removeItem("rememberedEmail");
                localStorage.removeItem("rememberedPassword");
            }

            window.location.href = "/index.html";
        } else {
            alert("No se pudo iniciar sesión correctamente. Por favor, intente de nuevo.");
        }
    })
    .catch((error) => {
        console.error("Error:", error);
        document.getElementById('error').textContent = error.message;
        document.getElementById('error').style.display= "block";
    });
}

document.getElementById('togglePassword').addEventListener('click', function (e) {
    const passwordInput = document.getElementById('exampleInputPassword');
    const icon = e.currentTarget.querySelector('i');
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        icon.classList.remove('bi-eye');
        icon.classList.add('bi-eye-slash');
    } else {
        passwordInput.type = 'password';
        icon.classList.remove('bi-eye-slash');
        icon.classList.add('bi-eye');
    }
});