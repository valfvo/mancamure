const GameType = {
  "EVE": 0,
  "PVE": 1,
  "PVP": 2
};

const Role = {
  "OBSERVER": 0,
  "PLAYER": 1
};

const settings = {
  gameType: parseInt(sessionStorage.getItem('gameType')),
  role: parseInt(sessionStorage.getItem('role')),
  isAbPruningEnabled: sessionStorage.getItem('isAbPruningEnabled') === 'true',
  depth: parseInt(sessionStorage.getItem('depth'))
};

if (isNaN(settings.gameType) || isNaN(settings.role)) {
  window.location = 'play.html';
} else if (
  settings.gameType != GameType.PVP && isNaN(settings.depth)
) {
  window.location = 'play.html';
}

const socket = io('wss://mancamure.fvo.app', {path: '/ws/socket.io'});

function isValid(rect) {
  return rect.every(point => {
    return (50 - point.x) ** 2 + (50 - point.y) ** 2 <= 50 ** 2;
  });
}

function makeSeed() {
  const defaultRect = [
    {x: -24, y: -15}, {x: 24, y: -15},
    {x: -24, y: 15}, {x: 24, y: 15}
  ];

  let angle = Math.random() * 2 * Math.PI;
  angle = Math.round((angle + Number.EPSILON) * 100) / 100;

  const rotatedRect = defaultRect.map(point => {
    return {
      x: point.x * Math.cos(angle) - point.y * Math.sin(angle),
      y: point.x * Math.sin(angle) + point.y * Math.cos(angle)
    };
  });

  let rect, x, y;
  do {
    x = Math.floor(Math.random() * 124);
    y = Math.floor(Math.random() * 115);

    rect = rotatedRect.map(point => {
      return {x: point.x + x, y: point.y + y};
    });
  } while (!isValid(rect));

  const seed = document.createElement('div');

  seed.classList.add('seed');
  seed.style.left = x - 24 + 'px';
  seed.style.top = y - 15 + 'px';
  seed.style.transform = `rotate(${angle}rad)`;

  seed.dataset.angle = angle;

  return seed;
}

function makeBoard() {
  const board = document.querySelector('.board');
  const seedsPerPit = 4;
  const pits = board.querySelectorAll('.pit');

  for (const pit of pits) {
    for (let i = 0; i < seedsPerPit; ++i) {
      pit.appendChild(makeSeed());
    }
  }
}

const playerPits = document.querySelectorAll('.player-element.pit');

const shift = 0.15;

for (const pit of playerPits) {
  pit.onmouseenter = function(e) {
    for (const seed of this.querySelectorAll('.seed')) {
      const newAngle = parseFloat(seed.dataset.angle) + shift;
      seed.dataset.angle = newAngle;
      seed.style.transform = `rotate(${newAngle}rad)`;
    }
  };

  pit.onmouseleave = function(e) {
    for (const seed of this.querySelectorAll('.seed')) {
      const newAngle = parseFloat(seed.dataset.angle) - shift;
      seed.dataset.angle = newAngle;
      seed.style.transform = `rotate(${newAngle}rad)`;
    }
  };
}

socket.on('connect', () => {
  socket.emit('game-connection', settings);
});

socket.on('game-connection-established', boardData => {
  const pits = document.querySelectorAll('.pit');
  pits.forEach((pit, index) => {
    const pitData = boardData.pits[index];
    pit.classList.add(`pos-${pitData.x}-${pitData.y}`);
    pit.dataset.x = pitData.x;
    pit.dataset.y = pitData.y;
    pit.dataset.seedCount = pitData.seedCount;

    for (let i = 0; i < pitData.seedCount; ++i) {
      pit.appendChild(makeSeed());
    }
  });

  const banks = document.querySelectorAll('.bank');
  banks.forEach((bank, index) => {
    const bankData = boardData.banks[index];
    bank.classList.add(`pos-${bankData.position}`);
    bank.dataset.position = bankData.position;
    bank.dataset.seedCount = bankData.seedCount;

    for (let i = 0; i < bankData.seedCount; ++i) {
      bank.appendChild(makeSeed());
    }
  });
});

socket.on('board-update', boardData => {
  console.log(boardData);
  // for (const pitData of boardData.pits) {
  //   const pit = document.querySelector(`.pos-${pitData.x}-${pitData.y}`);
  //   pit.dataset.seedCount = pitData.seedCount;

  //   while (pit.childElementCount > pitData.seedCount) {
  //     pit.lastElementChild.remove();
  //   }
  //   while (pit.childElementCount < pitData.seedCount) {
  //     pit.appendChild(makeSeed());
  //   }
  // }

  // for (const bankData of boardData.banks) {
  //   const bank = document.querySelector(`.pos-${bankData.position}`);
  //   bank.dataset.seedCount = bankData.seedCount;

  //   while (bank.childElementCount > bankData.seedCount) {
  //     bank.lastElementChild.remove();
  //   }
  //   while (bank.childElementCount < bankData.seedCount) {
  //     bank.appendChild(makeSeed());
  //   }
  // }
});

