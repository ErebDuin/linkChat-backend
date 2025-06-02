function openPopup() {
    document.getElementById('forgotModal').style.display = 'block';
}
function closePopup() {
    document.getElementById('forgotModal').style.display = 'none';
}
window.onclick = function(event) {
    var modal = document.getElementById('forgotModal');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}