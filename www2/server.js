const net = require('net');
const http = require('http');
const server = http.createServer();

const io = require('socket.io')(server, {path: '/ws/socket.io'});

const DataType = {
  "BOARD": 0,
  "MOVEMENT": 1,
  "GAME_END": 2
};

const DataSize = [41, 0, 1];

class GameResponse {
  constructor() {
    this.buffer = undefined;
    this.dataType = undefined;
    this.dataSize = undefined;
  }

  add(data) {
    if (this.buffer) {
      this.buffer = Buffer.concat(this.buffer, data);
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

  onBoardUpdate(callback) {
    this.callback = callback;
  }

  onGameEnd(callback) {
    this.callback2 = callback;
  }

  parseData() {
    switch (this.dataType) {
      case DataType.BOARD:
        // JSON : {pits: [{x: .., y: .., seedCount: ..}, {..}]}
        let pits = new Array(12);
        let banks = new Array(2);
        let offset = 1;

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

        const json = {pits: pits, banks: banks};

        if (typeof this.callback === "function") {
          this.callback(json);
        }

        this.buffer = this.buffer.slice(offset);
        if (this.buffer.length == 0) {
          this.buffer = undefined;
        }
        this.updateData();

        break;
      case DataType.MOVEMENT:
        break
      case DataType.GAME_END:
        let offset2 = 1;

        if (typeof this.callback2 === "function") {
          this.callback2({});
        }

        this.buffer = this.buffer.slice(offset2);
        if (this.buffer.length == 0) {
          this.buffer = undefined;
        }
        this.updateData();
        break;
      default:
        break;
    }
  }
}

io.on('connection', socket => {
  console.log('Server: client connected');
  let gameSocket;

  socket.on('game-connection', data => {
    const rawData = [
      data.gameType,
      data.role,
      data.isAbPruningEnabled,
      data.depth
    ];

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

    response.onGameEnd(json => {
      console.log('game end');
      socket.emit('game-end', json);
    })

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
