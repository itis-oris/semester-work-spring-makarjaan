document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("advertForm");
    const dealTypeBtns = document.querySelectorAll(".deal-type-btn");
    const previewContainer = document.getElementById("preview");
    const imageInput = document.getElementById("images");

    dealTypeBtns.forEach(button => {
        button.addEventListener("click", () => {
            dealTypeBtns.forEach(btn => btn.classList.remove("active"));
            button.classList.add("active");

            const dealType = button.getAttribute("data-deal-type");
            document.getElementById("dealType").value = dealType;

            const isRent = dealType === "rent";
            document.getElementById("rentFields").classList.toggle("d-none", !isRent);
            document.getElementById("saleFields").classList.toggle("d-none", isRent);
        });
    });

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        clearErrors();

        try {
            const response = await fetch("/api/adverts/add", {
                method: "POST",
                body: new FormData(form)
            });

            const result = await response.json();

            if (response.ok) {
                window.location.href = '/settings';
            } else if (result.errors) {
                showValidationErrors(result.errors);
            } else {
                alert(result.message || "Произошла ошибка при добавлении объявления");
            }
        } catch {
            alert("Произошла ошибка при отправке данных");
        }
    });

    imageInput.addEventListener("change", () => {
        previewContainer.innerHTML = "";

        Array.from(imageInput.files)
            .filter(file => file.type.startsWith("image/"))
            .forEach(file => {
                const reader = new FileReader();
                reader.onload = (e) => {
                    previewContainer.insertAdjacentHTML('beforeend', `
                        <div class="col-md-3">
                            <div class="preview-item">
                                <img src="${e.target.result}" class="preview-image" alt="Превью">
                            </div>
                        </div>
                    `);
                };
                reader.readAsDataURL(file);
            });
    });

    function showValidationErrors(errors) {
        Object.entries(errors).forEach(([field, error]) => {
            const errorDiv = document.getElementById(`${field}Error`);
            const input = document.getElementById(field);

            if (errorDiv) {
                errorDiv.textContent = error;
            }
            if (input) {
                input.classList.add("is-invalid");
            }
        });
    }

    function clearErrors() {
        document.querySelectorAll(".invalid-feedback").forEach(div => div.textContent = "");
        document.querySelectorAll(".is-invalid").forEach(input => input.classList.remove("is-invalid"));
    }
});