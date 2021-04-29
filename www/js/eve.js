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
  const isAbPruningEnabledForAIOne = document.querySelector('#algorithm').value;
  sessionStorage.setItem('isAbPruningEnabledForAIOne', isAbPruningEnabledForAIOne);

  const depthOfAIOne = document.querySelector('#depth').value;
  sessionStorage.setItem('depthOfAIOne', depthOfAIOne);

  const isAbPruningEnabledForAITwo = document.querySelector('#algorithm2').value;
  sessionStorage.setItem('isAbPruningEnabledForAITwo', isAbPruningEnabledForAITwo);

  const depthOfAITwo = document.querySelector('#depth2').value;
  sessionStorage.setItem('depthOfAITwo', depthOfAITwo);

  window.location = 'game.html';
}
