const GameType = {
  "EVE": 0,
  "PVE": 1,
  "PVP": 2
};

const Role = {
  "OBSERVER": 0,
  "PLAYER": 1
};

const confirmButton = document.querySelector('#confirm_button');
confirmButton.onclick = function(e) {
  const isAbPruningEnabled = document.querySelector('#algorithm').value;
  sessionStorage.setItem('isAbPruningEnabled', isAbPruningEnabled);

  const depth = document.querySelector('#depth').value;
  sessionStorage.setItem('depth', depth);

  window.location = 'game.html';
}
