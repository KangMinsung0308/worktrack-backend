
function funcWorkDate() {
  const workDate = document.getElementById("getWorkDate").value;

  fetch(`${API_URL}/${workDate}`)
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
        <td>${item.workDate.substring(5, 10)}</td>
        <td>${item.startTime.split("T")[1].substring(0, 5)}</td>
        <td>${item.endTime.split("T")[1].substring(0, 5)}</td>
        <td>${item.totalHours}</td>
        <td>${item.overtime}</td>
      `;
    tbody.appendChild(tr);
  });
}

function funclogout() {
  fetch(`${API_URL_LOGOUT}`, { method: "POST" })
    .then((res) => res.json())
    .then((data) => {
      if (data.success) {
        window.location.href = URL_LOGIN;
        showToast("로그아웃 되었습니다.");
      } else {
        console.log("로그아웃 실패");
      }
    })
    .catch((err) => console.error("요청 오류:", err));
}

// 버튼 클릭 시 함수 실행 연결
document.getElementById("loadData").addEventListener("click", funcWorkDate);
document.getElementById("postLogout").addEventListener("click", funclogout);
