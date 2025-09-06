document.querySelectorAll('.toggle-password').forEach(icon => {
    icon.addEventListener('click', () => {
        const form = icon.closest('form');
        // Only toggle fields with class "password-field"
        form.querySelectorAll('input.password-field').forEach(input => {
            const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
            input.setAttribute('type', type);
        });
        icon.classList.toggle('fa-eye');
        icon.classList.toggle('fa-eye-slash');
    });
});