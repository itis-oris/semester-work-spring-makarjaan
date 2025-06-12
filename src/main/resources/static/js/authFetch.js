async function authFetch(url, options = {}) {
    const token = sessionStorage.getItem("accessToken");
    console.log('Token: (из консоля): ', token);

    options.headers = options.headers || {};

    if (token) {
        options.headers.Authorization = `Bearer ${token}`;
    }

    options.credentials = 'include';

    console.log('Making request to:', url);

    let response;
    try {
        response = await fetch(url, options);
        console.log('Response status:', response.status);

        if (response.ok) {
            return response;
        }

        if (response.status === 401) {
            console.log('Access token expired, refreshing...');

            const refreshResponse = await fetch('/api/auth/token', {
                method: 'POST',
                credentials: 'include'
            });

            if (refreshResponse.ok) {
                const { accessToken: newToken } = await refreshResponse.json();
                sessionStorage.setItem('accessToken', newToken);
                console.log('New access token stored');

                // Обновляем заголовок с новым токеном
                options.headers.Authorization = `Bearer ${newToken}`;

                // Повторяем исходный запрос
                return fetch(url, options);
            } else {
                console.error('Refresh token failed');
                sessionStorage.removeItem('accessToken');
                throw new Error('Session expired and could not refresh token');
            }
        }

        return response;

    } catch (error) {
        console.error('Fetch error:', error);
        throw error;
    }
}