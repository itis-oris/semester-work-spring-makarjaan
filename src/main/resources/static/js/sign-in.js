document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
            credentials: 'include'
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || 'Login failed');
        }

        sessionStorage.setItem('accessToken', data.accessToken);
        console.log('Login successful, access token stored');

        window.location.href = '/main';
    } catch (error) {
        console.error('Login error:', error);
        const errorElement = document.getElementById('error');
        errorElement.textContent = error.message;
        errorElement.classList.remove('d-none');
    }
});

