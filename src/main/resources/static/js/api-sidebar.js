$(document).ready(function () {
    $("a[data-tab]").click(function (e) {
        e.preventDefault();
        var tabName = $(this).data("tab");

        loadTabContent(tabName);

        $(".tab-section").addClass("d-none");
        $("#" + tabName).removeClass("d-none");

        $("a[data-tab]").removeClass("title");
        $(this).addClass("title");
    });


    function loadTabContent(tabName) {
        $.get('/settings', { activeTab: tabName }, function(response) {
            $('#tabContent').html(response);
        });
    }


    var initialTab = 'advertisement';
    loadTabContent(initialTab);
    $("#" + initialTab).removeClass("d-none");
});
