// ====== Элементы страницы ======
const albumForm = document.getElementById("albumForm");
const uploadForm = document.getElementById("uploadForm");
const player = document.getElementById("player");
const albumsContainer = document.getElementById("albumsContainer");
const albumSelect = document.getElementById("albumSelect");
const allSongsContainer = document.getElementById("allSongsContainer"); // список всех песен (блок)
const userInfo = document.getElementById("userInfo");

// ====== CSRF из cookie ======
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
    return null;
}
function csrfHeaders() {
    const token = getCookie("XSRF-TOKEN"); // если включён Spring Security CSRF
    return token ? { "X-CSRF-TOKEN": token } : {};
}

// ====== Показать текущего пользователя ======
async function loadUser() {
    try {
        const response = await fetch("/me", { credentials: "include" });
        if (response.ok) {
            const user = await response.json();
            userInfo.textContent = "Вы вошли как: " + user.username;
        } else {
            userInfo.textContent = "Не авторизован";
        }
    } catch {
        userInfo.textContent = "Не авторизован";
    }
}

// ====== Загрузка альбома ======
albumForm?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(albumForm);

    const submitBtn = albumForm.querySelector("button[type='submit']");
    submitBtn.disabled = true;

    try {
        const response = await fetch("/api", {
            method: "POST",
            body: formData,
            credentials: "include",
            headers: { ...csrfHeaders() }
        });
        if (!response.ok) throw new Error("Ошибка добавления альбома");
        await response.json();
        await loadAlbums();
        await loadSongs();
    } catch (err) {
        alert("Не удалось добавить альбом");
        console.error(err);
    } finally {
        submitBtn.disabled = false;
        albumForm.reset();
    }
});

// ====== Загрузка песни ======
uploadForm?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(uploadForm);

    const submitBtn = uploadForm.querySelector("button[type='submit']");
    submitBtn.disabled = true;

    try {
        const response = await fetch("/api/add", {
            method: "POST",
            body: formData,
            credentials: "include",
            headers: { ...csrfHeaders() }
        });
        if (!response.ok) throw new Error("Ошибка загрузки песни");
        // await response.json();
        await loadAlbums();
        await loadSongs();
    } catch (err) {
        alert("Не удалось загрузить файл");
        console.error(err);
    } finally {
        submitBtn.disabled = false;
        uploadForm.reset();
    }
});

// ====== Загрузка альбомов и их песен ======
async function loadAlbums() {
    try {
        const response = await fetch("/api/all/albums", { credentials: "include" });
        if (!response.ok) throw new Error("Ошибка получения альбомов");
         const albums = await response.json();


        albumsContainer.innerHTML = "";
        albumSelect.innerHTML = '<option value="">-- без альбома --</option>';

        Object.entries(albums).forEach(([title, info]) => {
            const div = document.createElement("div");
            div.className = "album";
            div.innerHTML = `
                <img src="${info.imageurl}" alt="${title}">
                <p><strong>${title}</strong></p>
                <p><em>Автор альбома: ${info.username}</em></p>
                <button class="like-album-btn" data-id="${info.id}">${info.isFavorite === true || info.favorite === true ? "❤ В избранном" : "❤ Лайк"}</button>
                <ul id="songs-${title}"></ul>
            `;
            albumsContainer.appendChild(div);

            // дополняем селект для добавления песни
            const option = document.createElement("option");
            option.value = title;
            option.textContent = title;
            albumSelect.appendChild(option);

            // Загружаем песни альбома
            fetch(`/api/albums/${encodeURIComponent(title)}/songs`, { credentials: "include" })
                .then(r => {
                    if (!r.ok) throw new Error("Ошибка ответа: " + r.status);
                    return r.json();
                })
                .then(songs => {
                    const ul = document.getElementById(`songs-${title}`);
                    console.log(songs)
                    if (!Array.isArray(songs) || songs.length === 0) {
                        ul.innerHTML = "<li><em>Нет песен</em></li>";
                    } else {

                        songs.forEach(song => {
                            const url = song.fileUrl || song.fileurl || "";
                            const li = document.createElement("li");
                            li.innerHTML = `
                                ${song.songname}
                                <span style="color: gray"> (от ${song.account?.username || song.artist || "неизвестно"})</span>
                                <button type="button" onclick="playSong('${encodeURI(url)}')">▶</button>
                                <button class="like-song-btn" data-id="${song.id}">❤ Лайк</button>
                            `;
                            ul.appendChild(li);
                        });
                    }
                })
                .catch(err => {
                    console.error("Ошибка загрузки песен альбома", err);
                });
        });
    } catch (err) {
        console.error(err);
        alert("Не удалось загрузить альбомы");
    }
}

