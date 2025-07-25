document.addEventListener('DOMContentLoaded', function () {
    // Sayfa elementlerini seç
    const homeButton = document.getElementById('homeButton');
    const dreamWorldButton = document.getElementById('dreamWorldButton');
    const profileButton = document.getElementById('profileButton');
    const loginButton = document.getElementById('loginButton');
    const contentContainer = document.querySelector('.content');

    // Modal elementleri
    const commentModal = document.getElementById('commentModal');
    const closeModal = document.getElementById('closeModal');
    const commentInput = document.getElementById('commentInput');
    const submitCommentButton = document.getElementById('submitCommentButton');
    let currentDreamId = null;

    // Tarih formatlama yardımcı fonksiyonu
    function formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('tr-TR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    // Authentication kontrolü
    function checkAuthentication() {
        return fetch('/api/user/profile', {
            method: 'GET',
            credentials: 'include'
        }).then(response => {
            if (response.ok) {
                return true;
            } else {
                return false;
            }
        }).catch(() => false);
    }

    // 401'de login'e yonlendir
    function handleUnauthorized(response) {
        if (response.status === 401) {
            window.location.href = '/login';
            return Promise.reject('Yetkisiz erişim!');
        }
        return response;
    }

    // Sayfa yüklendiğinde authentication kontrolü
    checkAuthentication().then(isAuthenticated => {
        if (isAuthenticated) {
            loginButton.style.display = 'none';
            loadHomePage();
        } else {
            loginButton.style.display = 'block';
            loadLoginPage();
        }
    });

    function loadLoginPage() {
        contentContainer.innerHTML = `
        <div class="login-container">
            <h1>Dream Catcher</h1>
            <p>Devam etmek için Google hesabınızla giriş yapın.</p>
            <a href="/oauth2/authorization/google" class="google-login-button">
                Google ile Giriş Yap
            </a>
        </div>
    `;
    }

    // Giriş Yap butonuna event listener ekleme
    loginButton.addEventListener('click', function() {
        window.location.href = '/login';
    });

    // Sayfa içeriğini yükleyen yardımcı fonksiyonlar
    function loadHomePage() {
        contentContainer.innerHTML = `
            <h1>Dream Catcher</h1>
            <div class="dream-entry">
                <textarea id="dreamInput" placeholder="Rüyanızı buraya yazın (Maks. 300 karakter)..." maxlength="300"></textarea><br>
                <button id="interpretButton">Rüyamı Yorumla</button>
                <button id="saveButton" disabled>Rüyayı Kaydet</button>
                <div class="loading" id="loading">Yorumlanıyor...</div>
                <div id="result"></div>
            </div>
        `;
        bindDreamEntryEvents();
    }

    function loadDreamWorld() {
        fetch('/api/dream/all-dreams', {
            credentials: 'include'
        })
            .then(response => response.json())
            .then(data => {
                const dreamsHtml = data.map(dream => `
                    <div class="card" data-id="${dream.id}">
                        <div class="card-header">
                            ${dream.dream}
                            <div class="card-date">📅 ${formatDate(dream.createdAt)}</div>
                        </div>
                        <div class="card-body">${dream.dreamInterpretation}</div>
                        <div class="card-footer">
                            <button onclick="openCommentModal(${dream.id})">Yorum Yap</button>
                            <button onclick="loadComments(${dream.id})">Yorumları Göster</button>
                        </div>
                        <div class="comments-container" style="display: none;"></div>
                    </div>
                `).join('');
                contentContainer.innerHTML = `<h1>Rüyalar Alemi</h1>${dreamsHtml}`;
            })
            .catch(() => alert('Rüyalar yüklenirken bir hata oluştu!'));
    }

    function loadProfilePage() {
        fetch('/api/user/profile', {
            credentials: 'include'
        })
            .then(response => handleUnauthorized(response))
            .then(response => response.json())
            .then(user => {
                contentContainer.innerHTML = `
                    <div class="profile-card">
                        <img src="${user.picture}" alt="Profil Fotoğrafı" class="profile-picture" />
                        <div class="profile-info">Ad: ${user.firstName} ${user.lastName}</div>
                        <div class="profile-info">Email: ${user.email}</div>
                    </div>
                    <h2 style="text-align:center;">Kaydedilen Rüyalar</h2>
                    <div id="userDreams"></div>
                `;
                loadUserDreams();
            })
            .catch(() => alert('Profil yüklenirken hata oluştu!'));
    }

    function loadUserDreams() {
        fetch('/api/user/dreams', {
            credentials: 'include'
        })
            .then(response => handleUnauthorized(response))
            .then(response => response.json())
            .then(dreams => {
                const dreamsContainer = document.getElementById('userDreams');
                const dreamsHtml = dreams.map(dream => `
                    <div class="card" data-id="${dream.id}">
                        <div class="card-header">
                            ${dream.dream}
                            <div class="card-date">📅 ${formatDate(dream.createdAt)}</div>
                        </div>
                        <div class="card-body">${dream.dreamInterpretation}</div>
                        <div class="card-footer">
                            <button onclick="deleteDream(${dream.id})">Sil</button>
                        </div>
                    </div>
                `).join('');
                dreamsContainer.innerHTML = dreamsHtml;
            })
            .catch(() => alert('Kaydedilen rüyalar yüklenirken hata oluştu!'));
    }

    // Rüya yorumlama ve kaydetme işlemleri
    function bindDreamEntryEvents() {
        const dreamInput = document.getElementById('dreamInput');
        const interpretButton = document.getElementById('interpretButton');
        const saveButton = document.getElementById('saveButton');
        const resultContainer = document.getElementById('result');
        const loadingIndicator = document.getElementById('loading');

        interpretButton.addEventListener('click', function () {
            const dream = dreamInput.value.trim();
            if (dream) {
                loadingIndicator.style.display = 'block';

                fetch('/api/dream', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    credentials: 'include',
                    body: JSON.stringify({
                        dream: dream
                    })
                })
                    .then(response => response.json())
                    .then(data => {
                        resultContainer.innerHTML = `<strong>Yorum:</strong> ${data.content}`;
                        resultContainer.style.display = 'block';
                        saveButton.disabled = false;
                    })
                    .catch(() => alert('Rüya yorumu alınırken bir sorun oluştu!'))
                    .finally(() => loadingIndicator.style.display = 'none');
            } else {
                alert("Lütfen bir rüya girin!");
            }
        });

        saveButton.addEventListener('click', function () {
            const dream = dreamInput.value.trim();
            const dreamInterpretation = resultContainer.innerText.replace('Yorum:', '').trim();

            fetch('/api/dream/add-dream', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({
                    dream: dream,
                    dreamInterpretation: dreamInterpretation
                })
            })
                .then(response => handleUnauthorized(response))
                .then(response => response.json())
                .then(() => {
                    alert('Rüya başarıyla kaydedildi!');
                    dreamInput.value = '';
                    resultContainer.style.display = 'none';
                    saveButton.disabled = true;
                })
                .catch(error => {
                    console.error(error);
                    alert('Rüya kaydedilirken bir hata oluştu.');
                });
        });
    }

    window.openCommentModal = function (dreamId) {
        currentDreamId = dreamId;
        commentModal.style.display = 'block';
    };

    closeModal.addEventListener('click', () => {
        commentModal.style.display = 'none';
        commentInput.value = '';
    });

    //add-comment
    submitCommentButton.addEventListener('click', function () {
        const commentText = commentInput.value.trim();

        if (commentText && currentDreamId) {
            fetch('/api/comment/add-comment', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({
                    comment: commentText,
                    dreamId: currentDreamId
                })
            })
                .then(response => handleUnauthorized(response))
                .then(() => {
                    alert('Yorum başarıyla eklendi!');
                    commentModal.style.display = 'none';
                    commentInput.value = '';
                    loadComments(currentDreamId);
                })
                .catch(() => alert('Yorum eklenirken bir hata oluştu!'));
        } else {
            alert('Lütfen bir yorum yazın.');
        }
    });

    //get-comments of dream
    window.loadComments = function (dreamId) {
        fetch(`/api/dream/${dreamId}/all-comments`, {
            credentials: 'include'
        })
            .then(response => response.json())
            .then(comments => {
                const container = document.querySelector(`.card[data-id="${dreamId}"] .comments-container`);
                if (comments.length === 0) {
                    container.innerHTML = '<div class="comment">Henüz yorum yapılmamış.</div>';
                } else {
                    container.innerHTML = comments.map(comment => 
                        `<div class="comment"><b>${comment.userName || 'Anonim'}:</b> ${comment.comment}</div><hr/>`
                    ).join('');
                }
                container.style.display = 'block';
            })
            .catch(() => alert('Yorumlar yüklenirken bir hata oluştu!'));
    };

    //delete-dream
    window.deleteDream = function (dreamId) {
        if (confirm('Bu rüyayı silmek istediğinizden emin misiniz?')) {
            fetch(`/api/dream/delete-dream/${dreamId}`, { 
                method: 'DELETE',
                credentials: 'include'
            })
                .then(response => handleUnauthorized(response))
                .then(() => {
                    alert('Rüya silindi.');
                    loadProfilePage();
                })
                .catch(() => alert('Rüya silinirken bir hata oluştu!'));
        }
    };

    homeButton.addEventListener('click', loadHomePage);
    dreamWorldButton.addEventListener('click', loadDreamWorld);
    profileButton.addEventListener('click', loadProfilePage);
});
