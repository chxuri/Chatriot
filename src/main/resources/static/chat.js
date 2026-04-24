let currentClassId = ""; 
let currentPeriod = "";
let currentUserName = "Alice";
const host = window.location.host;
const protocol = window.location.protocol === "https:" ? "wss:" : "ws:";
const socket = new WebSocket(`${protocol}//${host}/chat`);
socket.binaryType = "blob";
//mvn spring-boot:run
socket.onopen = () => {
    console.log("connected to " + socket.url);
    //document.getElementById("chat-container").style.display = "block";
};

function sendMessage() {
    const input = document.getElementById("messageInput");
    //const messages = document.getElementById("messages");

    //const messageText = String(input.value);
    
    const messageText = {
        sender: currentUserName,
        content: input.value,
        classId: currentClassId,
        period: currentPeriod
    };
    
    //if(messageText.trim() === "") return;

    //console.log("Socket state:", socket.readyState);
    //console.log("Sending type:", typeof messageText);
    socket.send(JSON.stringify(messageText));
    console.log(JSON.stringify(messageText));
    input.value = "";

};

const input = document.getElementById("enterButton");

input.addEventListener("keydown", function(event) {
  if (event.key === "Enter") {
    //makes sure enter key doesnt trigger anything else
    event.preventDefault();
    document.getElementById("enterButton").click();
  }
});




//HERE******* just figured out how to put buttons in list format. 
//find a way to make a method that sends over the info from when someone clicks button
//maybe share the variables between the js and java files???
//need a way to detect the id and decide which messages get shown to whom


// slash before file name helps avoid 404 errors
fetch("/classes.json")
    //returns string of bytes
    .then(response => response.json())
    //transforms the bytes into data
    .then(data => {
        const container = document.getElementById("classroom-table-info");

        //note to self: actually organize periods later since rn its getting sent to the same classID regardless of period
        data.forEach(classroom => {
            //join part puts the periods into one html string
            let periodOptions = `<option value="" disabled selected>Select Period</option>`;
            periodOptions += classroom.periods.map(p => `
                <option value="${p}">Period ${p}</option>
            `).join("");

            //back ticks allow multi line html!
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
                        <button type="button" class="button" onclick="joinClass('${classroom.classId}', '${classroom.subject}')">Join</button>
                    </td>
                </tr>
            `;
            container.innerHTML += row;
        });

        //timeout makes sure messages have loaded
        setTimeout(() => {
                const lastMessage = messageArea.lastElementChild;
                if (lastMessage) {
                    lastMessage.scrollIntoView({ behavior: 'smooth' });
                }
        }, 50);
    });

function formatTimestamp(timestamp)
{
    if(!timestamp) return "";

    const time = new Date(timestamp);
    const now = new Date();
    
    if (time.toDateString() === now.toDateString())
    {
        return time.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
        hour12: true
        });
    }
    else {
        return time.toLocaleDateString([], {
            month: "short",
            day: "numeric"
        }) + ", " + time.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
        hour12: true
        });
    }
    
}
socket.onmessage = function(event) {
    const messages = document.getElementById("messages");
    const data = JSON.parse(event.data);
    const msg = document.createElement("div");
    msg.className = "single-message";
    msg.textContent = `${formatTimestamp(data.timestamp)} ${data.sender}: ${data.content}`;
    messages.appendChild(msg);
    console.log("inside onmessage");
    messageArea.scrollTop = messageArea.scrollHeight;
};

function joinClass(classId, subject) {
    document.getElementById("messages").innerHTML = "";
    const chatHeadName = document.getElementById("chat-head");
    const dropdown = document.getElementById(`period-${classId}`);
    currentPeriod = dropdown.value;

    currentClassId = classId;
    
    if(!currentPeriod)
    {
        //makes sure they select a period and stops before sending to backend
        alert("Select a period before joining!");
        return;
    }
    console.log(`Joining class: ${classId}, Period: ${currentPeriod}`);



    const joinSignal = {
        sender: currentUserName,
        content: "joined class",
        classId: classId,
        period: currentPeriod,
        type: "JOIN" 
    };

    //sends joinsignal to backend as string
    socket.send(JSON.stringify(joinSignal));

    //switches ui
    document.getElementById("waiting-room").style.display = "none";
    document.getElementById("chat-container").style.display = "block";
    chatHeadName.innerText = `Live Chat for ${subject} Period ${currentPeriod}!`;
    //socket.send(JSON.stringify(messageText));
}

function backToWaitingPage()
{
    document.getElementById("waiting-room").style.display = "block";
    document.getElementById("chat-container").style.display = "none";
}

/*
socket.addEventListener("message", function(event) {
    //appends messages to chat box, both when sent and old messages
    console.log("inside event listener");
    const messages = document.getElementById("messages");
    const msg = document.createElement("div");
    msg.textContent = event.data;
    messages.appendChild(msg);
});
*/

/*
socket.onclose("message", async (data) => {
    const newMessage = new MessageChannel({
        class_id: data.classId,
        sender_name: data.userName,
        content: data.text
    });

    await newMessage.save();

    io.to(data.classId).emit("broadcast", newMessage);
});
*/
//who wrote this method ^^?  im so confused lol

socket.onclose = () => {
    console.log("disconnected");
};
//socket.send(JSON.stringify({ user, text }));
//socket.onmessage = (event) => {
    //console.log(event.data);
//};

/*

*/
