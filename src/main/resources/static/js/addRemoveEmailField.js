window.addInvite = function () {
  const container = document.getElementById('invite-container');
  const fields = container.querySelectorAll('.invite-field');
  const newIndex = fields.length;

  const lastField = fields[fields.length - 1];
  const newField = lastField.cloneNode(true);

  const input = newField.querySelector('input[type="email"]');
  input.value = '';
  input.name = `inviteEmails[${newIndex}]`;
  input.id = `inviteEmails${newIndex}`;

  container.appendChild(newField);
  console.log("addRemoveEmailField.js loaded");
};

window.removeInvite = function (button) {
  const container = document.getElementById('invite-container');
  const fields = container.querySelectorAll('.invite-field');

  if (fields.length > 1) {
    const field = button.closest('.invite-field');
    container.removeChild(field);
  }
};