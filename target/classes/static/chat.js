const socket = new WebSocket("ws://localhost:8080/chat");
//mvn spring-boot:run
socket.onopen = () => {
    console.log("connected!");
};

function sendMessage() {
    const input = document.getElementById("messageInput");
    const messages = document.getElementById("messages");

    const messageText = input.value;
    if(messageText.trim() === "") return;

    socket.send(messageText);
    input.value = "";

};

socket.onmessage = function(event) {
    const messages = document.getElementById("messages");
    const msg = document.createElement("div");
    msg.textContent = event.data;
    messages.appendChild(msg);
    //messages.innerHTML += `<p>${event.data}</p>`;
};

socket.onclose = () => {
    console.log("disconnected!");
};
//socket.send(JSON.stringify({ user, text }));
//socket.onmessage = (event) => {
    //console.log(event.data);
//};

/*

*/
