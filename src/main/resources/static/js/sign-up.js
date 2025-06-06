document.getElementById('registrationForm')
    .addEventListener('submit', async (e) => {
        e.preventDefault();

        const form = e.target;
        const formData = new FormData(form);

        const user = {
            username: formData.get('username'),
            email: formData.get('email'),
            phone: formData.get('phone'),
            password: formData.get('password'),
        };

        try {
            const response = await fetch('/signUp', {
                method: 'POST',
            headers: {
            'Content-Type': 'application/json',
        },
            body: JSON.stringify(user),
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || 'Ошибка при регистрации');
        }
            window.location.href = '/signIn';

        } catch (error) {
            console.error('Registration error:', error.message);
            const errorMessage = document.getElementById('error-message');
            const errorText = document.getElementById('error-text');
            errorText.textContent = error.message;
            errorMessage.classList.remove('d-none');
        }
});