let pendingAnimations = [];

function playPendingAnimations() {
  if (pendingAnimations.length > 0) {
    pendingAnimation = pendingAnimations.shift();

    if (typeof pendingAnimation === "function") {
      pendingAnimation();
    } else {
      playPendingAnimations();
    }
  }
}

let movementsHistory = [];
let currentMovementIndex = 0;
let isMovementPlaying = false;

function animateMovement() {
  if (isMovementPlaying || currentMovementIndex >= movementsHistory.length) {
    playPendingAnimations();
    return;
  }

  isMovementPlaying = true;

  const movementData = movementsHistory[currentMovementIndex];
  const { x, y, seedCount } = movementData.startingPit;
  const startingPit = document.querySelector(`.pos-${x}-${y}`);

  for (let i = 0; i < seedCount; ++i) {
    const seed = startingPit.children[seedCount - 1 - i];

    const destinationPitData = movementData.destinationPits[i];
    const destinationPit = document.querySelector(
      `.pos-${destinationPitData.x}-${destinationPitData.y}`
    );

    const destinationLeft =
      destinationPit.offsetLeft - startingPit.offsetLeft + parseInt(seed.style.left);

    const destinationTop =
      destinationPit.offsetTop - startingPit.offsetTop + parseInt(seed.style.top);

    setTimeout(() => {
      seed.style.zIndex = i + 1;
      animation = seed.animate({
        left: destinationLeft + 'px',
        top: destinationTop + 'px',
      }, 600);

      animation.onfinish = () => {
        seed.style.zIndex = '';

        destinationPit.appendChild(seed);
        destinationPit.dataset.seedCount =
          parseInt(destinationPit.dataset.seedCount) + 1;

        if (i == seedCount - 1) {  // callback of the last seed
          isMovementPlaying = false;
          startingPit.dataset.seedCount = 0;

          for (let i = 0; i < movementData.opponentPits.length; ++i) {
            const opponentPitData = movementData.opponentPits[i];

            if (opponentPitData.shallBeCollected) {
              const opponentPit = document.querySelector(
                `.pos-${opponentPitData.x}-${opponentPitData.y}`
              );

              const receivingBank = document.querySelector(
                `.pos-${opponentPitData.receivingBank}`
              );

              for (let i = 0; i < opponentPit.dataset.seedCount; ++i) {
                receivingBank.appendChild(opponentPit.lastChild);
              }

              receivingBank.dataset.seedCount =
                parseInt(receivingBank.dataset.seedCount)
                + parseInt(opponentPit.dataset.seedCount);

              opponentPit.dataset.seedCount = 0;
            }
          }

          ++currentMovementIndex;
          animateMovement();
        }
      }
    }, 100 * i);
  }
}

socket.on('movement', movementData => {
  // {startingPit: {x, y, seedCount}, destinationPits: [{x, y}, {x, y}, ...]}
  console.log(movementData);
  movementsHistory.push(movementData);
  animateMovement();
});

socket.on('play-request', boardData => {
  for (const playerPit of boardData.playerPits) {
    if (playerPit.legality == 0) continue;

    const position = `.pos-${playerPit.x}-${playerPit.y}`;
    const pit = document.querySelector(position);

    if (playerPit.legality == 2) {
      pit.classList.add('legal');

      pit.onclick = function(e) {
        for (const legalPit of document.querySelectorAll('.legal')) {
          legalPit.classList.remove('legal');
          legalPit.onclick = null;
        }
        for (const illegalPit of document.querySelectorAll('.illegal')) {
          illegalPit.classList.remove('illegal');
        }

        socket.emit('movement-played', {
          x: parseInt(this.dataset.x),
          y: parseInt(this.dataset.y)
        });
      };
    } else {  // playerPit.legality == 1
      pit.classList.add('illegal');
    }
  }
});

socket.on('game-end', json => {
  console.log('game end');

  pendingAnimations.push(() => {
    const seeds = document.querySelectorAll('.seed');

    for (const seed of seeds) {
      seed.style.filter = "grayscale(1)";
    }

    for (const bank of document.querySelectorAll('.bank')) {
      if (bank.childElementCount >= 25) {
        bank.style.color = "#8ae68a";
      } else {
        bank.style.color = "#f75c54";
      }
    }

    playPendingAnimations();
  });
});
