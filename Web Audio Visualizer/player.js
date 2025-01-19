document.addEventListener('DOMContentLoaded', function() {
    let audioContext;
    let analyser;
    let dataArray;
    let source;
    let audioElement;
    let isPlaying = false;
    let isFileLoaded = false;
    let hueRotation = 0;
    let animationFrameId;

    // Initialize audio context on user interaction
    function initAudioContext() {
        if (!audioContext) {
            audioContext = new (window.AudioContext || window.webkitAudioContext)();
            analyser = audioContext.createAnalyser();
            analyser.fftSize = 2048;
            dataArray = new Uint8Array(analyser.frequencyBinCount);
        }
    }

    // Progress bar functionality
    const progressBar = document.querySelector('.progress-bar');
    const progress = document.querySelector('.progress');
    const currentTimeDisplay = document.getElementById('currentTime');
    const durationDisplay = document.getElementById('duration');

    function formatTime(seconds) {
        const mins = Math.floor(seconds / 60);
        const secs = Math.floor(seconds % 60);
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    }

    function updateProgress() {
        if (audioElement && !isNaN(audioElement.duration)) {
            const percentage = (audioElement.currentTime / audioElement.duration) * 100;
            progress.style.width = `${percentage}%`;
            currentTimeDisplay.textContent = formatTime(audioElement.currentTime);
            durationDisplay.textContent = formatTime(audioElement.duration);
        }
    }

    progressBar.addEventListener('click', function(e) {
        if (audioElement && isFileLoaded) {
            const rect = this.getBoundingClientRect();
            const pos = (e.clientX - rect.left) / rect.width;
            audioElement.currentTime = pos * audioElement.duration;
        }
    });

    // File input change handler
    document.getElementById('audioFile').addEventListener('change', async function(event) {
        const file = event.target.files[0];
        if (file) {
            await loadAndPlayFile(file);
        }
    });

    // Play/Pause button handler
    document.getElementById('playPauseButton').addEventListener('click', async function() {
        if (!isFileLoaded) {
            alert('Please select an audio file or drag and drop one.');
            return;
        }

        try {
            if (isPlaying) {
                await audioElement.pause();
                isPlaying = false;
                morphPauseToPlay();
            } else {
                await audioElement.play();
                isPlaying = true;
                morphPlayToPause();
            }
        } catch (error) {
            console.error('Error with audio playback:', error);
        }
    });

    // Drag and drop handlers
    const dropArea = document.getElementById('dropArea');

    dropArea.addEventListener('dragover', (event) => {
        event.preventDefault();
        dropArea.style.borderColor = '#e63946';
    });

    dropArea.addEventListener('dragleave', () => {
        dropArea.style.borderColor = '#ff4081';
    });

    dropArea.addEventListener('drop', async (event) => {
        event.preventDefault();
        dropArea.style.borderColor = '#ff4081';
        
        const files = event.dataTransfer.files;
        if (files.length > 0) {
            await loadAndPlayFile(files[0]);
        }
    });

    dropArea.addEventListener('click', () => {
        document.getElementById('audioFile').click();
    });

    async function loadAndPlayFile(file) {
        initAudioContext();
        const audioURL = URL.createObjectURL(file);
        const fileNameElement = document.getElementById('fileName');
        const fileNameWithoutExtension = file.name.replace(/\.mp3$/, ''); // or use slice method
        fileNameElement.innerHTML = `Now Playing: <br>${fileNameWithoutExtension}`;
        await setupAudioContext(audioURL);
        isFileLoaded = true;
        
        try {
            await audioElement.play();
            isPlaying = true;
            morphPlayToPause();
        } catch (error) {
            console.error('Error playing audio:', error);
        }
    }

    async function setupAudioContext(audioURL) {
        try {
            if (audioElement) {
                audioElement.pause();
                audioElement.src = '';
                if (source) {
                    source.disconnect();
                }
                if (animationFrameId) {
                    cancelAnimationFrame(animationFrameId);
                }
            }
    
            audioElement = new Audio();
            audioElement.crossOrigin = "anonymous";  // Add this line
            audioElement.src = audioURL;
            
            // Wait for the audio to be loaded enough to play
            await new Promise((resolve, reject) => {
                audioElement.addEventListener('canplay', resolve);
                audioElement.addEventListener('error', reject);
                // Start loading the audio
                audioElement.load();
            });
    
            source = audioContext.createMediaElementSource(audioElement);
            source.connect(analyser);
            analyser.connect(audioContext.destination);
    
            audioElement.addEventListener('timeupdate', updateProgress);
            visualize();
            
            console.log('Audio setup complete');
        } catch (error) {
            console.error('Error in setupAudioContext:', error);
            throw error;
        }
    }

    function visualize() {
        animationFrameId = requestAnimationFrame(visualize);
        analyser.getByteFrequencyData(dataArray);
        
        const canvas = document.getElementById('canvas');
        const ctx = canvas.getContext('2d');
    
        // Make canvas responsive
        canvas.width = canvas.offsetWidth;
        canvas.height = canvas.offsetHeight;
    
        // Clear the entire canvas
        ctx.fillStyle = 'rgba(26, 26, 26, 1)'; // Made opacity 1 for complete clear
        ctx.fillRect(0, 0, canvas.width, canvas.height);
    
        const barWidth = (canvas.width / dataArray.length) * 2.5;
        const maxHeight = canvas.height - 1; // Leave some padding at top and bottom
        let x = 0;
    
        // Slower rainbow rotation
        hueRotation = (hueRotation + 0.2) % 360;
    
        for (let i = 0; i < dataArray.length; i++) {
            // Scale the height proportionally to canvas height
            const barHeight = (dataArray[i] / 255) * maxHeight;
    
            // Smoother color transition
            const hue = (i * 2 + hueRotation) % 360;
            ctx.fillStyle = `hsl(${hue}, 70%, 50%)`;
    
            // Round the top of the bars
            ctx.beginPath();
            ctx.moveTo(x + barWidth/2, canvas.height);
            ctx.lineTo(x + barWidth/2, canvas.height - barHeight);
            ctx.lineWidth = barWidth;
            ctx.lineCap = 'round';
            ctx.strokeStyle = ctx.fillStyle;
            ctx.stroke();
    
            x += barWidth + 1.5;
        }
    }
});