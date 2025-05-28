// server.js - CORRECTED VERSION

// Use CommonJS require (not ES6 import)
const WebSocket = require('ws');

// Create WebSocket server on port 8080
const wss = new WebSocket.Server({ port: 8080 });

console.log('WebSocket server started on port 8080');

// Handle new client connections
wss.on('connection', function connection(ws) {
    console.log('New client connected');
    
    // Send welcome message to the connected client
    // THIS MUST BE INSIDE the connection handler
    ws.send(JSON.stringify({
        type: 'welcome',
        message: 'Connected to WebSocket server!'
    }));
    
    // Handle messages from client
    // THIS MUST BE INSIDE the connection handler
    ws.on('message', function message(data) {
        console.log('Received:', data.toString());
        
        try {
            const parsedData = JSON.parse(data.toString());
            
            // Echo the message back to the client
            ws.send(JSON.stringify({
                type: 'echo',
                message: `Server received: ${parsedData.message}`,
                timestamp: new Date().toISOString()
            }));
            
        } catch (error) {
            console.error('Error parsing message:', error);
        }
    });
    
    // Handle client disconnect
    // THIS MUST BE INSIDE the connection handler
    ws.on('close', function close() {
        console.log('Client disconnected');
    });
    
    // Handle errors for this specific client
    // THIS MUST BE INSIDE the connection handler
    ws.on('error', function error(err) {
        console.error('WebSocket error:', err);
    });
});

function broadcast(data) {
    wss.clients.forEach(client => {
        if(client.readyState === WebSocket.OPEN){
            client.send(JSON.stringify(data));
        }
    })
}

setInterval(() => {
    const fakeData = {
        type: 'food_update',
        payload: {
            message: 'New food item added!'
        }
    };
    broadcast(fakeData);
},1000)

// Handle server errors (this stays outside)
wss.on('error', function error(err) {
    console.error('Server error:', err);
});