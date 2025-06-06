document.addEventListener('DOMContentLoaded', function() {
    const addAdvertButton = document.querySelector('.add-advert-btn');
    if (addAdvertButton) {
        addAdvertButton.style.display = 'none';
    }

    const rentLink = document.querySelector('a[data-deal-type="rent"]');
    const saleLink = document.querySelector('a[data-deal-type="sale"]');

    if (rentLink && saleLink) {
        rentLink.addEventListener('click', function(e) {
            e.preventDefault();
            switchDealType('rent');
        });

        saleLink.addEventListener('click', function(e) {
            e.preventDefault();
            switchDealType('sale');
        });
    }

    function switchDealType(type) {
        rentLink.classList.toggle('rounded-custom-btn', type === 'rent');
        saleLink.classList.toggle('rounded-custom-btn', type === 'sale');

        const rentFields = document.getElementById('rentFields');
        const saleFields = document.getElementById('saleFields');

        if (type === 'rent') {
            rentFields.classList.remove('d-none');
            saleFields.classList.add('d-none');
        } else {
            rentFields.classList.add('d-none');
            saleFields.classList.remove('d-none');
        }
    }

    const form = document.getElementById('advertForm');
    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        const formData = new FormData(form);

        try {
            const response = await fetch('/api/adverts/add', {
                method: 'POST',
                body: formData
            });

            const data = await response.json();

            if (response.ok) {
                window.location.href = '/settings';
            } else {
                if (data.errors) {
                    clearErrors();

                    Object.keys(data.errors).forEach(field => {
                        const element = document.getElementById(field);
                        if (element) {
                            const errorDiv = document.createElement('div');
                            errorDiv.className = 'invalid-feedback d-block';
                            errorDiv.textContent = data.errors[field];
                            element.parentNode.appendChild(errorDiv);
                            element.classList.add('is-invalid');
                        }
                    });
                } else if (data.message) {
                    showError(data.message);
                }
            }
        } catch (error) {
            showError('Произошла ошибка при добавлении объявления');
        }
    });

    function clearErrors() {
        document.querySelectorAll('.invalid-feedback').forEach(el => el.remove());
        document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
        document.querySelectorAll('.alert').forEach(el => el.remove());
    }

    function showError(message) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger';
        errorDiv.textContent = message;
        form.insertBefore(errorDiv, form.firstChild);
    }
});