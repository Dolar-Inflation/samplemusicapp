// ====== Элементы страницы ======
const albumForm = document.getElementById("albumForm");
const uploadForm = document.getElementById("uploadForm");
const player = document.getElementById("player");
const albumsContainer = document.getElementById("albumsContainer");
const albumSelect = document.getElementById("albumSelect");
const songList = document.getElementById("songList");
const userInfo = document.getElementById("userInfo");

// ====== Вспомогательное: CSRF из cookie (если включён Spring Security CSRF) ======
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
    return null;
}
function csrfHeaders() {
    const token = getCookie("XSRF-TOKEN"); // стандартное имя для cookie в Spring Security (если настроено)
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

    // Блокируем кнопку на время запроса
    const submitBtn = albumForm.querySelector("button[type='submit']");
    submitBtn.disabled = true;

    try {
        const response = await fetch("/api", {
            method: "POST",
            body: formData,
            credentials: "include",
            headers: {
                ...csrfHeaders()
            }
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
            headers: {
                ...csrfHeaders()
            }
        });
        if (!response.ok) throw new Error("Ошибка загрузки песни");
        await response.json();
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
                <button class="like-btn" data-id="${info.id}">❤ Лайк</button>
                <ul id="songs-${title}"></ul>
            `;
            albumsContainer.appendChild(div);

            // Если бэкенд уже возвращает признак, что альбом в избранном — сразу отключим кнопку
            const likeBtn = div.querySelector(".like-btn");
            if (info.isFavorite === true || info.favorite === true) {
                likeBtn.disabled = true;
                likeBtn.textContent = "❤ В избранном";
            }

            // Дополняем селект для добавления песни в альбом
            const option = document.createElement("option");
            option.value = title;
            option.textContent = title;
            albumSelect.appendChild(option);

            // Загружаем песни альбома
            fetch(`/api/albums/${encodeURIComponent(title)}/songs`, { credentials: "include" })
                .then(r => r.json())
                .then(songs => {
                    const ul = document.getElementById(`songs-${title}`);
                    if (!Array.isArray(songs) || songs.length === 0) {
                        ul.innerHTML = "<li><em>Нет песен</em></li>";
                    } else {
                        songs.forEach(song => {
                            const li = document.createElement("li");
                            li.innerHTML = `
                                ${song.songname}
                                <span style="color: gray"> (от ${song.account?.username || song.artist})</span>
                                <button type="button" onclick="playSong('${song.fileUrl}')">▶</button>
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

// ====== Обработка лайков (избранное, один раз) ======
albumsContainer.addEventListener("click", async (e) => {
    const target = e.target;
    if (target.classList.contains("like-btn")) {
        const albumId = target.dataset.id;
        if (!albumId) return;

        target.disabled = true; // защищаем от двойного клика во время запроса

        try {
            const response = await fetch(`/favorites?albumId=${albumId}`, {
                method: "POST",
                credentials: "include",
                headers: {
                    ...csrfHeaders()
                }
            });
            if (!response.ok) {
                // вернём кнопку в исходное состояние, если неуспешно
                target.disabled = false;
                throw new Error("Ошибка добавления в избранное");
            }
            target.textContent = "❤ В избранном";
        } catch (err) {
            alert("Не удалось добавить в избранное");
            console.error(err);
        }
    }
});

// ====== Загрузка всех песен ======
async function loadSongs() {
    try {
        const response = await fetch("/api/all", { credentials: "include" });
        if (!response.ok) throw new Error("Ошибка получения списка песен");
        const songs = await response.json();

        songList.innerHTML = '<option value="">-- выберите песню --</option>';
        for (const [name, url] of Object.entries(songs)) {
            const option = document.createElement("option");
            option.value = encodeURI(url);
            option.textContent = name;
            songList.appendChild(option);
        }
    } catch (err) {
        console.error(err);
        alert("Не удалось загрузить список песен");
    }
}

// ====== Проигрывание ======
function playSong(url) {
    player.src = encodeURI(url);
    player.play();
}
window.playSong = playSong; // чтобы можно было вызывать из onclick в шаблоне

songList.addEventListener("change", () => {
    if (songList.value) {
        player.src = songList.value;
        player.play();
    }
});

// ====== Инициализация ======
window.addEventListener("DOMContentLoaded", async () => {
    await loadUser();
    await loadAlbums();
    await loadSongs();
});
