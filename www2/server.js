const net = require('net');
const http = require('http');
const server = http.createServer();

const io = require('socket.io')(server, {path: '/ws/socket.io'});

const DataType = {
  "BOARD": 0,
  "MOVEMENT": 1,
  "GAME_END": 2,
  "PLAY_REQUEST": 3
};

const DataSize = [41, 0, 2, 19];

const GameType = {
  "EVE": 0,
  "PVE": 1,
  "PVP": 2
};

class GameResponse {
  constructor() {
    this.buffer = undefined;
    this.dataType = undefined;
    this.dataSize = undefined;
  }

  add(data) {
    if (this.buffer) {
      this.buffer = Buffer.concat([this.buffer, data]);
    } else {
      this.buffer = Buffer.from(data);
      this.updateData();
    }

    while (
      this.dataType != undefined
      && this.buffer.length >= this.dataSize
    ) {
      this.parseData();
    }
  }

  updateData() {
    if (this.buffer && this.buffer.length >= 2) {
      this.dataType = this.buffer[0];
      this.dataSize = DataSize[this.dataType];
      if (this.dataSize == 0) {
        this.dataSize = this.buffer[1];
      }
    } else {
      this.dataType = undefined;
      this.dataSize = undefined;
    }
  }

  onMovement(callback) {
    this.movementCallback = callback;
  }

  onBoardUpdate(callback) {
    this.boardUpdateCallback = callback;
  }

  onGameEnd(callback) {
    this.gameEndCallback = callback;
  }

  onPlayRequest(callback) {
    this.playRequestCallback = callback;
  }

  parseBoard() {
    // JSON : {pits: [{x: .., y: .., seedCount: ..}, {..}]}
    let json = {};
    let offset = 1;

    let pits = new Array(12);
    let banks = new Array(2);

    for (let i = 0; i < 12; ++i) {
      pits[i] = {
        x: this.buffer[offset++],
        y: this.buffer[offset++],
        seedCount: this.buffer[offset++]
      };
    }

    for (let i = 0; i < 2; ++i) {
      banks[i] = {
        position: this.buffer[offset++],
        seedCount: this.buffer[offset++]
      };
    }

    json = {pits: pits, banks: banks};

    if (typeof this.boardUpdateCallback === "function") {
      this.boardUpdateCallback(json);
    }
  }

  parseMovement() {
    // JSON (movementData) : {
    //   startingPit: { x, y, seedCount },
    //   destinationPits: [ { x, y }, ... ],
    //   opponentPits: [ { x, y, shallBeCollected, receivingBank }, ... ] }

    let json = {};
    let offset = 2;

    json.startingPit = {
      x: this.buffer[offset++],
      y: this.buffer[offset++],
      seedCount: this.buffer[offset++]
    };

    json.destinationPits = [];
    for (let i = 0; i < json.startingPit.seedCount; ++i) {
      json.destinationPits[i] = {
        x: this.buffer[offset++],
        y: this.buffer[offset++]
      };
    }

    json.opponentPits = [];
    for (let i = 0; i < 6; ++i) {
      const opponentPit = {
        x: this.buffer[offset++],
        y: this.buffer[offset++],
      }

      const bank = this.buffer[offset++];
      if (bank != 255) {
        opponentPit.shallBeCollected = true;
        opponentPit.receivingBank = bank;
      } else {
        opponentPit.shallBeCollected = false;
      }

      json.opponentPits[i] = opponentPit;
    }

    if (typeof this.movementCallback === "function") {
      this.movementCallback(json);
    }
  }

  parseGameEnd() {
    let json = {};
    let offset = 1;

    if (typeof this.gameEndCallback === "function") {
      this.gameEndCallback(json);
    }
  }

  parsePlayRequest() {
    // JSON : {playerPits: [{x, y, legality}, ...]}
    let json = {};
    let offset = 1;

    let playerPits = new Array(6);
    for (let i = 0; i < 6; ++i) {
      playerPits[i] = {
        x: this.buffer[offset++],
        y: this.buffer[offset++],
        legality: this.buffer[offset++]
      }
    }

    json = { playerPits: playerPits };

    if (typeof this.playRequestCallback === "function") {
      this.playRequestCallback(json);
    }
  }

  parseData() {
    console.log('dataType:', this.dataType);

    switch (this.dataType) {
      case DataType.BOARD:
        this.parseBoard();
        break;
      case DataType.MOVEMENT:
        this.parseMovement();
        break;
      case DataType.GAME_END:
        this.parseGameEnd();
        break;
      case DataType.PLAY_REQUEST:
        this.parsePlayRequest();
        break;
      default:
        break;
    }

    this.buffer = this.buffer.slice(this.dataSize);
    if (this.buffer.length == 0) {
      this.buffer = undefined;
    }
    this.updateData();
  }
}

io.on('connection', socket => {
  console.log('Server: client connected');
  let gameSocket;

  socket.on('game-connection', data => {
    let rawData;
    switch (data.gameType) {
      case GameType.PVE:
        rawData = [
          data.gameType,
          data.role,
          data.isAbPruningEnabled,
          data.depth
        ];
        break;
      case GameType.PVP:
        rawData = [data.gameType, data.role];
        break;
      case GameType.EVE:
        rawData = [
          data.gameType,
          data.role,
          data.isAbPruningEnabledForAIOne,
          data.depthOfAIOne,
          data.isAbPruningEnabledForAITwo,
          data.depthOfAITwo
        ];
        break;
      default:
        break;
    }

    gameSocket = net.createConnection(3034);
    gameSocket.write(new Uint8Array(rawData));

    let isGameConnectionEstablished = false;

    const response = new GameResponse();

    response.onBoardUpdate(json => {
      if (isGameConnectionEstablished) {
        socket.emit('board-update', json);
      } else {
        socket.emit('game-connection-established', json);
        isGameConnectionEstablished = true;
      }
    });

    response.onMovement(json => {
      socket.emit('movement', json);
    });

    response.onPlayRequest(json => {
      console.log('sent play request');
      socket.emit('play-request', json);
    });

    response.onGameEnd(json => {
      console.log('game end');
      socket.emit('game-end', json);
    });

    gameSocket.on('data', data => {
      response.add(data);
    });
    
  });

  socket.on('disconnect', () => {
    console.log('Server: client disconnected');
    if (gameSocket != undefined) {
      gameSocket.destroy();
    }
  });

  socket.on('movement-played', movement => {
    gameSocket.write(new Uint8Array([movement.x, movement.y]));
  });
});

server.listen(3033);
