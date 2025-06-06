$(document).ready(function () {
    // Функция для активации вкладки
    function activateTab(tabName) {
        // Скрыть все секции
        $(".tab-section").addClass("d-none");
        // Показать выбранную секцию
        $("#" + tabName).removeClass("d-none");

        // Обновить активные ссылки в меню
        $("a[data-tab]").removeClass("title");
        $("a[data-tab='" + tabName + "']").addClass("title");
    }

    // Обработчик клика по ссылкам меню
    $("a[data-tab]").click(function (e) {
        e.preventDefault();
        var tabName = $(this).data("tab");
        activateTab(tabName);
    });

    // Активировать начальную вкладку при загрузке страницы
    var initialTab = 'advertisement';
    activateTab(initialTab);
});