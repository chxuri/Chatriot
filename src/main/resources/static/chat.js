const host = window.location.host;
const protocol = window.location.protocol === "https:" ? "wss:" : "ws";
const socket = new WebSocket(`${protocol}//${host}/chat`);
socket.binaryType = "blob";
//mvn spring-boot:run
socket.onopen = () => {
    console.log("connected!");
    document.getElementById("chat-container").style.display = "block";
};

function sendMessage() {
    const input = document.getElementById("messageInput");
    //const messages = document.getElementById("messages");

    //const messageText = String(input.value);
    
    const messageText = {
        sender: "Alice",
        content: input.value,
        classId: "PreCalc_1"
    };
    
    //if(messageText.trim() === "") return;

    //console.log("Socket state:", socket.readyState);
    //console.log("Sending type:", typeof messageText);
    socket.send(JSON.stringify(messageText));
    console.log(JSON.stringify(messageText));
    input.value = "";

};





//HERE******* just figured out how to put buttons in list format. 
//find a way to make a method that sends over the info from when someone clicks button
//maybe share the variables between the js and java files???
//need a way to detect the id and decide which messages get shown to whom


// slash before file name helps avoid 404 errors
fetch("/classes.json")
    .then(response => response.json())
    .then(data => {
        const container = document.getElementById("classroom-table-info");

        //note to self: actually organize periods later since rn its getting sent to the same classID regardless of period
        data.forEach(classroom => {
            const periodOptions = classroom.periods.map(p => `
                <option value="${p}" ${p === classroom.period ? "selected" : ""}>Period ${p}</option>
            `).join("");

            const row = `
                <tr>
                    <td>${classroom.subject}</td>
                    <td>${classroom.teacher}</td>
                    <td>
                        <select id="period-${classroom.classId}">
                            ${periodOptions}
                        </select>
                    </td>
                    <td>
                        <button type="button" class="button" onclick="joinClass('${classroom.classId}')">Join</button>
                    </td>
                </tr>
            `;
            container.innerHTML += row;
        });
    });
socket.onmessage = function(event) {
    const messages = document.getElementById("messages");
    const data = JSON.parse(event.data);
    const msg = document.createElement("div");
    msg.textContent = `${data.sender}: ${data.content}`;
    messages.appendChild(msg);
    console.log("inside onmessage");
};

function joinClass(classId) {
    const dropdown = document.getElementById(`period-${classId}`);
    const selectedPeriod = dropdown.value;

    console.log(`Joining class: ${classId}, Period: ${selectedPeriod}`);

    const joinSignal = {
        sender: "Alice",
        content: "joined class",
        classId: classId,
        //period: selectedPeriod, // period from dropdown is now included
        type: "JOIN" 
    };

    //sends joinsignal to backend as string
    socket.send(JSON.stringify(joinSignal));

    //switches ui
    document.getElementById("waiting-room").style.display = "none";
    document.getElementById("chat-container").style.display = "block";
    //socket.send(JSON.stringify(messageText));
}

socket.addEventListener("message", function(event) {
    //appends messages to chat box, both when sent and old messages
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
