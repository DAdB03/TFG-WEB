function checkPasswords(event) {
    event.preventDefault();

    var password = document.getElementById("password_input").value;
    var passwordConfirm = document.getElementById("passwordConfirm").value;

    if (password !== passwordConfirm) {
      document.getElementById("error").textContent =
        "Las contraseñas no coinciden. Por favor, verifica e intenta de nuevo.";
      document.getElementById("error").style.display = "block";
      return;
    }

    // Si las contraseñas coinciden, llamar a la función register
    register();
  }

  function register() {
    var formData = {
      firstName: document.getElementById("first_name").value,
      lastName: document.getElementById("last_name").value,
      email: document.getElementById("email_input").value,
      password: document.getElementById("password_input").value,
    };

    fetch("/users/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then((response) => {
        if (response.ok) {
          window.location.href = "/login.html"; // Redirige a la página de login si el registro es exitoso
        } else {
          // Procesa la respuesta para mostrar el mensaje de error
          return response.text().then((text) => {
            throw new Error(text);
          });
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        document.getElementById("error").textContent = error.message;
        document.getElementById("error").style.display = "block";
      });
  }

  // Agrega el listener al botón de registro para manejar el click
  document.getElementById("registerButton").addEventListener("click", register);