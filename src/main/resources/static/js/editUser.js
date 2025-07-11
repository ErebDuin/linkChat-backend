export function showSuccessAndRedirect(message, redirectUrl, delay = 3000) {
  document.querySelectorAll('.toast-success').forEach(t => t.remove());

  const toast = Object.assign(document.createElement('div'), {
    className: 'toast-success',
    textContent: message
  });
  document.body.appendChild(toast);

  setTimeout(() => (window.location.href = redirectUrl), delay);
}

document.addEventListener('DOMContentLoaded', () => {
  const form          = document.getElementById('editForm');
  const deleteBtn     = document.getElementById('deleteBtn');
  const dialog        = document.getElementById('confirmDialog');
  const confirmDelete = document.getElementById('confirmDelete');
  const cancelDelete  = document.getElementById('cancelDelete');

  if (!form || !deleteBtn) return;

  deleteBtn.addEventListener('click', e => {
    e.preventDefault();
    if (dialog?.showModal) dialog.showModal();
    else if (confirm('Are you sure you want to delete this user?')) submitDelete();
  });

  confirmDelete?.addEventListener('click', e => {
    e.preventDefault();
    submitDelete();
  });

  cancelDelete?.addEventListener('click', e => {
    e.preventDefault();
    dialog?.close();
  });

  function submitDelete() {
    dialog?.close();
    form.action = '/ui/delete-user';
    form.method = 'post';
    form.submit();
  }
});



