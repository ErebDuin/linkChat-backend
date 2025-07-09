document.addEventListener("DOMContentLoaded", function() {
  const logoutBtn = document.getElementById("logoutButton");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", function() {
      fetch("/logout", {
        method: "POST",
        credentials: "include"
      })
      .then(response => {
        if (response.ok) {
          window.location.href = "/ui/admin-login";
        } else if (response.ok) {
                    window.location.href = "/ui/admin-login";
             } else {
          alert("Logout failed!");
        }
      })
      .catch(() => alert("Logout failed!"));
    });
  }
});