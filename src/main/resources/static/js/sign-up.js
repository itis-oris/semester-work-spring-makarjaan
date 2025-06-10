document.getElementById('registrationForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const form = e.target;
    const formData = new FormData(form);

    const user = {
        username: formData.get('username'),
        email: formData.get('email'),
        phone: formData.get('phone'),
        password: formData.get('password')
    };

    try {
        const response = await fetch('/signUp', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        });

        const data = await response.json();

        if (!response.ok) {
            clearErrors();

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
            } else if (data.message) {
                const errorMessage = document.getElementById('error-message');
                const errorText = document.getElementById('error-text');
                errorText.textContent = data.message;
                errorMessage.classList.remove('d-none');
            }

            return;
        }

        window.location.href = '/signIn';

    } catch (error) {
        console.error('Ошибка:', error);
        const errorMessage = document.getElementById('error-message');
        const errorText = document.getElementById('error-text');
        errorText.textContent = 'Не удалось отправить форму. Проверьте соединение.';
        errorMessage.classList.remove('d-none');
    }
});

function clearErrors() {
    document.querySelectorAll('.invalid-feedback').forEach(el => el.remove());
    document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
    const errorMessage = document.getElementById('error-message');
    if (errorMessage) errorMessage.classList.add('d-none');
}