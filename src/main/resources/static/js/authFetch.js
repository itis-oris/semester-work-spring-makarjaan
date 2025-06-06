async function authFetch(url, options = {}) {
    const token = sessionStorage.getItem("accessToken");
    console.log('Token: (из консоля): ', token);

    // Убедиться, что headers определены
    options.headers = options.headers || {};

    // Добавить Authorization, если есть токен
    if (token) {
        options.headers.Authorization = `Bearer ${token}`;
    }

    // Всегда отправляйте credentials
    options.credentials = 'include';

    console.log('Making request to:', url);

    let response;
    try {
        response = await fetch(url, options);
        console.log('Response status:', response.status);

        // Если ответ успешный, просто вернуть его
        if (response.ok) {
            return response;
        }

        // Если 401 - начинаем обработку обновления токена
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

        // Для других ошибок (например 400, 500 и др.)
        return response;

    } catch (error) {
        console.error('Fetch error:', error);
        throw error;
    }
}