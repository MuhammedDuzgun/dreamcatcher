document.addEventListener('DOMContentLoaded', function () {
    // Sayfa elementlerini seç
    const homeButton = document.getElementById('homeButton');
    const dreamWorldButton = document.getElementById('dreamWorldButton');
    const profileButton = document.getElementById('profileButton');
    const contentContainer = document.querySelector('.content');

    // Modal elementleri
    const commentModal = document.getElementById('commentModal');
    const closeModal = document.getElementById('closeModal');
    const commentInput = document.getElementById('commentInput');
    const submitCommentButton = document.getElementById('submitCommentButton');
    let currentDreamId = null;

    // Yeni Giriş Yap butonuna yönlendirme ekle
    loginButton.addEventListener('click', function () {
        window.location.href = '/login'; // /login sayfasına yönlendir
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
        fetch('http://localhost:8080/api/dream/all-dreams')
            .then(response => response.json())
            .then(data => {
                const dreamsHtml = data.map(dream => `
                    <div class="card" data-id="${dream.id}">
                        <div class="card-header">${dream.dream}</div>
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
        fetch('http://localhost:8080/api/user/profile')
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
        fetch('http://localhost:8080/api/user/dreams')
            .then(response => response.json())
            .then(dreams => {
                const dreamsContainer = document.getElementById('userDreams');
                const dreamsHtml = dreams.map(dream => `
                    <div class="card" data-id="${dream.id}">
                        <div class="card-header">${dream.dream}</div>
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
        const loginButton = document.getElementById('loginButton');

        interpretButton.addEventListener('click', function () {
            const dream = dreamInput.value.trim();
            if (dream) {
                loadingIndicator.style.display = 'block';

                // GET isteği ve RequestParam ekleme
                fetch(`http://localhost:8080/api/dream?dream=${encodeURIComponent(dream)}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' }
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
            const dreamInterpretation = resultContainer.innerText.trim();

            fetch('http://localhost:8080/api/dream/add-dream', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    dream: dream,
                    dreamInterpretation: dreamInterpretation
                })
            })
                .then(() => alert('Rüya başarıyla kaydedildi!'))
                .catch(() => alert('Rüya kaydedilirken bir hata oluştu.'));
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

    submitCommentButton.addEventListener('click', function () {
        const commentText = commentInput.value.trim();

        if (commentText && currentDreamId) {
            fetch('http://localhost:8080/api/comment/add-comment', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    comment: commentText,
                    dreamId: currentDreamId
                })
            })
                .then(() => {
                    alert('Yorum başarıyla eklendi!');
                    commentModal.style.display = 'none';
                    loadComments(currentDreamId);
                })
                .catch(() => alert('Yorum eklenirken hata oluştu!'));
        } else {
            alert('Lütfen bir yorum yazın.');
        }
    });

    window.loadComments = function (dreamId) {
        fetch(`http://localhost:8080/api/dream/${dreamId}/all-comments`)
            .then(response => response.json())
            .then(comments => {
                const container = document.querySelector(`.card[data-id="${dreamId}"] .comments-container`);
                container.innerHTML = comments.map(comment => `<div class="comment">${comment.comment}</div>`).join('');
                container.style.display = 'block';
            });
    };

    window.deleteDream = function (dreamId) {
        fetch(`http://localhost:8080/api/dream/delete-dream/${dreamId}`, { method: 'DELETE' })
            .then(() => {
                alert('Rüya silindi.');
                loadProfilePage();
            });
    };

    homeButton.addEventListener('click', loadHomePage);
    dreamWorldButton.addEventListener('click', loadDreamWorld);
    profileButton.addEventListener('click', loadProfilePage);

    loadHomePage();
});
