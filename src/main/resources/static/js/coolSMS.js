function sendVerificationCode() {
    const phone = document.getElementById('phone').value;
    fetch('/send-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'phone': phone
        })
    })
        .then(response => response.text())
        .then(result => {
            if (result === 'success') {
                alert('Verification code sent!');
            } else {
                alert('Failed to send verification code.');
            }
        });
}

function verifyCode() {
    const code = document.getElementById('code').value;
    fetch('/verify-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'code': code
        })
    })
        .then(response => response.text())
        .then(result => {
            if (result === 'success') {
                alert('Verification successful!');
            } else {
                alert('Verification failed. Please try again.');
            }
        });
}