async function authFetch(url, options = {}) {
    const accessToken = sessionStorage.getItem('accessToken');

    options.headers = {
        ...options.headers,
        ...(accessToken && { 'Authorization': 'Bearer ' + accessToken }),
        ...(options.body instanceof FormData ? {} : { 'Content-Type': 'application/json' })
    };

    console.log('Making request to:', url);
    let response = await fetch(url, options);

    if (response.status === 401) {
        console.log('Access token expired, refreshing...');

        try {
            const refreshResponse = await fetch('/api/auth/token', {
                method: 'POST',
                credentials: 'include'
            });

            if (refreshResponse.ok) {
                const { accessToken: newToken } = await refreshResponse.json();
                sessionStorage.setItem('accessToken', newToken);
                console.log('New access token stored');

                options.headers['Authorization'] = 'Bearer ' + newToken;
                return fetch(url, options);
            } else {
                console.error('Refresh token failed');
                sessionStorage.removeItem('accessToken');
                window.location.href = '/signIn';
                return Promise.reject(new Error('Session expired'));
            }
        } catch (refreshError) {
            console.error('Token refresh error:', refreshError);
            window.location.href = '/signIn';
            return Promise.reject(refreshError);
        }
    }

    return response;
}