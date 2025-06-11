document.getElementById('registrationForm').addEventListener('submit', async function (e) {
    e.preventDefault();
    clearErrors();

    const form = e.target;
    const formData = new FormData(form);

    const user = {
        username: formData.get('username'),
        email: formData.get('email'),
        phone: formData.get('phone'),
        password: formData.get('password')
    };

    try {
        const response = await fetch('api/user/signUp', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        });

        const data = await response.json();

        if (!response.ok) {
            if (data.errors) {
                Object.keys(data.errors).forEach(field => {
                    const message = data.errors[field];
                    const element = document.getElementById(field);

                    if (element) {
                        const errorDiv = document.createElement('div');
                        errorDiv.className = 'invalid-feedback d-block';
                        errorDiv.textContent = message;

                        element.classList.add('is-invalid');
                        element.parentNode.appendChild(errorDiv);
                    }
                });
            } else {
                showError(data.message || 'Произошла ошибка при регистрации');
            }
            return;
        }

        window.location.href = '/signIn';

    } catch (error) {
        console.error('Ошибка:', error);
        showError('Не удалось отправить форму. Проверьте соединение.');
    }
});

function clearErrors() {
    document.querySelectorAll('.invalid-feedback').forEach(el => el.remove());
    document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
    const errorMessage = document.getElementById('error-message');
    if (errorMessage) errorMessage.classList.add('d-none');
}

function showError(message) {
    const errorMessage = document.getElementById('error-message');
    const errorText = document.getElementById('error-text');
    if (errorMessage && errorText) {
        errorText.textContent = message;
        errorMessage.classList.remove('d-none');
    }
}

document.getElementById('togglePassword').addEventListener('click', function() {
    const passwordInput = document.getElementById('password');
    const icon = this.querySelector('i');
    
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