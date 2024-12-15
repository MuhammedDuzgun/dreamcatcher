document.getElementById('interpretButton').addEventListener('click', function () {
    const dream = document.getElementById('dreamInput').value;
    const resultDiv = document.getElementById('result');
    const loadingDiv = document.getElementById('loading');
    const interpretButton = document.getElementById('interpretButton');
    const saveButton = document.getElementById('saveButton');

    if (!dream) {
        resultDiv.innerHTML = "<strong>Hata:</strong> Lütfen bir rüya girin!";
        resultDiv.style.display = 'block';
        return;
    }

    resultDiv.style.display = 'none';
    loadingDiv.style.display = 'block';
    interpretButton.disabled = true;

    fetch(`http://localhost:8080/api/dream?dream=${encodeURIComponent(dream)}`)
        .then(response => response.json())
        .then(data => {
            loadingDiv.style.display = 'none';
            resultDiv.innerHTML = `<strong>Rüya Yorumu:</strong> ${data.content}`;
            resultDiv.style.display = 'block';
            saveButton.disabled = false;
            saveButton.dataset.dream = dream;
            saveButton.dataset.dreamInterpretation = data.content;
        })
        .catch(error => {
            loadingDiv.style.display = 'none';
            resultDiv.innerHTML = "<strong>Hata:</strong> Bir hata oluştu, lütfen tekrar deneyin.";
            resultDiv.style.display = 'block';
        })
        .finally(() => {
            interpretButton.disabled = false;
        });
});

document.getElementById('saveButton').addEventListener('click', function () {
    const dream = this.dataset.dream;
    const dreamInterpretation = this.dataset.dreamInterpretation;

    fetch('http://localhost:8080/api/dream/add-dream', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('token')}` // OAuth token kullanımı
        },
        body: JSON.stringify({ dream, dreamInterpretation })
    })
        .then(response => {
            if (response.ok) {
                alert('Rüya başarıyla kaydedildi!');
            } else {
                alert('Rüya kaydedilemedi. Lütfen tekrar deneyin.');
            }
        })
        .catch(() => alert('Bir hata oluştu, lütfen tekrar deneyin.'));
});
