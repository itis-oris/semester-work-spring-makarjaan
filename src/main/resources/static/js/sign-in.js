document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        },
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => {
                    throw new Error(data.message || 'Ошибка входа');
                });
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem('token', data.accessToken);
            window.location.href = '/main';
        })
        .catch(error => {
            const errorDiv = document.getElementById('error');
            errorDiv.textContent = error.message;
            errorDiv.classList.remove('d-none');
        });
});