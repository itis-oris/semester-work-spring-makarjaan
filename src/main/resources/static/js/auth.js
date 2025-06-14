document.addEventListener("DOMContentLoaded", function () {
    const nav = document.getElementById("authNav");
    const token = sessionStorage.getItem("accessToken");

    // Проверяем валидность токена
    if (token && !isTokenValid(token)) {
        sessionStorage.removeItem("accessToken");
        updateNavigationForGuest();
        return;
    }

    // Показ/скрытие пункта "Избранные"
    const favoritesMenuItem = document.getElementById("favoritesMenuItem");
    if (token) {
        if (favoritesMenuItem) {
            favoritesMenuItem.style.display = "block";
        }
        // Получаем информацию о пользователе
        fetchUserInfo(token);
    } else {
        updateNavigationForGuest();
    }
});

function updateNavigationForGuest() {
    const nav = document.getElementById("authNav");
    const favoritesMenuItem = document.getElementById("favoritesMenuItem");
    
    if (favoritesMenuItem) {
        favoritesMenuItem.style.display = "none";
    }
    
    nav.innerHTML = `
        <li class="nav-item">
            <a href="/signIn" class="custom-btn">Войти</a>
        </li>
    `;
}

function isTokenValid(token) {
    try {
        // Декодируем JWT токен
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        const payload = JSON.parse(jsonPayload);
        const expirationTime = payload.exp * 1000; // конвертируем в миллисекунды
        const currentTime = Date.now();

        return currentTime < expirationTime;
    } catch (error) {
        console.error('Error checking token validity:', error);
        return false;
    }
}

async function fetchUserInfo(token) {
    try {
        const response = await fetch('/api/user/info', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const userData = await response.json();
            updateNavigation(userData);
        } else if (response.status === 401) {
            // Если токен недействителен, удаляем его и обновляем навигацию
            sessionStorage.removeItem("accessToken");
            updateNavigationForGuest();
        } else {
            console.error('Failed to fetch user info');
            updateNavigation({ username: 'Пользователь' });
        }
    } catch (error) {
        console.error('Error fetching user info:', error);
        updateNavigation({ username: 'Пользователь' });
    }
}

function updateNavigation(userData) {
    const nav = document.getElementById("authNav");
    const defaultAvatar = "https://res.cloudinary.com/dqm8yufmb/image/upload/v1733780390/%D0%94%D0%B5%D1%84%D0%BE%D0%BB%D1%8C%D0%BD%D0%B0%D1%8F_%D0%B0%D0%B2%D0%B0%D1%82%D0%B0%D1%80%D0%BA%D0%B0_adpx3f.jpg";
    
    nav.innerHTML = `
        <li class="nav-item dropdown d-flex align-items-center">
            <a class="nav-link dropdown-toggle d-flex align-items-center" id="profileDropdown"
               role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <img src="${userData.profilePhotoUrl || defaultAvatar}"   
                     class="rounded-circle me-2" width="40" height="40"
                     onerror="this.src='${defaultAvatar}'"/>
                <span class="me-2 fw-bold user-name">${userData.username || 'Пользователь'}</span>
            </a>
            <ul class="dropdown-menu text-small" aria-labelledby="profileDropdown">
                <li><a class="dropdown-item" href="/settings">Настройки</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a href="#" class="dropdown-item" id="logoutLink">Выйти</a></li>
            </ul>
        </li>
    `;

    // Добавляем обработчик для кнопки выхода
    const logoutLink = document.getElementById("logoutLink");
    if (logoutLink) {
        logoutLink.addEventListener("click", handleLogout);
    }
}

async function handleLogout(event) {
    event.preventDefault();

    try {
        const response = await fetch('/api/auth/logout', {
            method: 'POST',
            credentials: 'include'
        });

        if (response.ok) {
            sessionStorage.removeItem('accessToken');
            window.location.href = '/main';
        } else {
            console.error('Logout failed');
        }
    } catch (error) {
        console.error('Logout error:', error);
    }
}

// Добавляем интервал для проверки токена каждую минуту
setInterval(() => {
    const token = sessionStorage.getItem("accessToken");
    if (token && !isTokenValid(token)) {
        sessionStorage.removeItem("accessToken");
        updateNavigationForGuest();
    }
}, 60000); // 60000 мс = 1 минута 