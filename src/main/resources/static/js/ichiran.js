const apiUrl = "http://localhost:8080/worktrack";

function funcWorkDate() {
  const workDate = document.getElementById("getWorkDate").value;

  fetch(`${apiUrl}/${workDate}`)
    .then((res) => res.json())
    .then(renderTable)
    .catch((err) => alert("조회 오류: " + err));
}

function renderTable(data) {
  const tbody = document.querySelector("#workTimeTable tbody");
  tbody.innerHTML = "";

  data.forEach((item) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
        <td>${item.workDate}</td>
        <td>${item.startTime.split("T")[1].substring(0, 5)}</td>
        <td>${item.endTime.split("T")[1].substring(0, 5)}</td>
        <td>${item.totalHours}</td>
        <td>${item.overtime}</td>
      `;
    tbody.appendChild(tr);
  });
}

function funclogout() {
  fetch(`${apiUrl}/logout`, { method: "POST" })
    .then((res) => res.json())
    .then((data) => {
      if (data.success) {
        window.location.href = "/worktrack/login";
        showToast("로그아웃 되었습니다.");
      } else {
        console.log("로그아웃 실패");
      }
    })
    .catch((err) => console.error("요청 오류:", err));
}

function showToast(message) {
  const toast = document.createElement("div");
  toast.className = "toast";
  toast.textContent = message;
  document.body.appendChild(toast);

  setTimeout(() => {
    toast.classList.add("hide");
    setTimeout(() => {
      document.body.removeChild(toast);
    }, 300);
  }, 2000);
}

// 버튼 클릭 시 함수 실행 연결
document.getElementById("loadData").addEventListener("click", funcWorkDate);
document.getElementById("postLogout").addEventListener("click", funclogout);
