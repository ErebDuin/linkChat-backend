/* addUser.js  –  opens the “Add new user” modal and handles cancel */

document.addEventListener('DOMContentLoaded', () => {
  const openBtn   = document.querySelector('.new-user-btn');  // “New User” button
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