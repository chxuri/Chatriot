const socket = new WebSocket("ws://localhost:8080/chat");

function sendMessage() {
    const input = document.getElementById("messageInput");
    const messages = document.getElementById("messages");

    const msg = document.createElement("div");
    msg.textContent = input.value;

    messages.appendChild(msg);
    input.value = "";
    socket.send(input.value);

}

socket.onmessage = function(event) {
    const chat = document.getElementById("chat");
    chat.innerHTML += <p>${event.data}</p>
}
//socket.send(JSON.stringify({ user, text }));
//socket.onmessage = (event) => {
    //console.log(event.data);
//};

/*

*/
