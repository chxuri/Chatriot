const socket = new WebSocket("ws://localhost:9090/chat");
socket.binaryType = "blob";
//mvn spring-boot:run
socket.onopen = () => {
    console.log("connected!");
};

function sendMessage() {
    const input = document.getElementById("messageInput");
    //const messages = document.getElementById("messages");

    //const messageText = String(input.value);
    
    const messageText = {
        sender: "Alice",
        content: input.value,
        classId: "PreCalc-Period1"
    };
    
    //if(messageText.trim() === "") return;

    //console.log("Socket state:", socket.readyState);
    //console.log("Sending type:", typeof messageText);
    socket.send(JSON.stringify(messageText));
    console.log(JSON.stringify(messageText));
    input.value = "";

};

/*
socket.onmessage = function(event) {
    const messages = document.getElementById("messages");
    const msg = document.createElement("div");
    msg.textContent = event.data;
    messages.appendChild(msg);
    messages.innerHTML += `<p>${event.data}</p>`;
    console.log("inside onmessage");
};
*/

socket.addEventListener("message", function(event) {
    console.log("inside event listener");
    const messages = document.getElementById("messages");
    const msg = document.createElement("div");
    msg.textContent = event.data;
    messages.appendChild(msg);
});

socket.onclose("message", async (data) => {
    const newMessage = new MessageChannel({
        class_id: data.classId,
        sender_name: data.userName,
        content: data.text
    });

    await newMessage.save();

    io.to(data.classId).emit("broadcast", newMessage);
});

socket.onclose = () => {
    console.log("disconnected");
};
//socket.send(JSON.stringify({ user, text }));
//socket.onmessage = (event) => {
    //console.log(event.data);
//};

/*

*/