// ====== Общий обработчик лайков ======
document.addEventListener("click", async (e) => {
    const target = e.target;

    // Лайк альбома
    if (target.classList.contains("like-album-btn")) {
        const albumId = target.dataset.id;
        if (!albumId) return;
        if (target.disabled || target.textContent.includes("В избранном")) return;

        target.disabled = true;
        try {
            const response = await fetch(`/favorites?albumId=${albumId}`, {
                method: "POST",
                credentials: "include",
                headers: { ...csrfHeaders() }
            });
            if (!response.ok) {
                target.disabled = false;
                throw new Error("Ошибка добавления альбома в избранное");
            }
            target.textContent = "❤ В избранном";
        } catch (err) {
            console.error("Не удалось добавить альбом в избранное", err);
            alert("Не удалось добавить альбом в избранное");
            target.disabled = false;
        }
    }

    // Лайк песни (в списке альбома или в общем списке)
    if (target.classList.contains("like-song-btn")) {
        const songId = target.dataset.id;
        if (!songId) return;
        if (target.disabled || target.textContent.includes("В избранном")) return;

        target.disabled = true;
        try {
            const response = await fetch(`/favorites/songs?songId=${songId}`, {
                method: "POST",
                credentials: "include",
                headers: { ...csrfHeaders() }
            });
            if (!response.ok) {
                target.disabled = false;
                throw new Error("Ошибка добавления песни в избранное");
            }
            target.textContent = "❤ В избранном";
        } catch (err) {
            console.error("Не удалось добавить песню в избранное", err);
            alert("Не удалось добавить песню в избранное");
            target.disabled = false;
        }
    }
});

// ====== Загрузка всех песен (бэкенд: Map<String, SongInfo>) ======
async function loadSongs() {
    try {
        const response = await fetch("/api/all", { credentials: "include" });
        if (!response.ok) throw new Error("Ошибка получения песен");
        const songs = await response.json();

        allSongsContainer.innerHTML = "";
        if (!songs || Object.keys(songs).length === 0) {
            allSongsContainer.innerHTML = "<p><em>Нет песен</em></p>";
            return;
        }

        // songs — это Map: { songname: { fileUrl, id } }
        Object.entries(songs).forEach(([name, info]) => {
            const url = info?.fileUrl || "";
            const id = info?.id;

            const div = document.createElement("div");
            div.className = "song";
            div.innerHTML = `
                <p><strong>${name}</strong></p>
                <button onclick="playSong('${encodeURI(url)}')">▶</button>
                <button class="like-song-btn" data-id="${id}">❤ Лайк</button>
            `;
            allSongsContainer.appendChild(div);
        });
    } catch (err) {
        console.error(err);
        allSongsContainer.innerHTML = "<p>Ошибка загрузки песен</p>";
    }
}

// ====== Проигрывание ======
function playSong(url) {
    if (!url) return;
    player.src = url;
    player.play();
}
window.playSong = playSong;

// ====== Инициализация ======
window.addEventListener("DOMContentLoaded", async () => {
    await loadUser();
    await loadAlbums();
    await loadSongs();
});
