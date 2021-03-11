const cells = document.querySelectorAll('.cell');

for (const cell of cells) {
    cell.onclick = (e) => {
        test.ok("javafx NUL NUL NUL");
        test.ok2(e);
    }
}
