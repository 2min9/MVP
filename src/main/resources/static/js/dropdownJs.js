    document.addEventListener('DOMContentLoaded', function() {
        const dropdownToggle = document.querySelector('.dropdown-toggle');
        const logDataButtonContainer = document.getElementById('logDataButtonContainer');

        dropdownToggle.addEventListener('click', function() {
            setTimeout(function() {
                if (dropdownToggle.getAttribute('aria-expanded') === 'true') {
                    logDataButtonContainer.style.marginTop = '150px';
                } else {
                    logDataButtonContainer.style.marginTop = '0';
                }
            }, 300); // Adjust timeout to ensure it captures the dropdown state change
        });
    });