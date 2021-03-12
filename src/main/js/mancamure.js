const cells = document.querySelectorAll('.cell');

const r = 10;

let i = 0;

for (const cell of cells) {
    cell.onclick = (e) => {
        // test.ok("javafx NUL NUL NUL");
        // test.ok2(e);
    };
    cell.onmouseover = function(e) {
        if (e.relatedTarget.parentNode == this) return;

        for (const seed of this.querySelectorAll('.seed')) {
            const newRotation = parseInt(seed.dataset.rotation) + r;
            seed.dataset.rotation = newRotation;
            seed.style.transform = `rotate(${newRotation}deg)`;
        }
    };
    cell.onmouseout = function(e) {
        if (e.relatedTarget.parentNode == this) return;

        for (const seed of this.querySelectorAll('.seed')) {
            const newRotation = parseInt(seed.dataset.rotation) - r;
            seed.dataset.rotation = newRotation;
            seed.style.transform = `rotate(${newRotation}deg)`;
        }
    };
}

for (const seed of document.querySelectorAll('.seed')) {
    seed.onmouseover = (e) => { e.stopPropagation(); }
    seed.onmouseout = (e) => { e.stopPropagation(); }

    if (seed.classList.contains("seed3")) {
        seed.dataset.rotation = "150";
    } else if (seed.classList.contains("seed2")) {
        seed.dataset.rotation = "45";
    } else {
        seed.dataset.rotation = "0";
    }
}
