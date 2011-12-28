var http = require('http');

//options
var port     = 1337;
var delay    = 2000; // in ms

http.createServer(function (req, res) {

  setTimeout(function (){
    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end('OK!\n');
  }, delay); 
  
}).listen(port, "127.0.0.1");
console.log('Server running at http://127.0.0.1:'+port);
