document.addEventListener('DOMContentLoaded', function () {
    const dealTypeField = document.getElementById('dealTypeHidden');
    const rentTypeField = document.getElementById('rentTypeHidden');
    const form = document.querySelector('form');

    document.getElementById('nav-sale-tab').addEventListener('click', function () {
        dealTypeField.value = 'sale';
        rentTypeField.value = '';
    });

    document.getElementById('nav-rent-tab').addEventListener('click', function () {
        dealTypeField.value = 'rent';
        rentTypeField.value = 'Посуточно';
    });

    document.getElementById('rent-short-tab').addEventListener('click', function () {
        dealTypeField.value = 'rent';
        rentTypeField.value = 'Посуточно';
    });

    document.getElementById('rent-long-tab').addEventListener('click', function () {
        dealTypeField.value = 'rent';
        rentTypeField.value = 'Долгосрочно';
    });

    form.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
        }
    });

    form.addEventListener('submit', function(e) {
        const priceMin = document.getElementById('priceMin');
        const priceMax = document.getElementById('priceMax');
        const address = document.getElementById('address');
        const rooms = document.getElementById('rooms');
        const propertyType = document.getElementById('propertyType');

        if (!priceMin.value) priceMin.value = '';
        if (!priceMax.value) priceMax.value = '';
        if (!address.value) address.value = '';
        if (!rooms.value) rooms.value = '';
        if (!propertyType.value) propertyType.value = '';
    });
});