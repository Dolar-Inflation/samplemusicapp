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

// загрузка избранных альбомов
async function loadFavoriteAlbums() {
    try {
        const response = await fetch("/my/favorites", { credentials: "include" });
        if (!response.ok) throw new Error("Ошибка получения избранных альбомов");
        const albums = await response.json();

        favoriteAlbumsContainer.innerHTML = "";
        if (albums.length === 0) {
            favoriteAlbumsContainer.innerHTML = "<p><em>Нет избранных альбомов</em></p>";
            return;
        }

        albums.forEach(album => {
            const div = document.createElement("div");
            div.className = "album";
            div.innerHTML = `
                <img src="${album.imageurl}" alt="${album.title}">
                <p><strong>${album.title}</strong></p>
                <p><em>${album.artist}</em></p>
                <p>${album.year}, ${album.genre}</p>
            `;
            favoriteAlbumsContainer.appendChild(div);
        });
    } catch (err) {
        console.error(err);
        favoriteAlbumsContainer.innerHTML = "<p>Ошибка загрузки альбомов</p>";
    }
}

// загрузка избранных песен
async function loadFavoriteSongs() {
    try {
        const response = await fetch("/my/favorite-songs", { credentials: "include" });
        if (!response.ok) throw new Error("Ошибка получения избранных песен");
        const songs = await response.json();

        favoriteSongsContainer.innerHTML = "";
        if (songs.length === 0) {
            favoriteSongsContainer.innerHTML = "<p><em>Нет избранных песен</em></p>";
            return;
        }

        songs.forEach(song => {
            const div = document.createElement("div");
            div.className = "song";
            div.innerHTML = `
                <p><strong>${song.songname}</strong></p>
                <p><em>${song.artist}</em></p>
                <button onclick="playSong('${song.fileUrl}')">▶</button>
            `;
            favoriteSongsContainer.appendChild(div);
        });
    } catch (err) {
        console.error(err);
        favoriteSongsContainer.innerHTML = "<p>Ошибка загрузки песен</p>";
    }
}

// проигрывание
function playSong(url) {
    player.src = encodeURI(url);
    player.play();
}
window.playSong = playSong;

window.addEventListener("DOMContentLoaded", () => {
    loadUser();
    loadFavoriteAlbums();
    loadFavoriteSongs();
});
