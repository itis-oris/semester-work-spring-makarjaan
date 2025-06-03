document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
            credentials: 'include'
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Login failed');
        }

        const { accessToken } = await response.json();
        sessionStorage.setItem('accessToken', accessToken);
        console.log('Login successful, access token stored');

        window.location.href = '/main';
    } catch (error) {
        console.error('Login error:', error);
        document.getElementById('error').textContent = error.message;
    }
});

// Улучшенный interceptor
async function authFetch(url, options = {}) {
    const accessToken = sessionStorage.getItem('accessToken');

    // Добавляем authorization header
    options.headers = {
        ...options.headers,
        'Authorization': `Bearer ${accessToken}`
    };

    // Всегда включаем credentials
    options.credentials = 'include';

    console.log('Making request to:', url);
    let response = await fetch(url, options);

    // Обработка 401 ошибки
    if (response.status === 401) {
        console.log('Access token expired, refreshing...');

        try {
            const refreshResponse = await fetch('http://localhost:8080/api/auth/token', {
                method: 'POST',
                credentials: 'include'
            });

            if (refreshResponse.ok) {
                const { accessToken: newToken } = await refreshResponse.json();
                sessionStorage.setItem('accessToken', newToken);
                console.log('New access token stored');

                // Обновляем заголовок и повторяем запрос
                options.headers.Authorization = `Bearer ${newToken}`;
                return fetch(url, options);
            } else {
                console.error('Refresh token failed');
                sessionStorage.removeItem('accessToken');
                window.location.href = '/signIn';
                return Promise.reject(new Error('Session expired'));
            }
        } catch (refreshError) {
            console.error('Token refresh error:', refreshError);
            window.location.href = '/login';
            return Promise.reject(refreshError);
        }
    }

    return response;
}