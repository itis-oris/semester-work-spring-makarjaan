$(document).ready(function () {
    function activateTab(tabName) {
        $(".tab-section").addClass("d-none");
        $("#" + tabName).removeClass("d-none");

        $("a[data-tab]").removeClass("title");
        $("a[data-tab='" + tabName + "']").addClass("title");
    }

    $("a[data-tab]").click(function (e) {
        e.preventDefault();
        var tabName = $(this).data("tab");
        activateTab(tabName);
    });

    var initialTab = 'advertisement';
    activateTab(initialTab);
});