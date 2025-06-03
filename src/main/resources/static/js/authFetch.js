async function authFetch(url, options = {}) {
    const accessToken = localStorage.getItem('token');
    options.headers = {
        ...options.headers,
        Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    };

    let response = await fetch(url, options);

    if (response.status === 401) {
        const refreshToken = localStorage.getItem('refreshToken');
        const refreshResponse = await fetch('/api/auth/refresh', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ refreshToken })
        });

        if (refreshResponse.ok) {
            const data = await refreshResponse.json();
            localStorage.setItem('accessToken', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);

            options.headers.Authorization = `Bearer ${data.accessToken}`;
            response = await fetch(url, options);
        } else {
            window.location.href = '/login';
            return;
        }
    }

    return response;
}
