$(document).ready(function () {
    function activateTab(tabName) {
        $(".tab-section").addClass("d-none");

        $("#" + tabName).removeClass("d-none");

        $("a.nav-link").removeClass("active");

        $("a[data-tab='" + tabName + "']").addClass("active");
    }

    $("a.nav-link").on("click", function (e) {
        e.preventDefault();
        var tabName = $(this).data("tab");
        activateTab(tabName);
    });
    var initialTab = 'advertisement';
    activateTab(initialTab);
});