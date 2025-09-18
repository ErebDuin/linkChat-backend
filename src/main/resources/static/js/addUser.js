/* addUser.js  –  opens the “Add new user” modal and handles cancel */

document.addEventListener('DOMContentLoaded', () => {
  const openBtn   = document.getElementById('addUserButton');  // “New User” button
  const dialog    = document.getElementById('addUserDialog'); // <dialog> element
  const cancelBtn = document.getElementById('cancelAdd');     // “Cancel” in modal

  if (!openBtn || !dialog) return;

  // open modal
  openBtn.addEventListener('click', e => {
    e.preventDefault();
    dialog.showModal();
  });

  // close modal
  cancelBtn?.addEventListener('click', e => {
    e.preventDefault();
    dialog.close();
  });

  // optional: close on ESC key for browsers without native support
  dialog.addEventListener('cancel', e => e.preventDefault()); // prevent default ESC close
  window.addEventListener('keydown', e => {
    if (e.key === 'Escape' && dialog.open) dialog.close();
  });
});

document.addEventListener("DOMContentLoaded", () => {
    const roleSelector = document.getElementById("roleSelector");

    const passwordLabel = document.getElementById("passwordLabel");
    const passwordInput = document.getElementById("passwordInput");
    const confirmPasswordLabel = document.getElementById("confirmPasswordLabel");
    const confirmPasswordInput = document.getElementById("confirmPasswordInput");

    const hiddenElements = [passwordLabel, passwordInput, confirmPasswordLabel, confirmPasswordInput];
    const inputElements = [passwordInput, confirmPasswordInput]; // only these can be required

    roleSelector.addEventListener("change", () => {
        if (roleSelector.value === "ADMIN") {
            hiddenElements.forEach(el => el.style.display = "block");
            inputElements.forEach(el => el.required = true);
        } else {
            hiddenElements.forEach(el => el.style.display = "none");
            inputElements.forEach(el => {
                el.required = false;
                el.value = ""; // clear in case user typed something
            });
        }
    });
});