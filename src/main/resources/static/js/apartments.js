document.addEventListener('DOMContentLoaded', function () {
    const dealTypeField = document.getElementById('dealTypeHidden');
    const rentTypeField = document.getElementById('rentTypeHidden');

    dealTypeField.value = 'Продажа';

    document.getElementById('nav-sale-tab').addEventListener('click', function () {
        dealTypeField.value = 'Продажа';
        rentTypeField.value = '';
    });

    document.getElementById('nav-rent-tab').addEventListener('click', function () {
        dealTypeField.value = 'Аренда';
        rentTypeField.value = 'Посуточно';
    });

    document.getElementById('rent-short-tab').addEventListener('click', function () {
        rentTypeField.value = 'Посуточно';
    });

    document.getElementById('rent-long-tab').addEventListener('click', function () {
        rentTypeField.value = 'Долгосрочно';
    });
});