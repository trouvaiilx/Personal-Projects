function morphPlayToPause() {
    const playPath = document.querySelector('.play-path');
    const pausePath = document.querySelector('.pause-path');
    
    playPath.style.transition = 'opacity 0.3s';
    playPath.style.opacity = '0';
    
    setTimeout(() => {
        pausePath.style.transition = 'opacity 0.3s';
        pausePath.style.opacity = '1';
    }, 150);
}

function morphPauseToPlay() {
    const playPath = document.querySelector('.play-path');
    const pausePath = document.querySelector('.pause-path');
    
    pausePath.style.transition = 'opacity 0.3s';
    pausePath.style.opacity = '0';
    
    setTimeout(() => {
        playPath.style.transition = 'opacity 0.3s';
        playPath.style.opacity = '1';
    }, 150);
}

// Initialize play button visible
document.querySelector('.play-path').style.opacity = '1';
document.querySelector('.pause-path').style.opacity = '0';