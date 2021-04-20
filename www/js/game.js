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

  pit.onclick = function(e) {
    socket.emit('movement-played', {
      x: parseInt(this.dataset.x),
      y: parseInt(this.dataset.y)
    });
  };
}

const GameType = {
  "EVE": 0,
  "PVE": 1,
  "PVP": 2
};

const Role = {
  "OBSERVER": 0,
  "PLAYER": 1
};

socket.on('connect', () => {
  socket.emit('game-connection', {
    gameType: GameType.PVE,
    role: Role.PLAYER,
    isAbPruningEnabled: true,
    depth: 2
  });
});

socket.on('game-connection-established', boardData => {
  const pits = document.querySelectorAll('.pit');
  pits.forEach((pit, index) => {
    const pitData = boardData.pits[index];
    pit.classList.add(`pos-${pitData.x}-${pitData.y}`);
    pit.dataset.x = pitData.x;
    pit.dataset.y = pitData.y;

    for (let i = 0; i < pitData.seedCount; ++i) {
      pit.appendChild(makeSeed());
    }
  });

  const banks = document.querySelectorAll('.bank');
  banks.forEach((bank, index) => {
    const bankData = boardData.banks[index];
    bank.classList.add(`pos-${bankData.position}`);
    bank.dataset.position = bankData.position;

    for (let i = 0; i < bankData.seedCount; ++i) {
      bank.appendChild(makeSeed());
    }
  });
});

socket.on('board-update', boardData => {
  console.log(boardData);
  for (const pitData of boardData.pits) {
    const pit = document.querySelector(`.pos-${pitData.x}-${pitData.y}`);

    while (pit.childElementCount > pitData.seedCount) {
      pit.lastElementChild.remove();
    }
    while (pit.childElementCount < pitData.seedCount) {
      pit.appendChild(makeSeed());
    }
  }

  for (const bankData of boardData.banks) {
    const bank = document.querySelector(`.pos-${bankData.position}`);

    while (bank.childElementCount > bankData.seedCount) {
      bank.lastElementChild.remove();
    }
    while (bank.childElementCount < bankData.seedCount) {
      bank.appendChild(makeSeed());
    }
  }
});

socket.on('game-end', json => {
  console.log('game end');
  const seeds = document.querySelectorAll('.seed');

  for (const seed of seeds) {
    seed.style.filter = "grayscale(1)";
  }
});
