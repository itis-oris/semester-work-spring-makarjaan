document.addEventListener('DOMContentLoaded', function () {
    const dealTypeField = document.getElementById('dealTypeHidden');
    const rentTypeField = document.getElementById('rentTypeHidden');
    const form = document.querySelector('form');

    dealTypeField.value = 'Продажа';
    rentTypeField.value = '';

    document.getElementById('nav-sale-tab').addEventListener('click', function () {
        dealTypeField.value = 'Продажа';
        rentTypeField.value = '';
        form.submit();
    });

    document.getElementById('nav-rent-tab').addEventListener('click', function () {
        dealTypeField.value = 'Аренда';
        rentTypeField.value = 'Посуточно';
        form.submit();
    });

    document.getElementById('rent-short-tab').addEventListener('click', function () {
        dealTypeField.value = 'Аренда';
        rentTypeField.value = 'Посуточно';
        form.submit();
    });

    document.getElementById('rent-long-tab').addEventListener('click', function () {
        dealTypeField.value = 'Аренда';
        rentTypeField.value = 'Долгосрочно';
        form.submit();
    });

    form.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
        }
    });

    form.addEventListener('submit', function(e) {
        const priceMin = document.getElementById('priceMin').value;
        const priceMax = document.getElementById('priceMax').value;
        const address = document.getElementById('address').value;
        const rooms = document.getElementById('rooms').value;
        const propertyType = document.getElementById('propertyType').value;

        if (!priceMin && !priceMax && !address && !rooms && !propertyType) {
            return true;
        }

        return true;
    });
});