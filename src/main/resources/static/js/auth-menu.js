document.addEventListener("DOMContentLoaded", function () {
    const nav = document.getElementById("authNav");
    const token = localStorage.getItem("token");

    if (token) {
        nav.innerHTML = `
            <li class="nav-item dropdown d-flex align-items-center">
                <a class="nav-link dropdown-toggle d-flex align-items-center" id="profileDropdown"
                   role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    <img src="https://res.cloudinary.com/dqm8yufmb/image/upload/v1733780390/%D0%94%D0%B5%D1%84%D0%BE%D0%BB%D1%8C%D1%82%D0%BD%D0%B0%D1%8F_%D0%B0%D0%B2%D0%B0%D1%82%D0%B0%D1%80%D0%BA%D0%B0_adpx3f.jpg"   
                         class="rounded-circle me-2" width="40" height="40"/>
                    <span class="me-2 fw-bold user-name">Пользователь</span>
                </a>
                <ul class="dropdown-menu text-small" aria-labelledby="profileDropdown">
                    <li><a class="dropdown-item" href="/settings">Настройки</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="#" onclick="logout()">Выйти</a></li>
                </ul>
            </li>
        `;
    } else {
        nav.innerHTML = `
            <li class="nav-item">
                <a href="/signIn" class="custom-btn">Войти</a>
            </li>
        `;
    }
});

function logout() {
    localStorage.removeItem("token");
    window.location.reload();
}
