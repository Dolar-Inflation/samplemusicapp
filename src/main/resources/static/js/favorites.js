const favoriteAlbumsContainer = document.getElementById("favoriteAlbumsContainer");
const favoriteSongsContainer = document.getElementById("favoriteSongsContainer");
const userInfo = document.getElementById("userInfo");
const player = document.getElementById("player");

// показать текущего пользователя
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

// загрузка избранных альбомов (по факту — все альбомы, с отметкой избранного)
async function loadFavoriteAlbums() {
    try {
        const response = await fetch("/api/all/albums", { credentials: "include" });
        if (!response.ok) throw new Error("Ошибка получения альбомов");
        const albums = await response.json();

        favoriteAlbumsContainer.innerHTML = "";
        if (Object.keys(albums).length === 0) {
            favoriteAlbumsContainer.innerHTML = "<p><em>Нет альбомов</em></p>";
            return;
        }

        Object.entries(albums).forEach(([title, info]) => {
            const div = document.createElement("div");
            div.className = "album";
            div.innerHTML = `
                <img src="${info.imageurl}" alt="${title}">
                <p><strong>${title}</strong></p>
                <p><em>Автор альбома: ${info.username}</em></p>
                <button class="like-btn" data-id="${info.id}">${info.isFavorite ? "❤ В избранном" : "❤ Лайк"}</button>
                <ul id="songs-${title}"></ul>
            `;
            favoriteAlbumsContainer.appendChild(div);

            // загрузка песен альбома
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
        favoriteAlbumsContainer.innerHTML = "<p>Ошибка загрузки альбомов</p>";
    }
}

// загрузка избранных песен (по факту — все песни)
async function loadFavoriteSongs() {
    try {
        const response = await fetch("/api/all", { credentials: "include" });
        if (!response.ok) throw new Error("Ошибка получения песен");
        const songs = await response.json();

        favoriteSongsContainer.innerHTML = "";
        if (Object.keys(songs).length === 0) {
            favoriteSongsContainer.innerHTML = "<p><em>Нет песен</em></p>";
            return;
        }

        for (const [name, url] of Object.entries(songs)) {
            const div = document.createElement("div");
            div.className = "song";
            div.innerHTML = `
                <p><strong>${name}</strong></p>
                <button onclick="playSong('${encodeURI(url)}')">▶</button>
            `;
            favoriteSongsContainer.appendChild(div);
        }
    } catch (err) {
        console.error(err);
        favoriteSongsContainer.innerHTML = "<p>Ошибка загрузки песен</p>";
    }
}

// проигрывание
function playSong(url) {
    player.src = url;
    player.play();
}
window.playSong = playSong;

window.addEventListener("DOMContentLoaded", () => {
    loadUser();
    loadFavoriteAlbums();
    loadFavoriteSongs();
});
