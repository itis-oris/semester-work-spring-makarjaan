$(document).ready(function () {
    // Обработка формы удаления аккаунта
    $('#deleteAccountForm').on('submit', function(e) {
        e.preventDefault();
        if (confirm('Вы уверены, что хотите удалить этот аккаунт? Это действие нельзя отменить.')) {
            const formData = new FormData(this);
            $.ajax({
                url: "/api/settings",
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                success: function() {
                    sessionStorage.removeItem('accessToken');
                    window.location.href = '/main';
                },
                error: function(xhr) {
                    const errorMessage = xhr.responseJSON?.error || "Ошибка при удалении аккаунта";
                    showErrorMessage(errorMessage);
                }
            });
        }
    });

    // Обработка остальных форм
    $('form:not(#deleteAccountForm)').on('submit', function (e) {
        e.preventDefault();

        const form = $(this);
        const formData = new FormData(this);

        console.log("FormData contains:");
        for (let [key, value] of formData.entries()) {
            console.log(key, value);
        }

        $.ajax({
            url: "/api/settings",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                showSuccessMessage("Данные успешно сохранены");
                setTimeout(() => location.reload(), 1000);
            },
            error: function (xhr) {
                const errorMessage = xhr.responseJSON?.error || "Ошибка";
                showErrorMessage(errorMessage);
            }
        });
    });

    function showSuccessMessage(message) {
        showAlert(message, 'success');
    }

    function showErrorMessage(message) {
        showAlert(message, 'danger');
    }

    function showAlert(message, type) {
        $('#tabContent .alert').remove();

        const alertHtml = `
            <div class="alert alert-${type} alert-dismissible fade show mt-3" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>`;

        $('#tabContent').prepend(alertHtml);
    }
});