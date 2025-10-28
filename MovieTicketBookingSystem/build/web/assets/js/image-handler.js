// Image loading handler - Common script for all pages
function handleImageLoad(img) {
    img.classList.add('loaded');
}

function handleImageError(img) {
    img.classList.add('error');
    if (img.dataset.fallback) {
        img.src = img.dataset.fallback;
    }
}

function initializeImageHandling() {
    // Handle image loading
    document.querySelectorAll('img').forEach(img => {
        if (img.complete) {
            if (img.naturalHeight !== 0) {
                handleImageLoad(img);
            } else {
                handleImageError(img);
            }
        } else {
            img.addEventListener('load', () => handleImageLoad(img));
            img.addEventListener('error', () => handleImageError(img));
        }
    });
}

// Auto-initialize when DOM is ready
document.addEventListener('DOMContentLoaded', initializeImageHandling);
